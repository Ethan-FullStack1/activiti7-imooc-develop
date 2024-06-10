package com.activiti7.activiti7imoocdevelop.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 后端返回数据类型
 *
 * @author debao.yang
 * @since 2022/7/27 10:18
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResponse {

    private Integer status;

    private String msg;

    private Object obj;

    public static AjaxResponse ajaxData(Integer status,
                                        String msg,
                                        Object obj) {
        return new AjaxResponse(status,msg,obj);
    }
}
