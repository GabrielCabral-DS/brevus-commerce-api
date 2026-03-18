package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.JwtResponse;
import br.com.brevus.commerce_api.dto.LoginRequestDTO;
import br.com.brevus.commerce_api.dto.RefreshTokenRequestDTO;
import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.model.RefreshToken;
import br.com.brevus.commerce_api.model.Role;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.model.UserRole;
import br.com.brevus.commerce_api.repository.RoleRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.repository.UserRoleRepository;
import br.com.brevus.commerce_api.security.CustomUserDetails;
import br.com.brevus.commerce_api.validation.UserValidation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final LoginHistoryService loginHistoryService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final EmailService emailService;
    private final UserValidation userValidation;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, LoginHistoryService loginHistoryService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRoleRepository userRoleRepository, EmailService emailService, UserValidation userValidation) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.loginHistoryService = loginHistoryService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.emailService = emailService;
        this.userValidation = userValidation;
    }


    public JwtResponse login(LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.email(), request.password()));

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken =
                refreshTokenService.create(user.getId());

        loginHistoryService.save(user, accessToken);

        return new JwtResponse(accessToken, refreshToken.getToken());
    }

    public void registerUser(UserRequestDTO request) {

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setDateBirth(request.dateBirth());
        user.setPhone(request.phone());

        user.setPassword(passwordEncoder.encode(request.password()));
        userValidation.validate(user);
        userRepository.save(user);

        Role role = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
    }

    public JwtResponse refreshToken(RefreshTokenRequestDTO request) {

        RefreshToken refreshToken =
                refreshTokenService.validate(request.refreshToken());

        User user = userRepository.findById(refreshToken.getUserId()).orElse(null);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String newAccessToken = jwtService.generateToken(userDetails);

        return new JwtResponse(
                newAccessToken,
                refreshToken.getToken()
        );
    }

    public void sendRecoveryEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email informado"));

        String recoveryToken = jwtService.generateRecoveryToken(user);

        String recoveryLink = "http://localhost:8080/api/web/reset-password?token=" + recoveryToken;

        emailService.sendRecoverPassword(
                user.getEmail(),
                "Recuperar Senha",
                recoveryLink,
                user.getName()
        );
    }


}
