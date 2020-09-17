package com.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QiniuUploadInfoVO {
    private String token;
    private String imageUrl;
    private String uploadUrl;
}
