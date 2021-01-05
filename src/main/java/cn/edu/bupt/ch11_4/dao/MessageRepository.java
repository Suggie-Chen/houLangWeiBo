package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface MessageRepository extends JpaRepository<Message,Long> {
    //有问题，再想想
    Page<Message> findByName(String name, Pageable pageable);
    Integer countByName(String name);  //博客数
//    Long<Message> findById(String id);
}
