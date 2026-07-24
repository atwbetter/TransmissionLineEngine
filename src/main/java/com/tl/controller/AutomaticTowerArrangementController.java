package com.tl.controller;

import com.tl.common.core.R;
import com.tl.common.utils.LineGeometryUtils;
import com.tl.domain.entity.LineParser;
import com.tl.domain.entity.LinePoint;
import com.tl.domain.entity.LineSampler;
import com.tl.domain.entity.TowerPoint;
import com.tl.service.AutomaticTowerArrangementService;
import com.tl.service.TerrainProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 自动塔式配置控制器
 *
 * @author xingjinshuang
 * @date 2026/07/24
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auto-tower-arrangement")
public class AutomaticTowerArrangementController {
    /**
     * // 添加 logger
     */
    private final Logger log = LoggerFactory.getLogger(AutomaticTowerArrangementController.class);


    @Autowired
    private TerrainProvider terrainProvider;

    @Autowired
    private AutomaticTowerArrangementService arrangementService;


    @RequestMapping("/auto-tower")
    public void autoTowerArrangement() {
        // 1. 获取线路数据
        // 2. 获取塔型配置
        // 3. 获取地形数据
        // 4. 调用悬链线计算器计算悬链线
        // 5. 根据悬链线结果生成塔位点
        // 6. 返回塔位点结果
    }

    /**
     * String line = "[112.618646,26.011955,380.8,112.617296,26.008128,461.6,112.615552,26.005338,541.3,112.612746,25.999093,651]";
     * sampleDistance 5
     *
     * @param line
     * @return {@link R }
     */
    @GetMapping("/gen-tower-points")
    public R genTowerPoints(String line, Double sampleDistance) {
        List<LinePoint> points = LineGeometryUtils.parseLine(line);
        log.info("原始点:{}", points.size());
        double length = LineGeometryUtils.lineLength(points);
        log.info("线路长度:{}米", length);
        List<LinePoint> samples = LineGeometryUtils.sampleLine(points, sampleDistance);
        log.info("采样点:{}", samples.size());
//        for (LinePoint p : samples) {
//            log.info(String.valueOf(p));
//        }
        return R.ok(samples);
    }


    /**
     * 获取该点的高程
     */
    @GetMapping("/get-elevation")
    public R getElevation(Double lon, Double lat) {
        return R.ok(terrainProvider.getElevation(lon, lat));
    }


    /**
     * 获取该点的高程
     */
    @GetMapping("/get-elevation-list")
    public R getElevationList(@RequestBody List<double[]> points) {
        return R.ok(terrainProvider.getElevations(points));
    }


    /**
     * 获取该点的坡度
     */
    @GetMapping("/get-slope")
    public R getSlope(Double lon, Double lat) {
        return R.ok(terrainProvider.getSlope(lon, lat));
    }


    @GetMapping("/gen-line-points")
    public R genLinePoints() {
        String line = "[112.618646,26.011955,380.8,112.617296,26.008128,461.6,112.615552,26.005338,541.3,112.612746,25.999093,651]";
        List<LinePoint> points = LineParser.parse(line);
        LineSampler sampler = new LineSampler(terrainProvider);
        List<TowerPoint> towers = sampler.sample(points, 5);
        return R.ok(towers);

    }


}
