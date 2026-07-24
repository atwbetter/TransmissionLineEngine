package com.tl.domain.model;

import lombok.Data;

@Data
public class LinePoint {

    /** 经度 */
    private double lon;

    /** 纬度 */
    private double lat;

    /** 地面高程 */
    private double groundElevation;

    /** 地面坡度 */
    private double slope;

    /** 沿线路累计距离(米) */
    private double distance;

}