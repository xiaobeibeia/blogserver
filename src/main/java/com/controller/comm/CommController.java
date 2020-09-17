package com.controller.comm;

import com.model.comm.Results;
import com.model.vo.BlogInfoVO;
import com.service.CommService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("通用接口")
@RestController
public class CommController {

    @Autowired
    private CommService commService;

    @ApiOperation("检查服务端是否正常")
    @GetMapping("/ping")
    public Results ping() {
        return Results.ok("欢迎访问QBlog API", null);
    }

    @ApiOperation("获取博客信息")
    @GetMapping("/info")
    public Results<BlogInfoVO> info() {
        BlogInfoVO blogInfo = commService.getBlogInfo();
        return Results.ok(blogInfo);
    }

}

