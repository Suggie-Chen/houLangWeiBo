package cn.edu.bupt.ch11_4.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Fan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //x关注了y
    private Long xid;           //x用户id
    private String xname;       //x用户名
    private Long yid;           //y用户id
    private String yname;       //y用户名
}
