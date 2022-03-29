package org.micro.reading.cloud.common.cache;

/**
 * 图书资源中心缓存Key
 *
 * @author micro-paul
 * @date 2022年03月16日 17:29
 */
public class RedisBookKey {

    /**
     * 图书信息缓存
     *
     * @param bookId
     * @return java.lang.String
     * @author micro-paul
     * @date 2022/3/17 15:54
     */
    public static final String getBookKey(String bookId) {
        return String.format("book-center:detail-%s", bookId);
    }

    /**
     * 资源中心图书章节缓存
     *
     * @param bookId
     * @return java.lang.String
     * @author micro-paul
     * @date 2022/3/17 15:54
     */
    public static final String getBookChapterKey(String bookId) {
        return String.format("book-center:chapter-%s", bookId);
    }

    /**
     * 资源中心图书章节列表缓存
     *
     * @param bookId
     * @return java.lang.String
     * @author micro-paul
     * @date 2022/3/17 15:55
     */
    public static final String getBookChapterListKey(String bookId) {
        return String.format("book-center:chapter-list-%s", bookId);
    }

    /**
     * 资源中心图书章节节点缓存
     *
     * @param bookId
     * @return null
     * @author micro-paul
     * @date 2022/3/17 15:55
     */
    public static final String getBookChapterNodeKey(Integer bookId) {
        return String.format("book-center:chapter-node-%s", bookId);
    }

    /**
     * 图书资源中心feign-client缓存
     *
     * @author micro-paul
     * @date 2022/3/17 15:55
     * @return null
     */
    public static final class BookCenter {
        /**
         * 资源中心图书缓存
         */
        public static final String getFeignClientBookKey(String bookId) {
            return String.format("book-center:feign-client-book-%s", bookId);
        }

    }
}
