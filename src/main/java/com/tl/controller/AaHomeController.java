package com.tl.controller;

import com.tl.common.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AA家庭控制器
 *
 * @author x
 * @date 2026/07/23
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/index")
public class AaHomeController {

    @GetMapping("/hello")
    public R<String> hello() {
        return R.ok("成功");
    }






/*

    transmission-line-engine
│
        ├── geometry                //几何计算
│      LineGeometryUtils
│      GeoUtils
│
        ├── terrain                 //DEM分析
│      TerrainService
│      RasterReader
│
        ├── tower                   //塔位
│      TowerPoint
│      TowerConfig
│      TowerLayoutEngine
│
        ├── catenary                //悬链线
│      CatenaryCalculator
│      CatenaryPoint
│      CatenaryResult
│
        ├── safety                  //安全校验
│      GroundClearanceChecker
│      SpanChecker
│      SlopeChecker
│
        ├── optimizer               //排塔优化
│      TowerOptimizer
│      CostFunction
│
        └── model                   //公共模型



        */

}
