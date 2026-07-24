package com.tl.domain;

import lombok.Data;

/**
 * 悬链线采样点
 */
@Data
public class CatenaryPoint {


    /**
     * 经度
     */
    private double lon;


    /**
     * 纬度
     */
    private double lat;


    /**
     * 导线高度
     */
    private double wireElevation;


    /**
     * 地面高度
     */
    private double groundElevation;


    /**
     * 净空距离
     */
    private double clearance;



    /**
     * 是否满足安全距离
     */
    private boolean safe;



    /**
     * 距离起点
     */
    private double distance;





}