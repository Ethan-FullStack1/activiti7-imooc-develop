package com.activiti7.activiti7imoocdevelop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回定义
 *
 * @author debao.yang
 * @since 2022/7/27 10:13
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(0, "成功"),
    ERROR(1, "错误 ");

    private final int code;
    private final String desc;

}
