package cn.edu.bupt.ch11_4.dao;

import cn.edu.bupt.ch11_4.entity.Fan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanRepository {
    //有问题，再想想
    Fan findByXid(Long xid);
    Fan findByXname(String xname);
    Fan findByYid(Long yid);
    Fan findByYname(String yname);
}
