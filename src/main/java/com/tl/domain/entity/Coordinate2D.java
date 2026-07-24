package com.tl.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate2D {


    /**
     * 经度
     */
    private double lon;


    /**
     * 纬度
     */
    private double lat;

}
