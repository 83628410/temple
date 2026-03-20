package dev.cj.temple.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    /*
     * 总记录数 
     */
    private Long total;
    /**
     * 列表数据
     */
    private List<?> list;
}
