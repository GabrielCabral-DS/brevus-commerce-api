package br.com.brevus.commerce_api.security;

import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.repository.UserRoleRepository;
import br.com.brevus.commerce_api.service.JwtTokenService;
import br.com.brevus.commerce_api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();
        String email = oauth2User.getAttribute("email");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email não fornecido pelo provedor OAuth2.");
            return;
        }

        userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setName(oauth2User.getAttribute("name"));
                    user.setEmail(email);
                    user.setPhone("(11) 98765-4321");
                    user.setPassword(UUID.randomUUID().toString());

                    UserRequestDTO dto = userMapper.toDTO(user);

                    return userService.registerUser(dto);
                });

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        String userRole = String.valueOf(userRoleRepository.findRoleNamesByUserId(user.getId()));

        String token = jwtTokenService.generateTokenLogin(user.getId(),user.getName(),userRole,user.getEmail());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\":\"" + token + "\"}");
        response.getWriter().flush();
    }
}
