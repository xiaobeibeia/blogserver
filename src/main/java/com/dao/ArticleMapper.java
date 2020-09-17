package com.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.model.entity.ArticlePO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ArticleMapper extends BaseMapper<ArticlePO> {
}
