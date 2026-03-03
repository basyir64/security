package com.basyir.security.core;

import com.basyir.security.dtos.LoginDto;
import com.basyir.security.dtos.SignUpRequestDto;
import com.basyir.security.dtos.UserDto;
import com.basyir.security.entities.Role;
import com.basyir.security.entities.User;
import com.basyir.security.exceptions.ResourceNotFoundException;
import com.basyir.security.exceptions.UserAlreadyExistsException;
import com.basyir.security.repositories.RoleRepository;
import com.basyir.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWT_Service jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public UserDto signUp(SignUpRequestDto signUpRequestDto) {

        User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);

        if (user != null) {
            throw new UserAlreadyExistsException("User is already present with same email id");
        }

        User newUser = modelMapper.map(signUpRequestDto, User.class);

        Set<Role> roles = new HashSet<>();
        for (String roleStr : signUpRequestDto.getRoles()) {
            Role role = roleRepository.findByName(roleStr).orElseThrow(() -> new ResourceNotFoundException("Role does not exist: " + roleStr));
            roles.add(role);
        }

        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser = userRepository.save(newUser);

        return modelMapper.map(newUser, UserDto.class);
    }

    public String[] login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

        User user = (User) authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken) {
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }
}
