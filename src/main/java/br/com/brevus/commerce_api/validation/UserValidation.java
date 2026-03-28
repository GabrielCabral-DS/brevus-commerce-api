package br.com.brevus.commerce_api.validation;

import br.com.brevus.commerce_api.exceptions.DuplicateRecordException;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidation {

    private final UserRepository userRepository;


    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(User user){

        if (existsByEmail(user)){
            throw new DuplicateRecordException("Já existe um usuário cadastrado com esse email");
        }

        if (existsByCpf(user)){
            throw new DuplicateRecordException("Já existe um usuário cadastrado com esse cpf");
        }
    }

    public boolean existsByEmail(User user){
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if (user.getId() == null){
            return userOptional.isPresent();
        }

        return userOptional.isPresent() && !user.getId().equals(userOptional.get().getId());
    }

    public boolean existsByCpf(User user){
        Optional<User> userOptional = userRepository.findByCpf(user.getCpf());

        if (user.getId() == null){
            return userOptional.isPresent();
        }

        return userOptional.isPresent() && !user.getId().equals(userOptional.get().getId());
    }
}
