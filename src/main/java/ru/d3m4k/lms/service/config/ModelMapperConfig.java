package ru.d3m4k.lms.service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.d3m4k.lms.service.dto.*;
import ru.d3m4k.lms.service.entity.File;
import ru.d3m4k.lms.service.entity.Group;
import ru.d3m4k.lms.service.entity.Material;
import ru.d3m4k.lms.service.entity.User;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        mapper.createTypeMap(GroupRequestDto.class, Group.class)
                .addMappings(m -> m.skip(Group::setId))
                .addMappings(m -> m.map(GroupRequestDto::getName, Group::setName));

        mapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> m.map(User::getLogin, UserDto::setLogin))
                .addMappings(m -> m.map(User::getFirstName, UserDto::setFirstname));


        mapper.createTypeMap(Group.class, StudentsFromGroupResponseDto.class)
                .addMappings(m -> m.map(Group::getUsers, StudentsFromGroupResponseDto::setUsers));

        mapper.createTypeMap(File.class, FileResponse.class)
                .addMappings(m -> m.map(File::getId, FileResponse::setId))
                .addMappings(m -> m.map(File::getFileName, FileResponse::setFileName))
                .addMappings(m -> m.map(src -> src.getUploadedBy().getId(), FileResponse::setUploadedBy))
                .addMappings(m -> m.map(File::getCreatedAt, FileResponse::setCreatedAt));

        mapper.createTypeMap(Material.class, MaterialResponse.class)
                .addMappings(m -> m.map(Material::getId, MaterialResponse::setId))
                .addMappings(m -> m.map(Material::getTitle, MaterialResponse::setTitle))
                .addMappings(m -> m.map(Material::getDescription, MaterialResponse::setDescription))
                .addMappings(m -> m.map(src -> src.getFile().getId(), MaterialResponse::setFileId))
                .addMappings(m -> m.map(src -> src.getTeacher().getId(), MaterialResponse::setTeacherId))
                .addMappings(m -> m.map(Material::getCreatedAt, MaterialResponse::setCreatedAt));

        return mapper;
    }
}