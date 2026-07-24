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



    public LinePoint(){}



    public LinePoint(
            double lon,
            double lat,
            double elev){

        this.lon=lon;
        this.lat=lat;
        this.elev=elev;

    }



    @Override
    public String toString(){

        return "["+
                lon+
                ","+
                lat+
                ","+
                elev+
                "]";

    }


}