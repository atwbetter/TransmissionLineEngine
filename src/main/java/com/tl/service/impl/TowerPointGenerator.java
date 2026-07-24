package com.tl.service.impl;

import com.tl.common.config.TowerConfig;
import com.tl.domain.entity.LinePoint;
import com.tl.domain.entity.TowerPoint;
import com.tl.service.TerrainProvider;

import java.util.ArrayList;
import java.util.List;

public class TowerPointGenerator {

    private final TerrainProvider terrainProvider;

    public TowerPointGenerator(TerrainProvider terrainProvider) {
        this.terrainProvider = terrainProvider;
    }


    public List<TowerPoint> generate(List<LinePoint> samples, TowerConfig param) {
        List<TowerPoint> towers = new ArrayList<>();

        int index = 1;

        for (LinePoint c : samples) {
            double elev = terrainProvider.getElevation(c.getLon(), c.getLat());
            double slope = terrainProvider.getSlope(c.getLon(), c.getLat());

            TowerPoint tower = TowerPoint.builder()
                    .id(index++)
                    .lon(c.getLon())
                    .lat(c.getLat())
                    .groundElevation(elev)
                    .towerHeight(param.getTowerHeight())
                    .groundElevation(elev + param.getTowerHeight())
                    .slope(slope)
                    .valid(slope <= param.getMaxSlope())
                    .build();
            towers.add(tower);
        }

        return towers;


    }


}