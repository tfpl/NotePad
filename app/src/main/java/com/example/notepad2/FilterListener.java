package com.example.notepad2;

import java.util.ArrayList;
/**
 * 接口类，抽象方法用来获取过滤后的数据
 * @author 邹奇
 *
 */
public interface FilterListener {

    void getFilterData(ArrayList<Cuns> list);// 获取过滤后的数据

}