package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.*;
import br.com.brevus.commerce_api.service.AuthService;
import br.com.brevus.commerce_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
        authService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login para obter o token JWT.")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponse response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "RefreshToken", description = "RefreshToken para obter o token para continuar navegando")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        JwtResponse response = authService.refreshToken(refreshTokenRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Recover Password", description = "generate a new jwt token")
    public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO dto){
        authService.sendRecoveryEmail(dto.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordEmailRequestDTO dto){
        userService.passwordRecover(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> listAllUsers(){
        List<UsersResponseDTO> usersResponseDTOList = userService.listAllUsers();
        return ResponseEntity.ok().body(usersResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getUsersById(@PathVariable(value = "id") UUID id){
        UsersResponseDTO usersResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(usersResponseDTO);
    }

    @GetMapping("/recent-users")
    public ResponseEntity<Page<UsersResponseDTO>> getRecentUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search){
        Page<UsersResponseDTO> usersResponseDTOList = userService.getRecentUsers(page, size, search);
        return ResponseEntity.ok().body(usersResponseDTOList);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<Void> updateUsersById(@PathVariable(value = "id") UUID id, @Valid @RequestBody UserProfileRequestDTO dto){
        userService.updateUsers(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable(value = "id") UUID id, @Valid @RequestBody PasswordRequestDTO dto){
        userService.updatePassword(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable(value = "id") UUID id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
