package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.*;
import br.com.brevus.commerce_api.service.AuthService;
import br.com.brevus.commerce_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
@Tag(name = "Users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register-user")
    @Operation(summary = "Register", description = "Register a new user")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
        authService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Generate a new jwt token")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        JwtResponse response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "Generate a new jwt token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        JwtResponse response = authService.refreshToken(refreshTokenRequestDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Recover Password", description = "Send recovery email to user")
    public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO dto){
        authService.sendRecoveryEmail(dto.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recover-password")
    @Operation(summary = "Recover Password", description = "Recover user password using recovery token")
    public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordEmailRequestDTO dto){
        userService.passwordRecover(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List Users", description = "List all users")
    public ResponseEntity<List<UsersResponseDTO>> listAllUsers(){
        List<UsersResponseDTO> usersResponseDTOList = userService.listAllUsers();
        return ResponseEntity.ok().body(usersResponseDTOList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'CLIENT')")
    @Operation(summary = "Get User", description = "Get user by id")
    public ResponseEntity<UsersResponseDTO> getUsersById(@PathVariable(value = "id") UUID id){
        UsersResponseDTO usersResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(usersResponseDTO);
    }

    @GetMapping("/recent-users")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @Operation(summary = "Recent Users", description = "List recent users with pagination and optional search")
    public ResponseEntity<Page<UsersResponseDTO>> getRecentUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search){
        Page<UsersResponseDTO> usersResponseDTOList = userService.getRecentUsers(page, size, search);
        return ResponseEntity.ok().body(usersResponseDTOList);
    }

    @PutMapping("/profile/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'CLIENT')")
    @Operation(summary = "Update User", description = "Update user profile by id")
    public ResponseEntity<Void> updateUsersById(@PathVariable(value = "id") UUID id, @Valid @RequestBody UserProfileRequestDTO dto){
        userService.updateUsers(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/password/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'CLIENT')")
    @Operation(summary = "Update Password", description = "Update user password by id")
    public ResponseEntity<Void> updatePassword(@PathVariable(value = "id") UUID id, @Valid @RequestBody PasswordRequestDTO dto){
        userService.updatePassword(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete User", description = "Delete user by id")
    public ResponseEntity<Void> deleteUserById(@PathVariable(value = "id") UUID id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
