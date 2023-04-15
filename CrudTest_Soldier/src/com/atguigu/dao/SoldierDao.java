package com.atguigu.dao;

import com.atguigu.bean.Soldier;

import java.sql.*;
import java.util.List;

/**
 * project:CrudTest
 * package:com.atguigu.dao
 * class:SoldierDao
 *
 * @author: smile
 * @create: 2023/3/21-17:39
 * @Version: v1.0
 * @Description:
 */
public class SoldierDao extends BaseDao<Soldier> implements SoldierInterface {


    @Override
    public List<Soldier> getAllSoldier() {
        String sql = "select soldier_id soldierId , soldier_name soldierName ,soldier_weapon soldierWeapon from soldier";
        return this.getList(sql);
    }

    @Override
    public void addSoldier(Soldier soldier) {
        String sql = "insert into soldier values(null,?,?)";
        //通过JDBC进行添加并返回自增值
        Connection connection = getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, soldier.getSoldierName());
            pst.setObject(2, soldier.getSoldierWeapon());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int  index = generatedKeys.getInt(1);
                soldier.setSoldierId(index);
            }
            generatedKeys.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close();
        }
    }

    @Override
    public Soldier SelectSoldier(Soldier soldier) {
        String sql = "select soldier_id soldierId , soldier_name soldierName ,soldier_weapon soldierWeapon from soldier where soldier_name =?";
        return this.getBean(sql, soldier.getSoldierName());
    }

    @Override
    public boolean delSoldier(Object obj) {
        String sql = "delete from soldier where soldier_id =?";
        return update(sql, obj);
    }

    @Override
    public boolean updateSoldier(Soldier newSoldier) {
        String sql = "update soldier set soldier_name =? ,soldier_weapon=? where soldier_id=?";
        return update(sql, newSoldier.getSoldierName(), newSoldier.getSoldierWeapon(), newSoldier.getSoldierId());
    }

    @Override
    public Soldier getSoldier(Object obj) {
        String sql = "select soldier_id soldierId , soldier_name soldierName ,soldier_weapon soldierWeapon from soldier where soldier_id =?";
        return getBean(sql, obj);
    }

}
