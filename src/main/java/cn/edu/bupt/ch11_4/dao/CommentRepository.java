package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
