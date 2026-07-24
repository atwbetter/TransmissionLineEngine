package com.tl.common.utils;
public final class GeoDistance {


    private GeoDistance(){}



    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS =
            6378137;



    /**
     * 经纬度距离
     */
    public static double distance(
            double lon1,
            double lat1,
            double lon2,
            double lat2
    ){


        double radLat1 =
                Math.toRadians(lat1);


        double radLat2 =
                Math.toRadians(lat2);



        double dLat =
                Math.toRadians(
                        lat2-lat1
                );


        double dLon =
                Math.toRadians(
                        lon2-lon1
                );



        double a =
                Math.sin(dLat/2)
                        *
                        Math.sin(dLat/2)
                        +
                        Math.cos(radLat1)
                                *
                                Math.cos(radLat2)
                                *
                                Math.sin(dLon/2)
                                *
                                Math.sin(dLon/2);



        double c =
                2*
                        Math.atan2(
                                Math.sqrt(a),
                                Math.sqrt(1-a)
                        );


        return EARTH_RADIUS*c;

    }



}