package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {

}
