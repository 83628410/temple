package dev.cj.temple.dto;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private Integer status;
    private Long[] menuIds;
}