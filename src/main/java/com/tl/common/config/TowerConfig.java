package com.tl.common.config;

import lombok.Data;

@Data
public class TowerConfig {
    /**
     * 采样间隔
     */
    public double sampleDistance = 5;

    /**
     * 最小档距
     */
    public double minSpan = 10;

    /**
     * 最大档距
     */
    public double maxSpan = 300;

    /**
     * 塔高
     */
    public double towerHeight = 25;

    /**
     * 悬链线k
     */
    public double k = 0.02;

    /**
     * 最小对地距离
     */
    private double minGroundClearance = 6;

    /**
     * 最大允许坡度
     */
    private double maxSlope = 45;


}