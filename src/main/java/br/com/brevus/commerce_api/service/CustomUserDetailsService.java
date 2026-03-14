package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.UserRequestDTO;
import br.com.brevus.commerce_api.mapper.UserMapper;
import br.com.brevus.commerce_api.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    public CustomUserDetailsService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserRequestDTO user = userService.getUserByEmail(email);

        if (user == null) {
            throw  new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userMapper.toEntity(user));
    }
}
