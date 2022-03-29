package org.micro.reading.cloud.account.service.task;

import lombok.AllArgsConstructor;
import org.micro.reading.cloud.common.cache.RedisAccountKey;
import org.micro.reading.cloud.common.cache.RedisService;

/**
 * @author micro-paul
 * @date 2022年03月22日 10:25
 */
@AllArgsConstructor
public class LikeSeeClickTask implements Runnable {

    private RedisService redisService;
    private String bookId;
    private Integer value;

    LikeSeeClickTask() {
    }


    @Override
    public void run() {
        // 喜欢数若存在，进行相应的递增或递减
        try {
            if (this.redisService.hashHasKey(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId)) {
                int val = 1;
                if (value <= 0) {
                    val = -1;
                }
                this.redisService.hashIncrement(RedisAccountKey.ACCOUNT_CENTER_BOOK_LIKES_COUNT, this.bookId, val);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
