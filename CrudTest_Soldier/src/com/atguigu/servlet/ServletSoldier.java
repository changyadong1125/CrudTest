package com.atguigu.servlet;
/*
 * project:CrudTest
 * package:${PACKAGE_NAME}
 * class:${NAME}
 *
 * @author: smile
 * @create: 2023/3/21-18:00
 * @Version: v1.0
 * @Description:
 */

import com.atguigu.bean.Soldier;
import com.atguigu.service.SoldierListService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@WebServlet(name = "ServletSoldier", value = "/Servlet")
public class ServletSoldier extends ServletBase {
    protected void showSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        ServletContext application = this.getServletContext();
        Map<Integer, Soldier> mapSoldier = (Map<Integer, Soldier>) application.getAttribute("soldierMap");


        List<Soldier> listSoldier = null;
        if (mapSoldier == null) {
            System.out.println("从数据库中找");
            SoldierListService soldierListService = new SoldierListService();
            listSoldier = soldierListService.getALlSoldier();
            mapSoldier = listSoldier.stream().collect(Collectors.toConcurrentMap(soldier -> soldier.getSoldierId(), soldier -> soldier));
            application.setAttribute("soldierMap", mapSoldier);
        } else {
            System.out.println("从缓存中找");
            listSoldier = mapSoldier.values().stream().sorted((o1, o2) -> o1.getSoldierId()-o2.getSoldierId()).collect(Collectors.toList());
        }


        request.setAttribute("list", listSoldier);
        //转到士兵管理页面
        this.processTemplate("SoldierList", request, response);
    }

    protected void toAddSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.processTemplate("addSoldier", request, response);
    }

    protected void addSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        Map<String, String[]> parameterMap = request.getParameterMap();
        Soldier soldier = new Soldier();
        try {
            BeanUtils.populate(soldier, parameterMap);
            SoldierListService soldierListService = new SoldierListService();
            boolean flag = soldierListService.addSoldier(soldier);
            System.out.println(soldier.getSoldierId());
            //添加用户的时候添加到缓存中
            ServletContext servletContext = this.getServletContext();
            Map<Integer, Soldier> mapSoldier = (Map<Integer, Soldier>) servletContext.getAttribute("soldierMap");
            if (flag) {
                mapSoldier.put(soldier.getSoldierId(), soldier);
                request.setAttribute("messages", "添加成功");
                this.processTemplate("Add_success", request, response);
            } else {
                request.setAttribute("messages", "士兵已存在");
                this.processTemplate("Add_success", request, response);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected void delSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SoldierListService soldierListService = new SoldierListService();
        String id = request.getParameter("id");
        soldierListService.delSoldier(id);
        //从缓存中删除
        Map<Integer, Soldier> mapSoldier = (Map<Integer, Soldier>) this.getServletContext().getAttribute("soldierMap");
        mapSoldier.remove(Integer.parseInt(id));
        request.getRequestDispatcher("/Servlet?method=showSoldier").forward(request, response);
    }

    protected void toRepSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        SoldierListService soldierListService = new SoldierListService();
        Soldier SoldierById = soldierListService.getSoldier(id);
        request.setAttribute("id", SoldierById.getSoldierId());
        request.setAttribute("name", SoldierById.getSoldierName());
        request.setAttribute("weapon", SoldierById.getSoldierWeapon());
        this.processTemplate("ReplaceSoldier", request, response);

    }

    protected void repSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SoldierListService soldierListService = new SoldierListService();
        Soldier newSoldier = new Soldier();
        Map<String, String[]> parameterMap = request.getParameterMap();
        //修改缓存
        Map<Integer, Soldier> mapSoldier = (Map<Integer, Soldier>) this.getServletContext().getAttribute("soldierMap");
        try {
            BeanUtils.populate(newSoldier, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        boolean b = soldierListService.updateSoldier(newSoldier);
        mapSoldier.put(newSoldier.getSoldierId(),newSoldier);
        if (b) {
            request.getRequestDispatcher("/Servlet?method=showSoldier").forward(request, response);
        } else {
            try {
                throw new Exception("修改失败！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void selectSoldier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从缓存中查询
        Map<Integer,Soldier> mapSoldier = (Map<Integer, Soldier>) this.getServletContext().getAttribute("soldierMap");
        if (mapSoldier!=null){
            String select = request.getParameter("select");
            System.out.println(select);
            this.getServletContext().setAttribute("message",select);
            List<Soldier> list = mapSoldier.values().stream().filter(soldier -> soldier.getSoldierName().contains(select) || soldier.getSoldierWeapon().contains(select)).collect(Collectors.toList());
            this.getServletContext().setAttribute("list2",list);
            this.processTemplate("selectSoldier",request,response);
        }else{
            response.sendRedirect(request.getContextPath()+"/Servlet?method=showSoldier");
        }
    }
}
