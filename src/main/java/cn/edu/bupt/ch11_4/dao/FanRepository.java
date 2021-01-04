package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface FanRepository extends JpaRepository<Fan,String> {

    Optional<Fan> findByXname(String xname);

    boolean existsByXnameAndYname(String xname,String yname);

    Fan findByXnameAndYname(String uname, String name);
}
