package br.com.brevus.commerce_api.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebController {


    @GetMapping("/login")
    public String loginPage() {
        return "redirect:/api/web/login";
    }

    @GetMapping("/api/web/profile")
    public String profileManager() {
        return "profile";
    }

    @GetMapping("/api/web/profile-client")
    public String profileClient() {
        return "profile-client";
    }

    @GetMapping("/api/web/my-cart")
    public String myCart() {
        return "my-cart";
    }

    @GetMapping("/api/web/my-orders")
    public String myOrders() {
        return "my-orders";
    }

    @GetMapping("/api/web/login")
    public String loginUserPage() {
        return "login";
    }

    @GetMapping("/api/web/products-client")
    public String productsUserClient() {
        return "products-client";
    }

    @GetMapping("/api/web/products")
    public String products() {
        return "products";
    }

    @GetMapping("/api/web/clients")
    public String clients() {
        return "clients";
    }

    @GetMapping("api/web/email-password")
    public String recoverPassword() {
        return "email-password";
    }

    @GetMapping("api/web/reset-password")
    public String resetPasswrod() {
        return "reset-password";
    }

    @GetMapping("/api/web/oauth2-callback")
    public String oauth2CallbackPage() {
        return "oauth2-callback";
    }

    @GetMapping("/api/web/home-user")
    public String homeUserPage() {
        return "home-user";
    }

    @GetMapping("/api/web/home-manager")
    public String homeManagerPage() {
        return "home-manager";
    }

    @GetMapping("/api/web/register-manager")
    public String registerManagerPage() {
        return "register-manager";
    }

    @GetMapping("/api/web/register-user")
    public String registerUserPage() {
        return "register-user";
    }

    @GetMapping("/api/web/payment")
    public String paymentPage() {
        return "payment";
    }

    @GetMapping("/api/web/pix")
    public String pixPage() {
        return "pix";
    }

    @GetMapping("/api/web/sales")
    public String salesPage() {
        return "sales";
    }
}
