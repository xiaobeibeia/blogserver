package com.service;

import cn.hutool.core.convert.*;
import cn.hutool.setting.*;
import com.baomidou.mybatisplus.core.conditions.query.*;
import com.dao.ArticleMapper;
import com.model.comm.BlogSetting;
import com.model.entity.ArticlePO;
import com.model.vo.BlogInfoVO;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CommService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Setting setting;

    public BlogInfoVO getBlogInfo() {
        BlogSetting blogSetting = BlogSetting.fromSetting(setting);
        BlogInfoVO vo = BlogInfoVO.fromBlogSetting(blogSetting);
        QueryWrapper<ArticlePO> wrapper = new QueryWrapper<>();
        wrapper.select("sum(views) as total_views");
        List<Map<String, Object>> maps = articleMapper.selectMaps(wrapper);
        int totalViews = 0;
        if (!maps.isEmpty()) {
            totalViews = Convert.toInt(maps.get(0).get("total_views"), 0);
        }
        Integer articleCount = articleMapper.selectCount(null);
        vo.setTotalViews(totalViews);
        vo.setArticleCount(articleCount);
        return vo;
    }
}
