package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.LoginHistoryResponseDTO;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.LoginHistoryMapper;
import br.com.brevus.commerce_api.model.LoginHistory;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.LoginHistoryRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.security.CustomUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;
    private final UserRepository userRepository;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository, LoginHistoryMapper loginHistoryMapper, UserRepository userRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.loginHistoryMapper = loginHistoryMapper;
        this.userRepository = userRepository;
    }

    public void save(CustomUserDetails user, String token) {
        LoginHistory history = new LoginHistory();
        history.setUserId(user.getId());
        history.setEmail(user.getEmail());
        history.setAccessToken(token);
        loginHistoryRepository.save(history);
    }

    public List<LoginHistoryResponseDTO> listAllLogins(){
        List<LoginHistory> list = loginHistoryRepository.findAll();
        return loginHistoryMapper.toDtoList(list);
    }

    public List<LoginHistoryResponseDTO> listAllByUserId(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        List<LoginHistory> list = loginHistoryRepository.findByUserId(user.getId());
        return loginHistoryMapper.toDtoList(list);

    }
}
