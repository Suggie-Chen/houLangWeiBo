package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUsername(String username);
}
