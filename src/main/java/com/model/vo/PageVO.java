package com.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageVO<T> {
    protected List<T> records;
    protected long total;
    protected long size;
    protected long current;
}
