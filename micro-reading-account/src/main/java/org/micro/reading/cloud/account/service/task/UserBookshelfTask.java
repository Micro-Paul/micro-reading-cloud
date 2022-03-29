package org.micro.reading.cloud.account.service.task;

import lombok.AllArgsConstructor;
import org.micro.reading.cloud.account.dao.UserBookshelfMapper;
import org.micro.reading.cloud.common.pojo.account.UserBookshelf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 书架同步任务
 *
 * @author micro-paul
 * @date 2022年03月22日 10:07
 */
@AllArgsConstructor
public class UserBookshelfTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBookshelfTask.class);

    /**
     * 处理类型：1.新增 2.修改 3.删除
     */
    private Integer syncType;
    private UserBookshelf bookshelf;
    private UserBookshelfMapper bookshelfMapper;
    private Integer userId;

    public UserBookshelfTask() {
    }

    @Override
    public void run() {
        try {
            if (1 == this.syncType) {
                this.bookshelf.setUserId(this.userId);
                this.bookshelfMapper.insert(this.bookshelf);
            } else if (2 == this.syncType) {
                this.bookshelf.setUserId(this.userId);
                this.bookshelfMapper.updateByUserIdAndBookId(this.bookshelf);
            } else if (3 == this.syncType) {
                this.bookshelfMapper.deleteById(this.bookshelf.getId());
            }
        } catch (Exception e) {
            LOGGER.error("书架同步失败，同步类型[{}]，异常:{}", this.syncType, e);
        }

    }
}
