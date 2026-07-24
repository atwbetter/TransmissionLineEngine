package com.tl.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class LineParser {


    public static List<LinePoint> parse(String line) {


        List<LinePoint> result = new ArrayList<>();

        String value = line.replace("[", "").replace("]", "");

        String[] arr = value.split(",");

        for (int i = 0; i < arr.length; i += 3) {

            LinePoint p = new LinePoint();

            p.setLon(Double.parseDouble(arr[i]));

            p.setLat(Double.parseDouble(arr[i + 1]));

            p.setElev(Double.parseDouble(arr[i + 2]));

            result.add(p);
        }


        return result;
    }

}