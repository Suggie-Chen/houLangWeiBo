package cn.edu.bupt.ch11_4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("/")
    String index(){
        return "login";
    }

    @GetMapping("/forget")
    @ResponseBody
    String forget(){
        return "forgetPwd";
    }

    @GetMapping("/login")
    String login(){
        return "login";
    }

    @GetMapping("/loginfailure")
    String failure(Model model) {
        System.out.println("失败！！！！！！");
        return "loginfailure";
    }

//    @PostMapping("/loginfailure")
//    String fail(Model model) {
//        System.out.println("失败！！！！！！");
//        return "loginfailure";
//    }

//    @GetMapping("/admin")
//    String admin(){
//        return "admin";
//    }
}
