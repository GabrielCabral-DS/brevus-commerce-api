package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.PasswordRequestDTO;
import br.com.brevus.commerce_api.dto.RecoverPasswordEmailRequestDTO;
import br.com.brevus.commerce_api.dto.UsersResponseDTO;
import br.com.brevus.commerce_api.exceptions.BusinessException;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.validation.UserValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private JwtService jwtService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserValidation userValidation;

    private User user;
    private UUID userId;

    @BeforeEach
    public void setup() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("gabriel@brevus.com");
        user.setPassword("encoded_password");
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    public void deveBuscarUsuarioPorIdComSucesso() {
        LocalDate dataNasc = LocalDate.of(1995, 5, 20);
        LocalDate dataReg = LocalDate.now();
        UsersResponseDTO mockDto = new UsersResponseDTO(userId, "Gabriel", "gabriel@brevus.com", "00000000000", "8399999", dataNasc, dataReg);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(mockDto);

        UsersResponseDTO result = userService.getUserById(userId);

        Assertions.assertNotNull(result);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando usuário não existir")
    public void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso quando dados forem válidos")
    public void deveAtualizarSenhaComSucesso() {

        PasswordRequestDTO dto = new PasswordRequestDTO(
                "Senha@antiga",
                "novaSenha@123",
                "novaSenha@123"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("Senha@antiga", user.getPassword()))
                .thenReturn(true);

        when(passwordEncoder.matches("novaSenha@123", user.getPassword()))
                .thenReturn(false);

        when(passwordEncoder.encode("novaSenha@123"))
                .thenReturn("new_encoded");

        userService.updatePassword(userId, dto);

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando senhas forem divergentes")
    public void deveFalharQuandoSenhasDivergentes() {
        PasswordRequestDTO dto = new PasswordRequestDTO("senhaAntiga", "novaSenha", "outraSenha");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThrows(BusinessException.class, () -> userService.updatePassword(userId, dto));
    }

    @Test
    @DisplayName("Deve lançar BadCredentialsException quando token de recuperação for inválido")
    public void deveFalharRecuperacaoComTokenInvalido() {
        RecoverPasswordEmailRequestDTO dto = new RecoverPasswordEmailRequestDTO("token_fake", "123", "123");
        when(jwtService.isTokenValid("token_fake")).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> userService.passwordRecover(dto));
    }

    @Test
    @DisplayName("Deve lançar BadCredentialsException quando tipo do token não for RECOVER_PASSWORD")
    public void deveFalharSeTipoDoTokenForErrado() {
        RecoverPasswordEmailRequestDTO dto = new RecoverPasswordEmailRequestDTO("token_valido", "123", "123");
        when(jwtService.isTokenValid("token_valido")).thenReturn(true);
        when(jwtService.getClaim("token_valido", "type")).thenReturn("LOGIN_TOKEN");

        assertThrows(BadCredentialsException.class, () -> userService.passwordRecover(dto));
    }

    @Test
    @DisplayName("Deve retornar página de usuários recentes corretamente")
    public void deveRetornarUsuariosRecentesPaginados() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dateRegistered"));
        when(userRepository.findByUserRoles_Role_Name(anyString(), any())).thenReturn(Page.empty());

        Page<UsersResponseDTO> result = userService.getRecentUsers(0, 10, null);
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha atual informada está incorreta")
    public void deveFalharQuandoSenhaAtualNaoConfere() {
        PasswordRequestDTO dto = new PasswordRequestDTO("senhaErrada", "nova123", "nova123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.updatePassword(userId, dto));
        verify(userRepository, never()).save(any());
    }
}