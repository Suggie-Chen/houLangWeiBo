package cn.edu.bupt.ch11_4.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tourist")
public class TouristController {
    @GetMapping("/")
    String tour(){
        return "tourist/tindex";
    }
}
