package dev.cj.temple.dto;

import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Long parentId;
    private Integer orderNum;
    private Integer status;
}