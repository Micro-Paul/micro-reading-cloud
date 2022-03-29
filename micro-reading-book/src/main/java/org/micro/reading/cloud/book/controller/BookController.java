package org.micro.reading.cloud.book.controller;

import io.swagger.annotations.*;
import org.micro.reading.cloud.book.service.BookService;
import org.micro.reading.cloud.book.vo.BookVO;
import org.micro.reading.cloud.common.pojo.book.Book;
import org.micro.reading.cloud.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author micro-paul
 * @date 2022年03月16日 16:58
 */
@Api(description = "图书查询接口")
@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @ApiOperation(value = "查询图书基本信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({@ApiResponse(code = 200, message = "", response = Book.class)})
    @GetMapping("/getBookById")
    public Result<Book> getBookById(String bookId) {
        return bookService.getBookById(bookId);
    }

    @ApiOperation(value = "获取图书详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bookId", value = "图书ID", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "", response = BookVO.class)
    })
    @GetMapping("/details")
    public Result<BookVO> getBookDetails(String bookId) {
        return bookService.getBookDetails(bookId);
    }
}
