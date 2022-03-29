package org.micro.reading.cloud.homepage.service;

import org.micro.reading.cloud.common.pojo.index.IndexBooklist;
import org.micro.reading.cloud.homepage.vo.IndexBooklistVO;

/**
 * @author micro-paul
 * @date 2022年03月24日 16:00
 */
public interface IndexBooklistService {

    /**
     * 获取书单VO
     * @param itemId
     * @param clientRandomNumber
     * @return
     */
    IndexBooklistVO getIndexBooklistVO(Integer itemId, Integer clientRandomNumber);

    /**
     * 查询书单信息
     * @param booklistId
     * @return
     */
    IndexBooklist getIndexBooklistById(Integer booklistId);

    /**
     * 获取随机榜单VO
     * @param booklist
     * @param clientRandomNumber
     * @return
     */
    IndexBooklistVO getRandomIndexBooklistVO(IndexBooklist booklist, Integer clientRandomNumber);
}
