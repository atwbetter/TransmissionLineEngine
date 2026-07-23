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

}
