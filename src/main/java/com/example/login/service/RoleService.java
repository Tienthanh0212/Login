package com.example.login.service;

import com.example.login.DTO.RoleDTO;
import com.example.login.DTO.SignUpDTO;
import com.example.login.DTO.UserException;
import com.example.login.entity.Role;
import com.example.login.entity.User;
import com.example.login.internationalize.MyLocaleResolver;
import com.example.login.repository.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

public interface RoleService {

    ResponseEntity<?> create(RoleDTO roleDTO, HttpServletRequest request);
}
@Service
class RoleServiceImpl implements RoleService{


    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private MyLocaleResolver myLocaleResolver;

    @Autowired
    private MessageSource messageSource;
    @Override
    public ResponseEntity<?> create(RoleDTO roleDTO, HttpServletRequest request)  {
        Locale userLocale = myLocaleResolver.resolveLocale(request);

        String customErrorMessage = validateRoleName(roleDTO, userLocale);
        if (customErrorMessage != null) {
            return ResponseEntity.status(400)
                    .body(new UserException(400, "", customErrorMessage));
        }
        Role role = new ModelMapper().map(roleDTO,Role.class);
        roleRepository.save(role);
        String successMessage = messageSource.getMessage("role.success", null, userLocale);
        return ResponseEntity.status(200)
                .body(new UserException(200, "ROLE_CREATED", successMessage));
    }

    private String validateRoleName(RoleDTO roleDTO, Locale userLocale) {
        if (roleRepository.findByName(roleDTO.getName()) != null) {
            return messageSource.getMessage("register.error.role.exists", null, userLocale);
        }
        return null;
    }

}
