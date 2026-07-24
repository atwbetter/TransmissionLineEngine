package com.tl.domain.model;

import lombok.Data;

@Data
public class CatenaryPoint {

    /** 经度 */
    private double lon;

    /** 纬度 */
    private double lat;

    /** 导线高程 */
    private double wireElevation;

    /** 地面高程 */
    private double groundElevation;

    /** 对地距离 */
    private double clearance;

    /** 是否碰撞 */
    private boolean collision;

    /** 沿档距距离 */
    private double distance;

}
