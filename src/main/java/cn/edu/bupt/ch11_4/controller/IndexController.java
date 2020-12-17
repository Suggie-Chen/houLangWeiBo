package cn.edu.bupt.ch11_4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    String index(){
        return "index";
    }

    @GetMapping("/login")
    String login(){
        return "login";
    }

//    @GetMapping("/admin")
//    String admin(){
//        return "admin";
//    }
}
