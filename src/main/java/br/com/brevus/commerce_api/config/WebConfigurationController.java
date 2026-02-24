package br.com.brevus.commerce_api.config;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebConfigurationController {

    @GetMapping("/")
    public String redirectToLogin(){
        return "redirect:/api/web/login";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "redirect:/api/web/login";
    }

    @GetMapping("/api/web/login")
    public String loginUserPage(){
        return "login";
    }

    @GetMapping("/api/web/home-user")
    public String homeUserPage(){
        return "home-user";
    }

    @GetMapping("/api/web/home-manager")
    public String homeManagerPage(){
        return "home-manager";
    }

    @GetMapping("/api/web/register-manager")
    public String registerManagerPage(){
        return "register-manager";
    }

    @GetMapping("/api/web/register-user")
    public String registerUserPage(){
        return "register-user";
    }

}
