package com.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TimelineVO {
    private String year;
    private List<Item> items;

    @Data
    @Builder
    public static class Item {
        private String id;
        private String gmtCreate;
        private String title;
    }
}
