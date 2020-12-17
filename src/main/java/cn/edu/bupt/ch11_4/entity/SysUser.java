package cn.edu.bupt.ch11_4.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class SysUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
//    private List<Long> followers;
    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    //在系统中定义用户，角色，权限这三种实体，一个用户可以拥有多个角色，一个角色可以被多个用户拥有，所以用户与角色之间是多对多的关系，为了易于理解，这里加入第三种实体权限，作为用户和角色的中间关联实体，把用户与角色间的多对多关系拆为两个一对多的关联关系。这样一个用户就对应着多个权限，一个权限对应着一个用户，而一个角色对应着多个权限，一个权限对应着一个角色。
    private List<SysRole> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        List<SysRole> roles = this.getRoles();
        for(SysRole role:roles){
            auths.add(new SimpleGrantedAuthority(role.getType()));
        }
        System.out.println(auths);
        return auths;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
