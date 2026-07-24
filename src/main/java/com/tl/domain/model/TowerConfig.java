package com.tl.domain.model;

import lombok.Data;

@Data
public class TowerConfig {

    /** 杆塔高度 */
    private double towerHeight = 25;

    /** 最大档距 */
    private double maxSpan = 300;

    /** 最小档距 */
    private double minSpan = 10;

    /** 采样间隔 */
    private double sampleDistance = 5;

    /** 最小对地距离 */
    private double minimumGroundClearance = 6;

    /** 最大允许坡度 */
    private double maximumSlope = 45;

    /** 悬链线系数 */
    private double k = 0.03;

}