package org.micro.reading.cloud.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.micro.reading.cloud.account.bo.UserBookshelfBO;
import org.micro.reading.cloud.account.service.UserBookshelfService;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 书架接口
 *
 * @author micro-paul
 * @date 2022年03月22日 9:42
 */
@Api(description = "用户书架")
@RestController
@RequestMapping("account/bookshelf")
public class UserBookshelfController {


    @Autowired
    private UserBookshelfService userBookshelfService;

    @ApiOperation(value = "同步书架图书接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int")
    })
    @PostMapping("/sync-book")
    public Result syncUserBookshelf(@RequestHeader("userId") Integer userId, @RequestBody UserBookshelfBO userBookshelfBO) {
        return userBookshelfService.syncUserBookshelf(userId, userBookshelfBO);
    }

    @ApiOperation(value = "获取书架图书接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int")
    })
    public Result getUserBookshelf(@RequestHeader("userId") Integer userId) {
        return userBookshelfService.getUserBookshelf(userId);
    }

    @ApiOperation(value = "用户书架是否存在图书", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "userId", value = "用户ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", required = true, dataType = "String")
    })
    @GetMapping("/exist-book")
    public Result<Integer> userBookshelfExistBook(@RequestHeader("userId") Integer userId, String bookId) {
        return userBookshelfService.userBookshelfExistBook(userId, bookId);
    }
}
