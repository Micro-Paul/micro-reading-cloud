package org.micro.reading.cloud.gateway.controller;

import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author micro-paul
 * @date 2022年03月21日 15:20
 */
public class FallbackController {

    @GetMapping("/fallback")
    public Result fallback() {
        return ResultUtil.fail();
    }
}
