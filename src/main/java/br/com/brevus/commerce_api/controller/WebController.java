package br.com.brevus.commerce_api.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {


    @GetMapping("/login")
    public String loginPage(){
        return "redirect:/api/web/login";
    }

    @GetMapping("/api/web/profile")
    public String profileManager(){
        return "profile";
    }

    @GetMapping("/api/web/my-cart")
    public String myCart(){return "my-cart";}

    @GetMapping("/api/web/my-orders")
    public String myOrders(Model model){
        model.addAttribute("orders", List.of());
        return "my-orders";
    }

    @GetMapping("/api/web/login")
    public String loginUserPage(){
        return "login";
    }

    @GetMapping("/api/web/products-client")
    public String productsUserClient(){return "products-client";}

    @GetMapping("/api/web/products")
    public String products(){
        return "products";
    }

    @GetMapping("/api/web/clients")
    public String clients(){
        return "clients";
    }

    @GetMapping("api/web/email-password")
    public String recoverPassword(){
        return "email-password";
    }

    @GetMapping("api/web/reset-password")
    public String resetPasswrod(){
        return "reset-password";
    }

    @GetMapping("/api/web/oauth2-callback")
    public String oauth2CallbackPage(){
        return "oauth2-callback";
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
