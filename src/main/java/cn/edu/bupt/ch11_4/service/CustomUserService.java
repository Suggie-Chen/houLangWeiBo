package cn.edu.bupt.ch11_4.service;

import cn.edu.bupt.ch11_4.dao.SysUserRepository;
import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    SysUserRepository sysUserRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.findByUsername(s);
        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        return user;
    }
}
