package org.micro.reading.cloud.homepage.controller;

import io.swagger.annotations.*;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.homepage.service.IndexPageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 精品页接口
 *
 * @author micro-paul
 * @date 2022年03月24日 15:29
 */
@Api(description = "精品页接口")
@RestController
public class IndexPageConfigController {

    @Autowired
    private IndexPageConfigService indexPageConfigService;

    @ApiOperation(value = "获取精品页接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value = "类型：1.主页，2.男生，3.女生", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页数", required = true, dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "每页数量", required = true, dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
    @GetMapping("index")
    public Result getIndexPageByType(Integer type, Integer page, Integer limit) {
        return indexPageConfigService.getIndexPageByType(type, page, limit);
    }
}
