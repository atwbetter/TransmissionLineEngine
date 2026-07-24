package com.tl.domain.entity;

import com.tl.common.config.TowerConfig;
import com.tl.common.utils.GeoDistance;
import com.tl.service.TerrainProvider;

import java.util.ArrayList;
import java.util.List;

public class LineSampler {
    private final TerrainProvider terrainProvider;

    public LineSampler(TerrainProvider terrainProvider) {
        this.terrainProvider = terrainProvider;
    }

    /**
     * 线路采样
     *
     * @param line     原始线路
     * @param interval 采样间隔 米
     */
    public List<TowerPoint> sample(List<LinePoint> line, double interval) {
        TowerConfig param = new TowerConfig();

        List<TowerPoint> result = new ArrayList<>();
        int index = 1;
        for (int i = 0; i < line.size() - 1; i++) {
            LinePoint start = line.get(i);
            LinePoint end = line.get(i + 1);
            double distance = GeoDistance.distance(start.getLon(), start.getLat(), end.getLon(), end.getLat());
            int count = (int)
                    Math.ceil(distance / interval);
            for (int j = 0; j < count; j++) {
                double ratio = (double) j / count;
                double lon = start.getLon() + (end.getLon() - start.getLon()) * ratio;
                double lat = start.getLat() + (end.getLat() - start.getLat()) * ratio;
                //关键：
                //重新获取DEM高程
                double elev = terrainProvider.getElevation(lon, lat);
                double slope = terrainProvider.getSlope(lon, lat);

                //result.add(TowerPoint.builder().id(index++).lon(lon).lat(lat).groundElevation(elev).build());

                TowerPoint tower = TowerPoint.builder().
                        id(index++).
                        lon(lon).
                        lat(lat).
                        elev(elev).
                        groundElevation(elev).
                        towerHeight(param.getTowerHeight()).
                        towerTopElevation(elev + param.getTowerHeight()).
                        slope(slope).
                        valid(slope <= param.getMaxSlope()).build();

                result.add(tower);
            }
        }
        //加入终点
        LinePoint last = line.get(line.size() - 1);
        //result.add(TowerPoint.builder().id(index).lon(last.getLon()).lat(last.getLat()).groundElevation(terrainProvider.getElevation(last.getLon(), last.getLat())).build());
        double elev = terrainProvider.getElevation(last.getLon(), last.getLat());
        double slope = terrainProvider.getSlope(last.getLon(), last.getLat());

        TowerPoint tower = TowerPoint.builder().
                id(index++).
                lon(last.getLon()).
                lat(last.getLat()).
                elev(elev).
                groundElevation(elev).
                towerHeight(param.getTowerHeight()).
                towerTopElevation(elev + param.getTowerHeight()).
                slope(slope).
                valid(slope <= param.getMaxSlope()).build();

        result.add(tower);
        return result;
    }
}