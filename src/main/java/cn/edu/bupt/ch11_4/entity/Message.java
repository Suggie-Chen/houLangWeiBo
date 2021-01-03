package cn.edu.bupt.ch11_4.entity;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;           //消息id
    private Long userid;       //用户id
    private String name;       //用户名
    private Date time;         //发布时间
    private String content;    //正文
    private Integer thumbUp;  //点赞数
    private Integer cmtNum; //评论数

//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    @Column(/*name="picture",*/ columnDefinition="longblob")
//    private byte[] picture;    //图片

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<Comment>();


}
