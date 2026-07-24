package com.tl.domain.entity;


import lombok.Data;

/**
 * 线路三维点
 */
@Data
public class LinePoint {


    /**
     * 经度
     */
    public double lon;


    /**
     * 纬度
     */
    public double lat;


    /**
     * 高程
     */
    public double elev;

    /**
     * 地面高程
     */
    private double groundElevation;

    /**
     * 地面坡度
     */
    private double slope;

    /**
     * 沿线路累计距离(米)
     */
    private double distance;


    public LinePoint() {
    }


    public LinePoint(
            double lon,
            double lat,
            double elev) {

        this.lon = lon;
        this.lat = lat;
        this.elev = elev;

    }


    @Override
    public String toString() {

        return "[" +
                lon +
                "," +
                lat +
                "," +
                elev +
                "]";

    }


}