package com.tl.service.impl;

import com.tl.common.gdal.GdalRasterReader;
import com.tl.common.gdal.GdalUtils;
import com.tl.service.TerrainProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TerrainProviderImpl implements TerrainProvider {


    @Value("${project.file-path.dem}")
    private String demTifPath;

    @Value("${project.file-path.slope}")
    private String slopeTifPath;


    /**
     * 获取高程
     *
     * @param lon 隆
     * @param lat 纬度
     * @return double
     */
    @Override
    public double getElevation(double lon, double lat) {
        return GdalUtils.getDemValByCoordinate(lon, lat, demTifPath);
    }

    @Override
    public List<Double> getElevations(List<double[]> points) {
        GdalRasterReader demReader = new GdalRasterReader(demTifPath);
        points.add(new double[]{112.65807, 26.01319});
        points.add(new double[]{119.802700, 35.874200});
        List<Double> elevations = demReader.getValues(points);


        return elevations;
    }

    /**
     * 去坡度
     *
     * @param lon 隆
     * @param lat 纬度
     * @return double
     */
    @Override
    public double getSlope(double lon, double lat) {
        return GdalUtils.getSlopeValByCoordinate(lon, lat, slopeTifPath);
    }

    @Override
    public List<Double> getSlopes(List<double[]> points) {
        GdalRasterReader slopeReader = new GdalRasterReader(slopeTifPath);
        points.add(new double[]{112.65807, 26.01319});
        points.add(new double[]{119.802700, 35.874200});
        List<Double> slopes = slopeReader.getValues(points);

        return slopes;
    }


}
