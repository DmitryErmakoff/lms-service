package ru.d3m4k.lms.service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.d3m4k.lms.service.dto.MaterialResponse;
import ru.d3m4k.lms.service.dto.UpdateMaterialRequest;
import ru.d3m4k.lms.service.entity.File;
import ru.d3m4k.lms.service.entity.Material;
import ru.d3m4k.lms.service.exception.ResourceNotFoundException;
import ru.d3m4k.lms.service.repository.MaterialRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final FileService fileService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public MaterialResponse createMaterial(Material material) {
        Material savedMaterial = materialRepository.save(material);
        return modelMapper.map(savedMaterial, MaterialResponse.class);
    }

    public List<MaterialResponse> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(material -> modelMapper.map(material, MaterialResponse.class))
                .collect(Collectors.toList());
    }

    public MaterialResponse getMaterialById(Long id) {
        Material material = getMaterialEntityById(id);
        return modelMapper.map(material, MaterialResponse.class);
    }

    public Material getMaterialEntityById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));
    }

    public MaterialResponse updateMaterial(Long id, UpdateMaterialRequest request) {
        Material material = getMaterialEntityById(id);

        if (request.getTitle() != null) {
            material.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            material.setDescription(request.getDescription());
        }
        if (request.getFileId() != null) {
            File file = fileService.getFile(request.getFileId());
            material.setFile(file);
        } else if (material.getFile() != null) {
            // Если хотим удалить файл из материала
            material.setFile(null);
        }

        Material updatedMaterial = materialRepository.save(material);
        return modelMapper.map(updatedMaterial, MaterialResponse.class);
    }

    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }
}
