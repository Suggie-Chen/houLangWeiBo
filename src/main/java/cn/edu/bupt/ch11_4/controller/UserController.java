package cn.edu.bupt.ch11_4.controller;

import cn.edu.bupt.ch11_4.dao.MessageRepository;
import cn.edu.bupt.ch11_4.dao.SysRoleRepository;
import cn.edu.bupt.ch11_4.dao.SysUserRepository;
import cn.edu.bupt.ch11_4.entity.Message;
import cn.edu.bupt.ch11_4.entity.SysRole;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private MessageRepository messageRepository;

    @Autowired(required = false)
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;

    @GetMapping("/home")
    String home()
    {
        return "user/home";
    }
    @PostMapping("/home")
    void get_message(@RequestParam(value = "content") String content,
                     @RequestParam(value = "time") Date time,
                     @RequestParam(value = "id") Long id,
                     @RequestParam(value = "thumb_up") Integer thumb_up) {
//        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        //获取当前用户id
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userid = uid.getId();

        System.out.println("id:");
        System.out.println(userid);
        System.out.println(id);
        System.out.println(time);
        System.out.println(content);
        System.out.println(thumb_up);

        Message m = new Message();
        m.setContent(content);
        m.setTime(time);
        m.setId(id);
        m.setUserid(userid);
        m.setThumb_up(thumb_up);
        messageRepository.save(m);

    }

    @GetMapping("/login/**")
    String login(Model model){
        return "login";
    }


    @GetMapping("/register")
    String register(){
        return "user/register";
    }

    @PostMapping("/register")
    String registerDone(Model model, String username, String password){
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if(sysUser != null){
            model.addAttribute("msg","用户名已存在！");
            System.out.println("注册失败处理=============用户名已存在");
            return "user/register";
        }else {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            sysUser = new SysUser();
            sysUser.setUsername(username);
            sysUser.setPassword(passwordEncoder.encode(password));
//            if(username.equals("admin")){
            SysRole sysRole = new SysRole();
            sysRole.setType("ROLE_USER");
            sysRoleRepository.save(sysRole);
            List<SysRole> roles = new ArrayList<SysRole>();
            roles.add(sysRole);
            sysUser.setRoles(roles);
//            }
            sysUserRepository.save(sysUser);
            System.out.println("注册成功处理=============");
            return "redirect:/user/login";

        }
    }

    @GetMapping("/personal")
    String personal()
    {
        return "user/personal";
    }




}