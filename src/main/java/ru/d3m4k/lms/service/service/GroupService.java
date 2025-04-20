package ru.d3m4k.lms.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.d3m4k.lms.service.dto.GroupRequestDto;
import ru.d3m4k.lms.service.dto.GroupResponseDto;
import ru.d3m4k.lms.service.dto.StudentsFromGroupResponseDto;
import ru.d3m4k.lms.service.dto.UserDto;
import ru.d3m4k.lms.service.entity.Group;
import ru.d3m4k.lms.service.entity.User;
import ru.d3m4k.lms.service.exception.ResourceConflictException;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.repository.GroupRepository;
import ru.d3m4k.lms.service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public GroupResponseDto createGroup(GroupRequestDto dto) {
        groupRepository.findByName(dto.getName())
                .ifPresent(group -> {
                    throw new ResourceConflictException(
                            "Группа с названием '%s' уже существует (ID: %d)"
                                    .formatted(dto.getName(), group.getId())
                    );
                });
        log.info(dto.toString());

        Group group = new Group();
        group.setName(dto.getName());
        group.setCreatedAt(LocalDateTime.now());

        return modelMapper.map(groupRepository.save(group), GroupResponseDto.class);
    }

    public List<UserDto> getGroupStudents(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа не найдена"));

        return userRepository.findByGroupId(group.getId()).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public List<GroupResponseDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(group -> GroupResponseDto.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .createdAt(group.getCreatedAt())
                        .build())
                .toList();
    }

    @Transactional
    public StudentsFromGroupResponseDto getStudentsFromGroup(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Group group = user.getGroup();
        if (isNull(group)) {
            throw new ResourceConflictException(
                    "Пользователь с ID %d не состоит ни в одной из групп"
                            .formatted(user.getId())
            );
        }
        Group fullGroup = groupRepository.findByIdWithUsers(group.getId())
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        return modelMapper.map(fullGroup, StudentsFromGroupResponseDto.class);
    }

    @Transactional
    public UserDto addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Группа не найдена"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        if(user.getGroup() != null && user.getGroup().getId().equals(groupId)) {
            throw new ResourceConflictException("Пользователь уже состоит в группе");
        }

        user.setGroup(group);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Transactional
    public void removeUserFromGroup(Long groupId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(user.getGroup() == null || !user.getGroup().getId().equals(groupId)) {
            throw new ResourceConflictException("User not member of this group");
        }

        user.setGroup(null);
        userRepository.save(user);
    }

    public List<UserDto> getUsersWithoutGroup() {
        return userRepository.findUsersWithoutGroup().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(   Collectors.toList());
    }
}
