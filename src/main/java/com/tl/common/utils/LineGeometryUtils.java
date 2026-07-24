package com.tl.common.utils;


import com.tl.domain.entity.LinePoint;

import java.util.ArrayList;
import java.util.List;


public class LineGeometryUtils {


    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS =
            6378137;


    /**
     * 解析线路字符串
     * <p>
     * 输入:
     * [lon,lat,z,lon,lat,z]
     */
    public static List<LinePoint> parseLine(
            String line) {


        List<LinePoint> result =
                new ArrayList<>();


        line =
                line.replace("[", "")
                        .replace("]", "");


        String[] arr =
                line.split(",");


        for (int i = 0; i < arr.length; i += 3) {


            double lon =
                    Double.parseDouble(
                            arr[i].trim()
                    );


            double lat =
                    Double.parseDouble(
                            arr[i + 1].trim()
                    );


            double elev =
                    Double.parseDouble(
                            arr[i + 2].trim()
                    );


            result.add(
                    new LinePoint(
                            lon,
                            lat,
                            elev
                    )
            );

        }


        return result;

    }


    /**
     * 计算两点二维距离
     * <p>
     * 单位:米
     */
    public static double distance(
            LinePoint p1,
            LinePoint p2) {


        double lat1 =
                Math.toRadians(p1.lat);


        double lat2 =
                Math.toRadians(p2.lat);


        double dLat =
                lat2 - lat1;


        double dLon =
                Math.toRadians(
                        p2.lon - p1.lon
                );


        double a =
                Math.sin(dLat / 2)
                        *
                        Math.sin(dLat / 2)
                        +
                        Math.cos(lat1)
                                *
                                Math.cos(lat2)
                                *
                                Math.sin(dLon / 2)
                                *
                                Math.sin(dLon / 2);


        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );


        return EARTH_RADIUS * c;

    }


    /**
     * 计算线路总长度
     */
    public static double lineLength(
            List<LinePoint> points) {


        double length = 0;


        for (int i = 0; i < points.size() - 1; i++) {


            length +=
                    distance(
                            points.get(i),
                            points.get(i + 1)
                    );

        }


        return length;

    }


    /**
     * 计算方位角
     * <p>
     * 返回:
     * 0-360°
     */
    public static double azimuth(
            LinePoint p1,
            LinePoint p2) {


        double lat1 =
                Math.toRadians(p1.lat);


        double lat2 =
                Math.toRadians(p2.lat);


        double dLon =
                Math.toRadians(
                        p2.lon - p1.lon
                );


        double y =
                Math.sin(dLon)
                        *
                        Math.cos(lat2);


        double x =
                Math.cos(lat1)
                        *
                        Math.sin(lat2)
                        -
                        Math.sin(lat1)
                                *
                                Math.cos(lat2)
                                *
                                Math.cos(dLon);


        double angle =
                Math.toDegrees(
                        Math.atan2(y, x)
                );


        return
                (angle + 360) % 360;


    }


    /**
     * 在线段中按照比例插值
     * <p>
     * ratio:
     * <p>
     * 0 起点
     * 1 终点
     */
    public static LinePoint interpolate(
            LinePoint p1,
            LinePoint p2,
            double ratio) {


        double lon =
                p1.lon
                        +
                        (p2.lon - p1.lon)
                                *
                                ratio;


        double lat =
                p1.lat
                        +
                        (p2.lat - p1.lat)
                                *
                                ratio;


        double elev =
                p1.elev
                        +
                        (p2.elev - p1.elev)
                                *
                                ratio;


        return new LinePoint(
                lon,
                lat,
                elev
        );

    }


    /**
     * 线路采样
     * <p>
     * distance:
     * 采样间隔 米
     */
    public static List<LinePoint> sampleLine(
            List<LinePoint> line,
            double sampleDistance) {

        List<LinePoint> result = new ArrayList<>();

        if (line == null || line.size() < 2) {
            return result;
        }


        //加入起点

        result.add(line.get(0));

        for (int i = 0; i < line.size() - 1; i++) {

            LinePoint start = line.get(i);

            LinePoint end = line.get(i + 1);

            double segmentLength = distance(start, end);

            int count = (int) (segmentLength / sampleDistance);


            for (int j = 1; j <= count; j++) {


                double ratio = (j * sampleDistance) / segmentLength;


                if (ratio >= 1) {

                    break;

                }


                result.add(interpolate(start, end, ratio));


            }


        }


        //补终点

        result.add(line.get(line.size() - 1));


        return result;

    }

    public static void main(String[] args) {


        String line =
                "[112.653303,26.006398,370.5," +
                        "112.655047,26.00851,333.1," +
                        "112.656158,26.010697,397.1," +
                        "112.659456,26.014043,507.2]";


        List<LinePoint> points = LineGeometryUtils.parseLine(line);

        System.out.println("原始点:" + points.size());

        double length = LineGeometryUtils.lineLength(points);

        System.out.println("线路长度:" + length + "米");

        List<LinePoint> samples = LineGeometryUtils.sampleLine(points, 5);

        System.out.println("采样点:" + samples.size());

        for (LinePoint p : samples) {
            System.out.println(p);

        }

    }


}
