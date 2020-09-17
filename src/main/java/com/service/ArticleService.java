package com.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.*;
import com.dao.ArticleMapper;
import com.model.dto.ArticleDTO;
import com.model.entity.ArticlePO;
import com.model.vo.ArticleVO;
import com.model.vo.PageVO;
import com.model.vo.TimelineVO;
import com.utils.BlogUtils;
import com.utils.enums.ErrorInfoEnum;
import com.utils.exception.BlogException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public PageVO<ArticleVO> getArticles(int page, int limit) {
        QueryWrapper<ArticlePO> qw = new QueryWrapper<>();
       // 因为我们分页查询的时候，通常不需要文章内容，所以这里使用
        // QueryWrapper 的 select 方法，过滤了 content 字段。
        qw.select(ArticlePO.class, i -> !"content".equals(i.getColumn()));
        Page<ArticlePO> res = articleMapper.selectPage(new Page<>(page, limit), qw);
        List<ArticleVO> articleVOS = res.getRecords().stream()
                .map(ArticleVO::fromArticlePO)
                .collect(Collectors.toList());
        PageVO<ArticleVO> pageVO = PageVO.<ArticleVO>builder()
                .records(articleVOS.isEmpty() ? new ArrayList<>() : articleVOS)
                .total(res.getTotal())
                .current(res.getCurrent())
                .size(res.getSize())
                .build();
        return pageVO;
    }

    public String insArticle(ArticleDTO articleDTO) {
        ArticlePO po = articleDTO.toArticlePO(false);
        //IdUtil 为 Hutool 工具类中一个id生成工具，这里的 ObjectId
        // 是 MongoDB 数据库的一种唯一ID生成策略，是UUID version1的变种
        String id = IdUtil.objectId();
        po.setId(id);
        articleMapper.insert(po);
        return id;
    }

 /*   public ArticleVO findById(String id) {
        ArticlePO articlePO = articleMapper.selectById(id);
        if (Objects.isNull(articlePO)) {
            throw new BlogException(ErrorInfoEnum. INVALID_ID);
        }
        return ArticleVO.fromArticlePO(articlePO);
    }
*/
 //添加浏览量加一的功能
    @Transactional(rollbackFor = Exception.class)
    public ArticleVO findById(String id) {
        ArticlePO articlePO = articleMapper.selectById(id);
        if (Objects.isNull(articlePO)) {
            throw new BlogException(ErrorInfoEnum.INVALID_ID);
        }
        articlePO.setViews(articlePO.getViews() + 1);
        articleMapper.updateById(articlePO);
        return ArticleVO.fromArticlePO(articlePO);
    }

    public void deleteArticle(String id) {
        int i = articleMapper.deleteById(id);
        if (i <= 0) {
            throw new BlogException(ErrorInfoEnum.INVALID_ID);
        }
    }

    public void updateArticle(ArticleDTO articleDTO, String id) {
        ArticlePO dbArticle = articleMapper.selectById(id);
        if (Objects.isNull(dbArticle)) {
            throw new BlogException(ErrorInfoEnum.INVALID_ID);
        }
        ArticlePO articlePO = articleDTO.toArticlePO(true);
        articlePO.setId(id);
        articleMapper.updateById(articlePO);
    }

    public List<TimelineVO> timeline() {
        ArrayList<TimelineVO> res = new ArrayList<>();
        QueryWrapper<ArticlePO> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "gmt_create");
        List<Map<String, Object>> maps = articleMapper.selectMaps(wrapper);
        maps.stream().map(m -> TimelineVO.Item.builder()
                .id((String) m.get("id"))
                .gmtCreate(BlogUtils.formatDatetime((Long) m.get("gmt_create")))
                .title((String) m.get("title"))
                .build())
                .collect(Collectors.groupingBy(item -> {
                    String[] arr = item.getGmtCreate().split("-");
                    String year = arr[0];
                    return year;
                })).forEach((k, v) -> res.add(TimelineVO.builder().year(k).items(v).build()));
        res.sort(Comparator.comparing(TimelineVO::getYear).reversed());
        // log.info("timeline list : {}", JSONUtil.toJsonStr(res));
        return res;
        //将查询到的 Map 转换为 Item（TimelineVO 的内部类） 对象
        //从 item 对象的 gmtCreate 属性中获取年份，根据年份分组
        //遍历每个分组，创建 TimelineVO 对象，加入集合
    }
}