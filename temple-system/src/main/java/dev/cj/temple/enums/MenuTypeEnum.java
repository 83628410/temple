package dev.cj.temple.enums;

import lombok.Getter;

@Getter
public enum MenuTypeEnum {

    CATALOG("C", "目录"), MENU("M", "菜单"), BUTTON("B", "按钮");

    /**
     * 数据库存储值
     */
    private final String value;

    /**
     * 友好名称
     */
    private final String label;

    MenuTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
