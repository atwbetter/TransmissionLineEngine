package com.tl.service;

import com.tl.domain.model.TowerPoint;
import com.tl.domain.CatenaryPoint;
import com.tl.domain.CatenaryResult;
import com.tl.domain.entity.TowerConfig;

import java.util.ArrayList;
import java.util.List;


public class CatenaryCalculator {


    /**
     * 计算悬链线
     */
    public CatenaryResult calculate(
            TowerPoint start,
            TowerPoint end,
            TowerConfig config,
            TerrainProvider terrain) {

        CatenaryResult result = new CatenaryResult();

        /*
         * 档距
         */
        double span = distance(start.getLon(), start.getLat(), end.getLon(), end.getLat());
        result.setSpan(span);

        /*
         * 弧垂
         */
        double sag = span * config.getK();
        result.setSag(sag);

        /*
         * 采样数量
         */
        int count = (int) Math.ceil(span / config.getSampleDistance());

        List<double[]> queryPoints = new ArrayList<>();

        List<CatenaryPoint> points = new ArrayList<>();


        /*
         * 第一阶段:
         * 生成空间点
         */
        for (int i = 0; i <= count; i++) {

            double ratio = (double) i / count;

            double distance = span * ratio;

            double[] xy = interpolate(start, end, ratio);

            queryPoints.add(xy);

        }





        /*
         * 批量查询DEM
         */
        List<Double> elevations = terrain.getElevations(queryPoints);

        double minClearance = Double.MAX_VALUE;

        CatenaryPoint lowest = null;



        /*
         * 第二阶段:
         * 计算导线
         */
        for (int i = 0; i < queryPoints.size(); i++) {

            double ratio = (double) i / count;

            double x = span * ratio;



            /*
             * 抛物线悬链
             *
             * 两端=0
             *
             * 中间=sag
             */
            double offset = 4 * sag * ratio * (1 - ratio);


            double towerStart = start.getGroundElevation() + config.getTowerHeight();


            double towerEnd = end.getGroundElevation() + config.getTowerHeight();


            double lineHeight = towerStart + (towerEnd - towerStart) * ratio - offset;


            CatenaryPoint cp = new CatenaryPoint();


            cp.setLon(queryPoints.get(i)[0]);


            cp.setLat(queryPoints.get(i)[1]);


            cp.setWireElevation(lineHeight);


            cp.setGroundElevation(elevations.get(i));


            double clearance = lineHeight - elevations.get(i);


            cp.setClearance(clearance);


            boolean safe = clearance >= config.getMinGroundClearance();


            cp.setSafe(safe);


            if (clearance < minClearance) {


                minClearance = clearance;


                lowest = cp;

            }


            points.add(cp);


        }


        result.setPoints(points);

        result.setLowestPoint(lowest);


        result.setMinimumClearance(minClearance);


        result.setPass(minClearance >= config.getMinGroundClearance());


        return result;


    }


    /**
     * 经纬度插值
     */
    private double[] interpolate(
            TowerPoint a,
            TowerPoint b,
            double ratio) {


        return new double[]{

                a.getLon()
                        +
                        (
                                b.getLon()
                                        -
                                        a.getLon()
                        )
                                *
                                ratio,


                a.getLat()
                        +
                        (
                                b.getLat()
                                        -
                                        a.getLat()
                        )
                                *
                                ratio

        };

    }


    /**
     * 球面距离
     */
    private double distance(
            double lon1,
            double lat1,
            double lon2,
            double lat2) {


        double R = 6378137;


        double dLat = Math.toRadians(lat2 - lat1);


        double dLon = Math.toRadians(lon2 - lon1);


        double a = Math.sin(dLat / 2) *
                Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) *
                Math.sin(dLon / 2);


        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    }

}