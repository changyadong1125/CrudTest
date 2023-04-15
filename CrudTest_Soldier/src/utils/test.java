package utils;

import com.atguigu.bean.Soldier;
import com.atguigu.dao.SoldierDao;
import org.junit.Test;

/**
 * project:CrudTest
 * package:utils
 * class:test
 *
 * @author: smile
 * @create: 2023/3/22-18:09
 * @Version: v1.0
 * @Description:
 */
public class test {
    @Test
    public void test(){
        SoldierDao soldierDao = new SoldierDao();
       soldierDao.addSoldier(new Soldier());

    }
}
