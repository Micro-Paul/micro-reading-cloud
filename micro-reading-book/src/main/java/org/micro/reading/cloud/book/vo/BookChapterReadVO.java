package org.micro.reading.cloud.book.vo;

import lombok.Data;

/**
 * @author micro-paul
 * @date 2022年03月17日 16:34
 */
@Data
public class BookChapterReadVO {

    private BookChapterVO current;
    private BookChapterVO pre;
    private BookChapterVO next;
}
