package cn.edu.bupt.ch11_4.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Fan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //x关注了y
    private String xname;       //x用户名
    private String yname;       //y用户名

}
