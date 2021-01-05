package cn.edu.bupt.ch11_4.controller;

import cn.edu.bupt.ch11_4.dao.FanRepository;
import cn.edu.bupt.ch11_4.dao.CommentRepository;
import cn.edu.bupt.ch11_4.dao.MessageRepository;
import cn.edu.bupt.ch11_4.dao.SysRoleRepository;
import cn.edu.bupt.ch11_4.dao.SysUserRepository;
import cn.edu.bupt.ch11_4.entity.Fan;
import cn.edu.bupt.ch11_4.entity.Comment;
import cn.edu.bupt.ch11_4.entity.Message;
import cn.edu.bupt.ch11_4.entity.SysRole;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private MessageRepository messageRepository;
    private CommentRepository commentRepository;
    private FanRepository fanRepository;

    @Autowired(required = false)
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Autowired(required = false)
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;

    @GetMapping("/home")
    String home(Model model,
                @RequestParam(value = "start",defaultValue = "0") Integer start,
                @RequestParam(value = "limit",defaultValue = "9") Integer limit,
                @RequestParam(value = "sort_method",defaultValue = "time") String sort_method)
    {
        //得到当前用户名
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uname = uid.getUsername();

        start = start <0 ? 0 :start;
        Sort sort = Sort.by(Sort.Direction.DESC,sort_method);
        Pageable pageable = PageRequest.of(start,limit,sort);
        Page<Message> messages = messageRepository.findAll(pageable);
        model.addAttribute("messages",messages);
        model.addAttribute("uname", uname);
        return "user/home";
    }
    @PostMapping("/home")
    void get_message(@RequestParam(value = "content") String content,
                     @RequestParam(value = "time") Date time,
                     @RequestParam(value = "thumbUp") Integer thumbUp,
                     @RequestParam(value = "cmtNum") Integer cmtNum,
                     @RequestParam(value = "picUrl") String picUrl) {

        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userid = uid.getId();
        String uname = uid.getUsername();

        System.out.println(picUrl);
        Message m = new Message();
        m.setContent(content);
        m.setTime(time);
        m.setUserid(userid);
        m.setThumbUp(thumbUp);
        m.setName(uname);
        m.setCmtNum(cmtNum);

        if(picUrl.equals("#"))
        {
            m.setPicture(null);
        }
        else {
            m.setPicture(picUrl.getBytes());
        }

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
            SysRole sysRole = new SysRole();
            sysRole.setType("ROLE_USER");
            sysRoleRepository.save(sysRole);
            List<SysRole> roles = new ArrayList<SysRole>();
            roles.add(sysRole);
            sysUser.setRoles(roles);
            sysUserRepository.save(sysUser);
            System.out.println("注册成功处理=============");
            return "redirect:/user/login";
        }
    }

    @ResponseBody
    @GetMapping("/zan")
    public String thumbUp(@RequestParam("msgid")String msgid) {
        Long id = Long.parseLong(msgid);
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            Integer num = message.getThumbUp();
            num=num+1;
            String ns = num.toString();
            message.setThumbUp(num);
            messageRepository.save(message);
            return ns;
        } else {
            return "failure";
        }
    }

    @PostMapping("/comment")
    public String comments(@RequestParam("msgId")String msgId,@RequestParam("content")String content,Model model){

        Long msgid = Long.parseLong(msgId);
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userid = uid.getId();
        String name=uid.getUsername();
        Date time=new Date();
//        System.out.println(msgId);
//        System.out.println(content);
//        System.out.println(userid);
//        System.out.println(time);
        Comment c = new Comment();
        Optional<Message> messageOptional=messageRepository.findById(msgid);
        if(messageOptional.isPresent()) {
            c.setMessage(messageOptional.get());
            Message messagetemp= messageOptional.get();
            messagetemp.setCmtNum(messagetemp.getCmtNum()+1);
            messageRepository.save(messagetemp);
        }
        c.setMsgId(msgid);
        c.setContent(content);
        c.setTime(time);
        c.setPublisherName(name);
        c.setPublisherId(userid);
        commentRepository.save(c);//将评论存入数据库
        List<Comment> comments=commentRepository.findByMsgId(msgid);

        model.addAttribute("comments", comments);
        return "redirect:/user/home";
    }


    @GetMapping("/comment")
    public Object comment(@RequestParam("msgId")String msgId,Model model){
        System.out.println(msgId);
        Long id = Long.parseLong(msgId);

        List<Comment> comments=commentRepository.findByMsgId(id);
        model.addAttribute("comments", comments);
        return "/user/comments";
    }
    //曲曲写的↓
//    @GetMapping("/personal")
//    String personal(Model model,
//                    @RequestParam(value = "start",defaultValue = "0") Integer start,
//                    @RequestParam(value = "limit",defaultValue = "9") Integer limit,
//                    @RequestParam(value = "sort_method",defaultValue = "time") String sort_method)
//    {
//        //得到当前用户名
//        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String uname = uid.getUsername();
//        System.out.println(sort_method);
//
//        start = start <0 ? 0 :start;
//        Sort sort = Sort.by(Sort.Direction.DESC,sort_method);
//        Pageable pageable = PageRequest.of(start,limit,sort);
//        Page<Message> messages = messageRepository.findByName(uname, pageable);
//        model.addAttribute("messages",messages);
//        model.addAttribute("uname", uname);
//        return "user/personal";
//    }

    @GetMapping("/personal/self")     //点导航栏里的个人首页
    String personal(Model model,
                    @RequestParam(value = "start",defaultValue = "0") Integer start,
                    @RequestParam(value = "limit",defaultValue = "9") Integer limit)
    {
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uname = uid.getUsername();

        start = start <0 ? 0 :start;
        Sort sort = Sort.by(Sort.Direction.DESC,"time");
        Pageable pageable = PageRequest.of(start,limit,sort);
        Page<Message> messages = messageRepository.findByName(uname,pageable);
        model.addAttribute("messages",messages);
        model.addAttribute("uname", uname);
        return "user/personal";
    }

    @GetMapping("/personal/**")     //点消息里的头像
    String personal(Model model,
                    @RequestParam(value = "start",defaultValue = "0") Integer start,
                    @RequestParam(value = "limit",defaultValue = "9") Integer limit,
                    @RequestParam("name") String name,
                    @RequestParam(value = "sort_method",defaultValue = "time") String sort_method)
    {
        //得到当前用户名
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uname = uid.getUsername();

        start = start <0 ? 0 :start;
        Sort sort = Sort.by(Sort.Direction.DESC,sort_method);
        Pageable pageable = PageRequest.of(start,limit,sort);

        if( uname.equals(name))    //登录成功的用户要看自己的个人首页
        {
            Page<Message> messages = messageRepository.findByName(uname, pageable);
            model.addAttribute("messages",messages);
            model.addAttribute("uname", uname);
            return "user/personal";
        }
        else {                //登录成功的用户要看别人的个人首页
            Page<Message> messages = messageRepository.findByName(name, pageable);
            model.addAttribute("messages", messages);
            model.addAttribute("uname", name);
            return "user/personal";
        }
    }

    @PostMapping("/follow")
    public void follow(@RequestParam("name") String name)
    {
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uname = uid.getUsername();
        //判断是否已经关注了这个用户
        Optional<Fan> fanOptional = fanRepository.findByXname(uname);
        if (fanOptional.isPresent()) {
            if(fanRepository.existsByXnameAndYname(uname, name)){ }
            else{
                Fan f = new Fan();
                f.setXname(uname);
                f.setYname(name);
                fanRepository.save(f);
            }
        }
        else{
            Fan f = new Fan();
            f.setXname(uname);
            f.setYname(name);
            fanRepository.save(f);
        }
    }

    @PostMapping("/unfollow")
    public void unfollow(@RequestParam("name") String name)
    {
        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uname = uid.getUsername();
        //判断是否已经关注了这个用户
        Optional<Fan> fanOptional = fanRepository.findByXname(uname);
        if (fanOptional.isPresent()) {
            if(fanRepository.existsByXnameAndYname(uname, name)){
                Fan fan = fanOptional.get();
                fanRepository.delete(fan);
            }
            else{
            }
        }
        else{
        }
    }
}