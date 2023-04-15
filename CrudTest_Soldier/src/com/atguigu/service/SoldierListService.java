package com.atguigu.service;

import com.atguigu.bean.Soldier;
import com.atguigu.dao.SoldierDao;

import java.util.List;

/**
 * project:CrudTest
 * package:com.atguigu.service
 * class:SoldierListService
 *
 * @author: smile
 * @create: 2023/3/21-18:01
 * @Version: v1.0
 * @Description:
 */
public class SoldierListService implements serviceInterface<Soldier> {
    private final SoldierDao soldierDao = new SoldierDao();

    @Override
    public List<Soldier> getALlSoldier() {
        return soldierDao.getAllSoldier();
    }

    @Override
    public boolean addSoldier(Soldier soldier) {
        Soldier selectSoldier = soldierDao.SelectSoldier(soldier);
        if (selectSoldier == null) {
            soldierDao.addSoldier(soldier);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delSoldier(Object obj) {
        return soldierDao.delSoldier(obj);
    }

    @Override
    public boolean updateSoldier(Soldier newSoldier) {
        return soldierDao.updateSoldier(newSoldier);
    }

    @Override
    public Soldier getSoldier(Object obj) {
        return soldierDao.getSoldier(obj);
    }
}
