package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.model.LoginHistory;
import br.com.brevus.commerce_api.repository.LoginHistoryRepository;
import br.com.brevus.commerce_api.security.CustomUserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    public void save(CustomUserDetails user, String token) {
        LoginHistory history = new LoginHistory();
        history.setUserId(user.getId());
        history.setEmail(user.getEmail());
        history.setAccessToken(token);
        loginHistoryRepository.save(history);
    }
}
