package org.micro.reading.cloud.book.service;

import org.micro.reading.cloud.book.vo.BookChapterReadVO;
import org.micro.reading.cloud.common.pojo.book.BookChapter;
import org.micro.reading.cloud.common.result.Result;

/**
 * @author micro-paul
 * @date 2022年03月17日 15:21
 */
public interface BookChapterService {

    Result<BookChapter> getChapterById(String bookId, Integer chapterId);

    Result getBookChapterListByBookId(String bookId);

    Result<BookChapterReadVO> readChapter(String bookId, Integer chapterId);
}
