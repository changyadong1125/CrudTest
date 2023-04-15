package com.atguigu.dao;

import com.atguigu.bean.Soldier;

import java.util.List;

/**
 * project:CrudTest
 * package:com.atguigu.dao
 * class:SoldierInterface
 *
 * @author: smile
 * @create: 2023/3/21-17:40
 * @Version: v1.0
 * @Description:
 */
public interface SoldierInterface {
    List<Soldier> getAllSoldier();
    void addSoldier(Soldier soldier);
    Soldier SelectSoldier(Soldier soldier);
    boolean delSoldier(Object obj);
    boolean updateSoldier(Soldier newSoldier);

    Soldier getSoldier(Object obj);
}
