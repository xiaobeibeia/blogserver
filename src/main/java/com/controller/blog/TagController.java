package com.controller.blog;

import com.model.comm.Results;
import com.model.vo.ArticleVO;
import com.model.vo.PageVO;
import com.service.TagService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation("获取所有的标签")
    @GetMapping("/tags")
    public Results<Set<String>> getTags() {
        return Results.ok(tagService.getAllTags());
    }

    //标签名称使用路径参数
    //为适应文章数量比较多的情况，这里提供分页功能
    @GetMapping("/tag/{name}")
    @ApiOperation("根据标签查询文章集合")
    @ApiImplicitParam(name = "name", value = "标签名称", required = true, dataType = "String", paramType = "path")
    public Results<PageVO<ArticleVO>> getArticle(
            @PathVariable("name") String tagName,
            @ApiParam("页码")
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam("每页存放的记录数")
            @RequestParam(required = false, defaultValue = "5") Integer limit) {
        PageVO<ArticleVO> pv = tagService.getArticleByTag(tagName, page, limit);
        return Results.ok(pv);
    }
}
