package ru.d3m4k.lms.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.d3m4k.lms.service.dto.DisciplineRequestDto;
import ru.d3m4k.lms.service.dto.DisciplineResponseDto;
import ru.d3m4k.lms.service.dto.GroupResponseDto;
import ru.d3m4k.lms.service.entity.Discipline;
import ru.d3m4k.lms.service.entity.Group;
import ru.d3m4k.lms.service.entity.Role;
import ru.d3m4k.lms.service.entity.User;
import ru.d3m4k.lms.service.exception.ResourceConflictException;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.repository.DisciplineRepository;
import ru.d3m4k.lms.service.repository.GroupRepository;
import ru.d3m4k.lms.service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisciplineService {
    private final DisciplineRepository disciplineRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public DisciplineResponseDto createDiscipline(Long teacherId, DisciplineRequestDto dto) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель не найден"));

        disciplineRepository.findByName(dto.getName())
                .ifPresent(discipline -> {
                    throw new ResourceConflictException(
                            "Дисциплина с названием '%s' уже существует".formatted(dto.getName())
                    );
                });

        Discipline discipline = new Discipline();
        discipline.setName(dto.getName());
        discipline.setDescription(dto.getDescription());
        discipline.setCreatedAt(LocalDateTime.now());

        discipline.getTeachers().add(teacher);
        teacher.getDisciplines().add(discipline);

        Discipline savedDiscipline = disciplineRepository.save(discipline);
        userRepository.save(teacher);

        return modelMapper.map(savedDiscipline, DisciplineResponseDto.class);
    }

    @Transactional
    public List<DisciplineResponseDto> getAllDisciplines() {
        return disciplineRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public DisciplineResponseDto getDisciplineById(Long disciplineId) {
        return disciplineRepository.findById(disciplineId)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));
    }

    @Transactional
    public DisciplineResponseDto updateDiscipline(Long teacherId, Long disciplineId, DisciplineRequestDto dto) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));

        checkTeacherAccess(teacherId, discipline);

        discipline.setName(dto.getName());
        discipline.setDescription(dto.getDescription());
        return modelMapper.map(disciplineRepository.save(discipline), DisciplineResponseDto.class);
    }

    @Transactional
    public void deleteDiscipline(Long teacherId, Long disciplineId) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));
        checkTeacherAccess(teacherId, discipline);
        for (User teacher : discipline.getTeachers()) {
            teacher.getDisciplines().remove(discipline);
        }
        discipline.getTeachers().clear();

        disciplineRepository.delete(discipline);
    }

    @Transactional
    public void addGroupToDiscipline(Long teacherId, Long disciplineId, Long groupId)  {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа не найдена"));

        checkTeacherAccess(teacherId, discipline);

        if (discipline.getGroups().contains(group)) {
            throw new ResourceConflictException("Группа уже привязана к дисциплине");
        }

        discipline.getGroups().add(group);
        disciplineRepository.save(discipline);
    }

    @Transactional
    public void removeGroupFromDiscipline(Long teacherId, Long disciplineId, Long groupId) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа не найдена"));

        checkTeacherAccess(teacherId, discipline);

        if (!discipline.getGroups().contains(group)) {
            throw new ResourceConflictException("Группа не привязана к дисциплине");
        }

        discipline.getGroups().remove(group);
        disciplineRepository.save(discipline);
    }

    @Transactional
    public List<GroupResponseDto> getDisciplineGroups(Long disciplineId) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new ResourceNotFoundException("Дисциплина не найдена"));

        return discipline.getGroups().stream()
                .map(group -> modelMapper.map(group, GroupResponseDto.class))
                .toList();
    }

    @Transactional
    public List<DisciplineResponseDto> getAvailableDisciplines(Long userId) {
        User user = userRepository.findUserWithRolesAndGroupById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        List<Discipline> disciplines = new ArrayList<>();
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        if (roleNames.contains("ROLE_ADMIN")) {
            disciplines.addAll(disciplineRepository.findByTeachersId(userId));
        }
        if (roleNames.contains("ROLE_USER") && user.getGroup() != null) {
            disciplines.addAll(
                    disciplineRepository.findByGroupsId(user.getGroup().getId())
            );
        }
        return disciplines.stream()
                .distinct()
                .map(this::mapToDto)
                .toList();
    }

    private void checkTeacherAccess(Long teacherId, Discipline discipline) {
        if (!discipline.getTeachers().stream()
                .anyMatch(t -> t.getId().equals(teacherId))) {
            throw new ru.d3m4k.lms.service.exception.AccessDeniedException(
                    "Преподаватель ID %d не имеет доступа к дисциплине ID %d"
                            .formatted(teacherId, discipline.getId())
            );
        }
    }

    private DisciplineResponseDto mapToDto(Discipline discipline) {
        DisciplineResponseDto dto = modelMapper.map(discipline, DisciplineResponseDto.class);
        dto.setGroups(discipline.getGroups().stream()
                .map(group -> modelMapper.map(group, GroupResponseDto.class))
                .toList());
        return dto;
    }
}
