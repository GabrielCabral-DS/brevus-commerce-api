package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.*;
import br.com.brevus.commerce_api.exceptions.BadCredentialsException;
import br.com.brevus.commerce_api.exceptions.BusinessException;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    public UsersResponseDTO getUserById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserRequestDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("Email not found"));
        return userMapper.toDTO(user);
    }

    public List<UsersResponseDTO> listAllUsers(){
        List<User> userList = userRepository.findAll();
        return userMapper.toDtoList(userList);
    }

    public User updateUsers(UUID id, UserProfileRequestDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setDateBirth(dto.dateBirth());

        return userRepository.save(user);
    }

    public void deleteUserById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.deleteById(user.getId());
    }

    public void passwordRecover(RecoverPasswordEmailRequestDTO dto) {

        if (!jwtService.isTokenValid(dto.token())) {
            throw new RuntimeException("Invalid or expired token");
        }

        String tokenType = (String) jwtService.getClaim(dto.token(), "type");
        if (!"RECOVER_PASSWORD".equals(tokenType)) {
            throw new RuntimeException("This token is not for password recovery");
        }

        String userId = jwtService.getSubject(dto.token());

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!dto.password().equals(dto.passwordConfirmation())) {
            throw new RuntimeException("The passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(dto.password()));
        userRepository.save(user);
    }

    public Page<UsersResponseDTO> getRecentUsers(int page, int size, String search){
        int adjustedPage = Math.max(0, page);

        Pageable pageable = PageRequest.of(
                adjustedPage,
                size,
                Sort.by(Sort.Direction.DESC,"dateRegistered")
        );

        Page<User> userPage;
        if (search != null && !search.trim().isEmpty()) {
            userPage = userRepository.findByRoleAndSearch(
                    "CLIENT", search, pageable);
        } else {
            userPage = userRepository.findByUserRoles_Role_Name("CLIENT", pageable);
        }

        return userPage.map(userMapper::toDto);
    }

    public User updatePassword(UUID uuid, PasswordRequestDTO dto) {

        User userModel = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.currentPassword(), userModel.getPassword())) {
            throw new BadCredentialsException("Senha atual incorreta!");
        }

        if (!Objects.equals(dto.password(), dto.passwordConfirmation())) {
            throw new BusinessException("As senhas estão divergentes!");
        }

        if (passwordEncoder.matches(dto.password(), userModel.getPassword())) {
            throw new BusinessException("A nova senha não pode ser igual à senha atual!");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());
        userModel.setPassword(encodedPassword);

        return userRepository.save(userModel);
    }
}
