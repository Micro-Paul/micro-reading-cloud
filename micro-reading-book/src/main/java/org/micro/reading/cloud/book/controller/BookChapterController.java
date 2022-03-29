package org.micro.reading.cloud.book.controller;

import io.swagger.annotations.*;
import org.micro.reading.cloud.book.service.BookChapterService;
import org.micro.reading.cloud.book.vo.BookChapterReadVO;
import org.micro.reading.cloud.common.pojo.book.BookChapter;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图书章节接口
 *
 * @author micro-paul
 * @date 2022年03月17日 15:20
 */
@Api(description = "章节查询接口")
@RestController
@RequestMapping("book/chapter")
public class BookChapterController {

    @Autowired
    private BookChapterService bookChapterService;

    @ApiOperation(value = "查询图书章节基本信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chapterId", value = "章节ID", dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookChapter.class)})
    @GetMapping("/getChapter")
    public Result getChapter(String bookId, Integer chapterId) {
        return bookChapterService.getChapterById(bookId, chapterId);
    }

    @ApiOperation(value = "查询图书章节列表信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookChapter.class)})
    @GetMapping("/getChapterList")
    public Result getBookChapterList(String bookId) {
        return bookChapterService.getBookChapterListByBookId(bookId);
    }

    @ApiOperation(value = "阅读内容", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "chapterId", value = "章节ID", dataType = "Integer")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = BookChapterReadVO.class)})
    @GetMapping("/readChapter")
    public Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId) {
        return bookChapterService.readChapter(bookId, chapterId);
    }
}
