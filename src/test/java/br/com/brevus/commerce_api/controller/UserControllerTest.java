package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.*;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.security.JwtAuthenticationFilter;
import br.com.brevus.commerce_api.service.AuthService;
import br.com.brevus.commerce_api.service.JwtService;
import br.com.brevus.commerce_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    @Test
    @DisplayName("Deve registrar usuário com sucesso e retornar 201")
    void deveRegistrarUsuarioComSucesso() throws Exception {
        UserRequestDTO dto = new UserRequestDTO(
                UUID.randomUUID(),
                "Gabriel Cabral",
                "gabriel@brevus.com",
                "51117652041",
                "Gabriel@123",
                "83988887777",
                LocalDate.of(1995, 5, 20)
        );

        mvc.perform(post("/api/users/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(authService).registerUser(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 quando a senha não segue o padrão da Regex")
    void deveFalharSeSenhaForFraca() throws Exception {
        UserRequestDTO dtoSenhaInvalida = new UserRequestDTO(
                UUID.randomUUID(),
                "Gabriel",
                "gabriel@brevus.com",
                "51117652041",
                "Senha@@@123",
                "83988887777",
                LocalDate.of(1995, 5, 20)
        );

        mvc.perform(post("/api/users/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoSenhaInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 quando a data de nascimento for no futuro")
    void deveFalharSeDataNascimentoNoFuturo() throws Exception {
        UserRequestDTO dtoDataInvalida = new UserRequestDTO(
                UUID.randomUUID(),
                "Gabriel",
                "gabriel@brevus.com",
                "51117652041",
                "Gabriel@123",
                "83988887777",
                LocalDate.now().plusDays(1)
        );

        mvc.perform(post("/api/users/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoDataInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve realizar login e retornar token com status 200")
    void deveFazerLoginComSucesso() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("gabriel@teste.com", "Senha@123");
        JwtResponse response = new JwtResponse("token-fake", "refresh-fake");

        when(authService.login(any())).thenReturn(response);

        mvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token-fake"));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID e retornar 200")
    void deveBuscarUsuarioPorId() throws Exception {
        UUID id = UUID.randomUUID();
        UsersResponseDTO response = new UsersResponseDTO(id, "Gabriel", "gabriel@teste.com", "51117652041", "CLIENT", null, null);

        when(userService.getUserById(id)).thenReturn(response);

        mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gabriel"));
    }

    @Test
    @DisplayName("Deve deletar usuário e retornar 204")
    void deveDeletarUsuario() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(id);
    }

    // --- CENÁRIOS DE ERRO ---

    @Test
    @DisplayName("Deve retornar 400 quando o DTO de registro for inválido (Bean Validation)")
    void deveFalharNoRegistroComDadosInvalidos() throws Exception {
        UserRequestDTO dtoInvalido = new UserRequestDTO(
                UUID.randomUUID(),
                "G",
                "email-invalido",
                "51117652041",
                "",
                "123",
                LocalDate.now().plusDays(1)
        );

        mvc.perform(post("/api/users/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("Deve retornar 404 quando buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        when(userService.getUserById(idInexistente))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        mvc.perform(get("/api/users/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar atualizar senha com dados divergentes")
    void deveRetornar400AoAtualizarSenhaInvalida() throws Exception {
        UUID id = UUID.randomUUID();
        PasswordRequestDTO dto = new PasswordRequestDTO("senhaAtual", "nova123", "divergente123");

        doThrow(new RuntimeException("BusinessException"))
                .when(userService).updatePassword(eq(id), any());

        mvc.perform(patch("/api/users/password/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar usuários recentes paginados")
    void deveRetornarUsuariosRecentesPaginados() throws Exception {
        Page<UsersResponseDTO> page = new PageImpl<>(List.of(
                new UsersResponseDTO(UUID.randomUUID(), "Gabriel", "gabriel@teste.com", "51117652041", "ADMIN", null, null)
        ));

        when(userService.getRecentUsers(anyInt(), anyInt(), anyString())).thenReturn(page);

        mvc.perform(get("/api/users/recent-users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("search", "Gabriel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Gabriel"));
    }
}