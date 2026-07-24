package com.tl.domain.entity;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

@Data
public class TowerPoint {

    /**
     * 塔号
     */
    public int id;

    /**
     * 经纬度
     */
    public double lon;

    public double lat;

    /**
     * 地面高程
     */
    public double elev;

    /**
     * 坡度
     */
    public double slope;

    /**
     * 塔高
     */
    public double towerHeight=25;

    /**
     * 与前塔距离
     */
    public double span;

    /**
     * 前后悬链线
     */
    public JSONArray catenary;


}
