package br.com.brevus.commerce_api.controller;


import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDTO dto){
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserRequestDTO>> listAllUsers(){
        List<UserRequestDTO> userRequestDTOList = userService.listAllUsers();
        return ResponseEntity.ok().body(userRequestDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequestDTO> getUsersById(@PathVariable(value = "id") UUID id){
        UserRequestDTO userRequestDTO = userService.getUserById(id);
        return ResponseEntity.ok().body(userRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUsersById(@Valid @PathVariable(value = "id") UUID id, @RequestBody UserRequestDTO dto){
        userService.updateUsers(id,dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable(value = "id") UUID id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
