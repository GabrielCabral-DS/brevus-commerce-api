package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public User register(UserRequestDTO dto){
        User user = userMapper.toEntity(dto);
        return userRepository.save(user);
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

}
