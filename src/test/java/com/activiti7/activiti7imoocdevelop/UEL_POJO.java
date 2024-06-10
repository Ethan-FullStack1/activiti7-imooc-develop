package com.activiti7.activiti7imoocdevelop;

import lombok.*;

import java.io.Serializable;

/**
 * 启动流程实例的实体类
 *
 * @author debao.yang
 * @since 2022/7/20 11:32
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UEL_POJO implements Serializable {

    private String zhixingren;

    private String pay;

}
