package br.com.brevus.commerce_api.security;

import br.com.brevus.commerce_api.dto.JwtResponse;
import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.model.RefreshToken;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.UserRepository;
import br.com.brevus.commerce_api.service.AuthService;
import br.com.brevus.commerce_api.service.JwtService;
import br.com.brevus.commerce_api.service.LoginHistoryService;
import br.com.brevus.commerce_api.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final LoginHistoryService loginHistoryService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken oauth2Token =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User oauth2User = oauth2Token.getPrincipal();

        String email = oauth2User.getAttribute("email");

        if (email == null) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Email não fornecido pelo provedor OAuth2."
            );
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerSocialUser(oauth2User));

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken =
                refreshTokenService.create(user.getId());

        loginHistoryService.save(userDetails, accessToken);

        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken.getToken());

        // Retornar HTML com JavaScript que armazena os tokens e redireciona
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <title>Autenticando...</title>" +
                "    <script src='/js/auth-utils.js'></script>" +
                "</head>" +
                "<body>" +
                "    <script>" +
                "        const tokens = " + objectMapper.writeValueAsString(jwtResponse) + ";" +
                "        AuthUtils.setTokens(tokens.accessToken, tokens.refreshTokenToken);" +
                "        AuthUtils.redirectByRole();" +
                "    </script>" +
                "</body>" +
                "</html>";

        response.getWriter().write(html);
        response.getWriter().flush();
    }

    private User registerSocialUser(OAuth2User oauth2User) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        UserRequestDTO dto = new UserRequestDTO(
                UUID.randomUUID(),
                oauth2User.getAttribute("name"),
                oauth2User.getAttribute("email"),
                UUID.randomUUID().toString(),
                "(00) 00000-0000",
                LocalDate.parse("22/02/1990", formatter),
                LocalDate.parse("20/02/2026", formatter)

        );

        authService.registerUser(dto);

        return userRepository.findByEmail(dto.email())
                .orElseThrow();
    }
}



