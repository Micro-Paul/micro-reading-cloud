package org.micro.reading.cloud.book.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author micro-paul
 * @date 2022年03月17日 16:35
 */
@Data
@AllArgsConstructor
public class BookChapterVO {

    private Integer id;
    private String name;
    private String content;
}
