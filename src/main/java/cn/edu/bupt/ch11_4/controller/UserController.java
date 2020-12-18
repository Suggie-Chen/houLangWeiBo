package cn.edu.bupt.ch11_4.controller;

import cn.edu.bupt.ch11_4.dao.MessageRepository;
import cn.edu.bupt.ch11_4.dao.SysRoleRepository;
import cn.edu.bupt.ch11_4.dao.SysUserRepository;
import cn.edu.bupt.ch11_4.entity.Message;
import cn.edu.bupt.ch11_4.entity.SysRole;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.http.HttpRequest;
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

    @PostMapping("/home/post")
    void get_message(@RequestParam(value = "content") String content,
                     @RequestParam(value = "time") Date time,
                     @RequestParam(value = "id") Long id,
                     @RequestParam(value = "thumb_up") Integer thumb_up) {
        System.out.println(content);
        System.out.println(time);
        System.out.println(id);
        System.out.println(thumb_up);
//        MessageRepository.save(message);
//        messageRepository.save();
        //start 对象操作  操作原理说明，根据实体构造最终完整sql并执行操作，改操方法api最多有3个参数，第一个entity（Object类型） 第二个wheresql(String主要是where条件),第三个sql语句对应的参数(对象，list，map，数组)
        Message m = new Message();
        m.setContent(content);
        m.setTime(time);
        m.setId(id);
        m.setThumb_up(thumb_up);
        messageRepository.save(m);

    }

}
