package com.atguigu.servlet; /**
 * project:CrudTest
 * package:${PACKAGE_NAME}
 * class:${NAME}
 *
 * @author: smile
 * @create: 2023/3/24-8:37
 * @Version: v1.0
 * @Description:
 */

import com.atguigu.bean.Soldier;
import com.atguigu.service.SoldierListService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ServletSoldier_V2", value = "/ServletSoldier_V2")
public class ServletSoldier_V2 extends ServletBase {

    protected void showSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Integer, Soldier> mapSoldier = (Map<Integer, Soldier>) this.getServletContext().getAttribute("mapSoldier");
        SoldierListService soldierListService = new SoldierListService();
        if (mapSoldier==null){
            List<Soldier> soldierList = soldierListService.getALlSoldier();
            soldierList.stream().collect(Collectors.toConcurrentMap(Soldier::getSoldierId, soldier -> soldier));


        }



    }
}
