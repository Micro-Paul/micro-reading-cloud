package org.micro.reading.cloud.homepage.service.impl;

import com.github.pagehelper.PageHelper;
import org.micro.reading.cloud.common.cache.RedisExpire;
import org.micro.reading.cloud.common.cache.RedisHomepageKey;
import org.micro.reading.cloud.common.cache.RedisService;
import org.micro.reading.cloud.common.pojo.index.IndexPageConfig;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.common.result.ResultUtil;
import org.micro.reading.cloud.common.utils.CommonUtil;
import org.micro.reading.cloud.homepage.dao.IndexPageConfigMapper;
import org.micro.reading.cloud.homepage.service.IndexBannerService;
import org.micro.reading.cloud.homepage.service.IndexBooklistService;
import org.micro.reading.cloud.homepage.service.IndexPageConfigService;
import org.micro.reading.cloud.homepage.vo.IndexPageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月24日 15:30
 */
@Service
public class IndexPageConfigServiceImpl implements IndexPageConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPageConfigServiceImpl.class);

    @Autowired
    private IndexPageConfigMapper indexPageConfigMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private IndexBannerService indexBannerService;

    @Autowired
    private IndexBooklistService indexBooklistService;

    @Override
    public Result getIndexPageByType(Integer type, Integer page, Integer limit) {
        String key = RedisHomepageKey.getHomepageKey(type);
        // 精品页VO列表
        List<IndexPageVO> pageVOS = redisService.getHashListVal(key, page.toString(), IndexPageVO.class);
        if (Objects.nonNull(pageVOS)) {
            return ResultUtil.success(pageVOS);
        }
        // 获得精品页配置
        List<IndexPageConfig> pageConfigs = this.getIndexPageWithPaging(type, page, limit);
        if (CommonUtil.isEmpty(pageConfigs)) {
            LOGGER.warn("当前请求页没有配置项！");
            return ResultUtil.success(new ArrayList<>()).buildMessage("当前请求页没有配置项！");
        }
        pageVOS = new ArrayList<>(pageConfigs.size());
        for (int i = 0; i < pageConfigs.size(); i++) {
            IndexPageConfig pageConfig = pageConfigs.get(i);
            IndexPageVO vo = new IndexPageVO();
            BeanUtils.copyProperties(pageConfig, vo);
            // 模块是否有效
            boolean okFlag = true;
            switch (pageConfig.getItemType()) {
                case 1:
                    // 书单
                    vo.setBooklist(indexBooklistService.getIndexBooklistVO(pageConfig.getItemId(), null));
                    if (Objects.isNull(vo.getBooklist())) {
                        okFlag = false;
                    }
                    break;
                case 2:
                    // Banner
                    vo.setBanner(indexBannerService.getBannerVO(pageConfig.getItemId()));
                    if (Objects.isNull(vo.getBanner())) {
                        okFlag = false;
                    }
                    break;
                default:
                    break;
            }
            // 对应的模块值不为空，才进行添加到VO中
            if (okFlag) {
                pageVOS.add(vo);
            }
        }
        if (pageVOS.size() > 0) {
            // 缓存精品页
            redisService.setHashValExpire(key, page.toString(), pageVOS, RedisExpire.DAY);
        }
        return ResultUtil.success(pageVOS);
    }

    private List<IndexPageConfig> getIndexPageWithPaging(Integer type, Integer page, Integer limit) {
        if (page <= 0) {
            page = 1;
        }
        PageHelper.startPage(page, limit);
        return indexPageConfigMapper.findPageWithResult(type);
    }
}
