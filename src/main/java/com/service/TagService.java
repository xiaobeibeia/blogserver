package com.service;

import com.baomidou.mybatisplus.core.conditions.query.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dao.ArticleMapper;
import com.model.entity.ArticlePO;
import com.model.vo.ArticleVO;
import com.model.vo.PageVO;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
@Slf4j
public class TagService {
    @Autowired
    private ArticleMapper articleMapper;

    /***
     * 查询所有标签
     * @return 标签集合
     */
    //Set 用于去重
    //flatMap 方法可以将多个流合并成一个流
    public Set<String> getAllTags() {
        QueryWrapper<ArticlePO> wrapper = new QueryWrapper<>();
        wrapper.select("tags");
        List<ArticlePO> articles = articleMapper.selectList(wrapper);
        Set<String> tags = articles.stream()
                .map(ArticlePO::getTags)
                .flatMap(s -> Arrays.stream(s.split(",")))
                .collect(Collectors.toSet());
        log.info("tags : {}", tags);
        return tags.isEmpty() ? new HashSet<>() : tags;
    }

    public PageVO<ArticleVO> getArticleByTag(String tagName, Integer page, Integer limit) {
        QueryWrapper<ArticlePO> wrapper = new QueryWrapper<>();
        wrapper.select(ArticlePO.class, i -> !"content".equals(i.getColumn()))
                .like("tags", tagName);
        //因为查找文章列表的时候通常就不需要看到文章内容，这里排除 content 字段
        //这里其实使用 like 来查询时存在问题的，在一个标签内容包含另一个标签的时候会出现问题，
        // 但是由于出现这种情况的概率不大，这里就使用 like 了，有更好想法的朋友可以自行优化
        Page<ArticlePO> res = articleMapper.selectPage(new Page<>(page, limit), wrapper);
        List<ArticleVO> articleVOS = res.getRecords().stream()
                .map(ArticleVO::fromArticlePO)
                .collect(Collectors.toList());
        PageVO<ArticleVO> pv = PageVO.<ArticleVO>builder()
                .records(articleVOS.isEmpty() ? new ArrayList<>() : articleVOS)
                .total(res.getTotal())
                .current(res.getCurrent())
                .size(res.getSize())
                .build();
        return pv;
    }
}