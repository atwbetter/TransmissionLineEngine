package com.tl.common.gdal;


import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;

import java.util.ArrayList;
import java.util.List;


public class GdalRasterReader {


    static {

        gdal.AllRegister();

    }


    /**
     * TIF数据集
     */
    private Dataset dataset;


    /**
     * 栅格波段
     */
    private Band band;


    /**
     * 仿射参数
     */
    private double[] geoTransform;


    private int width;

    private int height;


    private String tifPath;


    public GdalRasterReader(
        String tifPath) {


        this.tifPath = tifPath;


        open();

    }


    /**
     * 打开TIF
     */
    private void open() {


        dataset =
            gdal.Open(tifPath);


        if (dataset == null) {

            throw new RuntimeException(
                "无法打开:"
                    + tifPath
            );

        }


        band =
            dataset.GetRasterBand(1);


        geoTransform =
            dataset.GetGeoTransform();


        width =
            dataset.GetRasterXSize();


        height =
            dataset.GetRasterYSize();


    }


    /**
     * 单点查询
     */
    public double getValue(
        double lon,
        double lat) {


        int[] pixel =
            lonLatToPixel(
                lon,
                lat
            );


        int x = pixel[0];

        int y = pixel[1];


        if (x < 0
            || y < 0
            || x >= width
            || y >= height) {

            return Double.NaN;

        }


        double[] value =
            new double[1];


        band.ReadRaster(
            x,
            y,
            1,
            1,
            value
        );


        if (isNoData(value[0])) {

            return Double.NaN;

        }


        return value[0];

    }


    /**
     * 批量查询
     */
    public List<Double> getValues(
        List<double[]> points) {


        List<Double> result =
            new ArrayList<>(
                points.size()
            );


        for (double[] p : points) {


            result.add(
                getValue(
                    p[0],
                    p[1]
                )
            );

        }


        return result;

    }


    /**
     * 经纬度转像元
     */
    private int[] lonLatToPixel(
        double lon,
        double lat) {


        int x =
            (int) Math.floor(
                (lon - geoTransform[0])
                    /
                    geoTransform[1]
            );


        int y =
            (int) Math.floor(
                (lat - geoTransform[3])
                    /
                    geoTransform[5]
            );


        return new int[]{
            x,
            y
        };


    }


    /**
     * 无效值判断
     */
    private boolean isNoData(
        double value) {


        return Double.isNaN(value)
            ||
            value <= -9990;

    }


    /**
     * 释放资源
     */
    public void close() {


        if (dataset != null) {

            dataset.delete();

            dataset = null;

        }


    }


    public static void main(String[] args) {

        String demPath = "E:\\tifdata\\D1\\China_Dem_15.tif";
        String slopePath = "E:\\tifdata\\China_Slope_15.tif";

        GdalRasterReader demReader = new GdalRasterReader(demPath);
        GdalRasterReader slopeReader = new GdalRasterReader(slopePath);


        List<double[]> points = new ArrayList<>();

        points.add(
            new double[]{
                112.65807,
                26.01319
            }
        );

        points.add(
            new double[]{
                119.802700,
                35.874200
            }
        );

        List<Double> elevations = demReader.getValues(points);

        List<Double> slopes = slopeReader.getValues(points);


        for (int i = 0; i < points.size(); i++) {
            System.out.println("点:" + i + " 高程:" + elevations.get(i) + " 坡度:" + slopes.get(i)
            );


        }


    }


}
