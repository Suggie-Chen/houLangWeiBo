package cn.edu.bupt.ch11_4.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;          //用户名
    private String content;   //文字内容
    private Date time;         //发布时间

//    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Message message;
}