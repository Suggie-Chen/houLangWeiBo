package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Comment;
import cn.edu.bupt.ch11_4.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface FanRepository extends JpaRepository<Fan,String> {

    boolean existsByXnameAndYname(String xname,String yname);
    Fan findByXnameAndYname(String uname, String name);
    Integer countByXname(String x); //关注数
    Integer countByYname(String y); //粉丝数
    List<Fan> findByXname(String name);
    List<Fan> findByYname(String name);
}
