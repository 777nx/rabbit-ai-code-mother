package com.fantasy.rabbitaicodemother.controller;

import com.fantasy.rabbitaicodemother.common.BaseResponse;
import com.fantasy.rabbitaicodemother.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }
}
