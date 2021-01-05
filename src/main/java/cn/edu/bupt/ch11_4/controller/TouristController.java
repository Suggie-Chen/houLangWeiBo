package cn.edu.bupt.ch11_4.controller;


import cn.edu.bupt.ch11_4.dao.*;
import cn.edu.bupt.ch11_4.entity.Message;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tourist")
public class TouristController {
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


    @GetMapping({"","/home"})
    String tour(Model model,
                @RequestParam(value = "start",defaultValue = "0") Integer start,
                @RequestParam(value = "limit",defaultValue = "9") Integer limit,
                @RequestParam(value = "sort_method",defaultValue = "time") String sort_method)
    {
        //得到当前用户名
//        SysUser uid = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String uname = uid.getUsername();

        start = start <0 ? 0 :start;
        Sort sort = Sort.by(Sort.Direction.DESC,sort_method);
        Pageable pageable = PageRequest.of(start,limit,sort);
        Page<Message> messages = messageRepository.findAll(pageable);
        model.addAttribute("messages",messages);
//        model.addAttribute("uname", uname);
        return "tourist/tindex";
    }
}
