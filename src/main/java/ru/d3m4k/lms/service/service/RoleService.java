package ru.d3m4k.lms.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.d3m4k.lms.service.entity.Role;
import ru.d3m4k.lms.service.repository.RoleRepository;


@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}
