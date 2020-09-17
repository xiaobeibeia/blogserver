package com.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;
import com.model.comm.BlogSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

//将 setting 对象加入 Spring 的容器中
@Slf4j
@Configuration
public class BlogSettingConfig {

    @Value("${setting.path}")
    private String settingFilePath;

    @Bean
    public Setting setting() {
        File file = new File(settingFilePath);
        Setting setting;
        //这里如果没有配置文件的话，我们在相应位置新建一个配置文件，并给定默认值
        if (!file.exists()) {
            setting = new Setting(file, CharsetUtil.CHARSET_UTF_8, false);
            setting.set("title", "竹林客栈");
            setting.set("desc", "不求风度翩翩，但求风骨立世；没有书生意气，也要为义为仁。");
            setting.set("covers", "https://tvax1.sinaimg.cn/mw1024/bfe05ea9ly1fxgu8jys3fj21hc0u0k0j.jpg,https://tvax1.sinaimg.cn/large/bfe05ea9ly1fxgunx09dtj21hc0u0q81.jpg,https://tvax1.sinaimg.cn/large/bfe05ea9ly1fxgv2t92yyj21hc0u0qb9.jpg");
            setting.set("avatar", "https://portrait.gitee.com/uploads/avatars/user/772/2317865_qianyucc_1589023575.png");
            setting.set("nickname", "竹林笔墨");
            setting.store(file.getAbsolutePath());
        }
        setting = new Setting(file, CharsetUtil.CHARSET_UTF_8, false);
        // 开启自动加载，当配置文件修改的时候，不需要重启项目就可以获取到修改后的内容
        setting.autoLoad(true);
        log.info(BlogSetting.fromSetting(setting).toString());
        return setting;
    }
}