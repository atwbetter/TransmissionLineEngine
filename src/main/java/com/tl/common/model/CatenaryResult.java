package com.tl.common.model;

import lombok.Data;

import java.util.List;

@Data
public class CatenaryResult {

    /** 起点塔 */
    private TowerPoint startTower;

    /** 终点塔 */
    private TowerPoint endTower;

    /** 档距 */
    private double span;

    /** 最大弧垂 */
    private double sag;

    /** 最低点 */
    private CatenaryPoint lowestPoint;

    /** 最小安全距离 */
    private double minimumClearance;

    /** 是否满足安全距离 */
    private boolean clearancePass;

    /** 导线采样点 */
    private List<CatenaryPoint> points;

}
