package ru.d3m4k.lms.service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.d3m4k.lms.service.dto.GroupRequestDto;
import ru.d3m4k.lms.service.dto.StudentsFromGroupResponseDto;
import ru.d3m4k.lms.service.dto.UserDto;
import ru.d3m4k.lms.service.entity.Group;
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

        return mapper;
    }
}