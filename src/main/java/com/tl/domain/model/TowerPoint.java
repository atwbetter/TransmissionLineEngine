package com.tl.domain.model;

import lombok.Data;

@Data
public class TowerPoint {

    /** 塔号 */
    private int id;

    /** 经度 */
    private double lon;

    /** 纬度 */
    private double lat;

    /** 地面高程 */
    private double groundElevation;

    /** 地面坡度 */
    private double slope;

    /** 塔高 */
    private double towerHeight;

    /** 塔顶高程 */
    private double towerTopElevation;

    /** 与前塔档距 */
    private double span;

    /** 前一档导线 */
    private CatenaryResult previousCatenary;

    /** 后一档导线 */
    private CatenaryResult nextCatenary;

}