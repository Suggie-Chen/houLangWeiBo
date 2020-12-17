package cn.edu.bupt.ch11_4.config;

import cn.edu.bupt.ch11_4.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomUserService customUserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        //不拦截静态资源
        web.ignoring().antMatchers("/js/**","/css/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许h2-console的frameset界面
        http.headers()
                .frameOptions().disable()
                //针对h2-console关闭csrf验证
                .and().csrf()
                    .ignoringAntMatchers("/h2-console/**","/user/**","/tourist/**")
                .and().formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        System.out.println("登陆成功处理==============");
                            //跳转到微博整体首页 ，配置登录成功的回调，如果是前后端分离开发的话，登录成功后返回 JSON 即可，
                            httpServletResponse.sendRedirect("/user/home");
                        })
                    .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                            System.out.println("登陆失败处理=============");
                            //返回到登陆页面，可以采用AJAX技术实现，提示用户用户名不存在，密码不正确之类的
                            httpServletResponse.sendRedirect("/user/login");
                        })
                .and().logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessHandler(((httpServletRequest, httpServletResponse, authentication) -> {
                        System.out.println("登出成功处理=============重定向到登录页面");
                        httpServletResponse.sendRedirect("/user/login");
                    }))
                .and().authorizeRequests()
                    .antMatchers("/h2-console/**","/user/register","/tourist/**").permitAll()
//                ！！！！！！！！！！！！！！权限管理
                    .antMatchers("/user/**").hasRole("USER")//Spring Security会自动为任何角色添加前缀ROLE_,使用hasRole表达式来检查当前经过身份验证的主体是否具有指定的权限。
                    .anyRequest().authenticated();//所有进入应用HTTP请求都要进行认证
//        ;and 方法的返回值是一个 SecurityBuilder 的子类，其实就是 HttpSecurity，也就是 and 方法总是让我们回到 HttpSecurity，从而开启新一轮的 xxxConfigurer 配置。
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       auth.userDetailsService(customUserService).passwordEncoder(passwordEncoder);
    }
}
