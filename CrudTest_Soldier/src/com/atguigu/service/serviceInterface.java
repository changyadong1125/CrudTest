package com.atguigu.service;

import com.atguigu.bean.Soldier;

import java.util.List;

/**
 * project:CrudTest
 * package:com.atguigu.service
 * class:serviceInterface
 *
 * @author: smile
 * @create: 2023/3/21-16:51
 * @Version: v1.0
 * @Description:
 */
public interface serviceInterface<T> {
    List<T> getALlSoldier();
     boolean addSoldier(Soldier soldier);
     boolean delSoldier(Object obj);
     boolean updateSoldier(Soldier newSoldier);
     T getSoldier(Object obj);
}

