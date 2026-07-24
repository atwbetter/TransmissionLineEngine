package com.tl.service;

import java.util.List;


/**
 * 地形数据访问接口
 *
 * CatenaryCalculator 不关心数据来源
 */
public interface TerrainProvider {


    /**
     * 获取单点高程
     */
    double getElevation(
            double lon,
            double lat
    );



    /**
     * 批量获取高程
     */
    List<Double> getElevations(
            List<double[]> points
    );



    /**
     * 获取坡度
     */
    double getSlope(
            double lon,
            double lat
    );

}