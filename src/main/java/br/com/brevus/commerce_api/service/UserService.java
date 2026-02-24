package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.model.Role;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.model.UserRole;
import br.com.brevus.commerce_api.repository.RoleRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtService;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenService jwtService, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new UsernameNotFoundException("User not found by id: " + uuid));

        List<String> permissions = userRoleRepository.findRoleNamesByUserId(user.getId());
        List<GrantedAuthority> authorities = permissions.stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }


    public User registerUser(UserRequestDTO dto){
        User user = userMapper.toEntity(dto);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        Role role = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);

        return user;

    }

    public User registerUserManager(UserRequestDTO dto){
        User user = userMapper.toEntity(dto);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        Role role = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);

        return user;

    }

    public UserRequestDTO getUserById(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
        return userMapper.toDTO(user);
    }

    public List<UserRequestDTO> listAllUsers(){
        List<User> userRequestDTOS = userRepository.findAll();
        return userMapper.toDtoList(userRequestDTOS);
    }

    public User updateUsers(UUID id, UserRequestDTO dto){
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


    @Transactional
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email is invalid"));
        String userRole = String.valueOf(userRoleRepository.findRoleNamesByUserId(user.getId()));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateTokenLogin(user.getId(), user.getName(),userRole,user.getEmail());
        } else {
            throw new RuntimeException("Password is invalid");
        }
    }

}
