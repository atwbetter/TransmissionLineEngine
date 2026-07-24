package com.tl.common.gdal;


import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;


public class GdalUtils {


    static {
        // 初始化GDAL
        gdal.AllRegister();

    }


    /**
     * 获取DEM高程
     *
     * @param lon     经度
     * @param lat     纬度
     * @param demPath DEM文件
     */
    public static double getDemValByCoordinate(
        double lon,
        double lat,
        String demPath) {


        return getRasterValue(
            lon,
            lat,
            demPath
        );

    }


    /**
     * 获取坡度
     *
     * @param lon       经度
     * @param lat       纬度
     * @param slopePath 坡度文件
     */
    public static double getSlopeValByCoordinate(
        double lon,
        double lat,
        String slopePath) {


        return getRasterValue(
            lon,
            lat,
            slopePath
        );

    }


    /**
     * 栅格查询核心方法
     */
    private static double getRasterValue(
        double lon,
        double lat,
        String tifPath) {


        Dataset dataset = null;


        try {


            dataset =
                gdal.Open(tifPath);


            if (dataset == null) {

                throw new RuntimeException(
                    "打开TIF失败:"
                        + tifPath
                );
            }


            /**
             * 获取仿射参数
             *
             * GT[0] 左上角X
             * GT[1] 像元宽度
             * GT[2] 旋转
             * GT[3] 左上角Y
             * GT[4] 旋转
             * GT[5] 像元高度
             *
             */

            double[] gt =
                dataset.GetGeoTransform();


            int px =
                (int) Math.floor(
                    (lon - gt[0])
                        /
                        gt[1]
                );


            int py =
                (int) Math.floor(
                    (lat - gt[3])
                        /
                        gt[5]
                );


            //越界判断

            if (px < 0
                || py < 0
                || px >= dataset.GetRasterXSize()
                || py >= dataset.GetRasterYSize()) {


                return Double.NaN;

            }


            Band band =
                dataset.GetRasterBand(1);


            double[] data =
                new double[1];


            band.ReadRaster(
                px,
                py,
                1,
                1,
                data
            );


            double value = data[0];


            /**
             * 常见DEM无效值处理
             *
             * -9999
             * -32768
             * 0
             */

            if (isNoData(value)) {

                return Double.NaN;

            }


            return value;


        } finally {


            if (dataset != null) {

                dataset.delete();

            }

        }


    }


    /**
     * 判断无效值
     */
    private static boolean isNoData(
        double value) {


        if (Double.isNaN(value)) {

            return true;

        }


        if (value <= -9990) {

            return true;

        }


        return false;

    }


    public static void main(String[] args) {
        double lon = 112.65807;
        double lat = 26.01319;

        double dem = GdalUtils.getDemValByCoordinate(lon, lat, "E:\\tifdata\\D1\\China_Dem_15.tif");
        double slope = GdalUtils.getSlopeValByCoordinate(lon, lat, "E:\\tifdata\\China_Slope_15.tif");

        System.out.println("高程:" + dem);
        System.out.println("坡度:" + slope);
    }


}
