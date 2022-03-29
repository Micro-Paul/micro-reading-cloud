package org.micro.reading.cloud.book.dao;

import org.micro.reading.cloud.common.pojo.book.BookChapter;

import java.util.List;

/**
 * @author micro-paul
 * @date 2022年03月17日 15:30
 */
public interface BookChapterMapper {


    BookChapter selectById(Integer chapterId);

    List<BookChapter> findPageWithResult(Integer id);
}
