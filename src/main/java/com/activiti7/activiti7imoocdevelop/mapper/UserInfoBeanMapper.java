package com.activiti7.activiti7imoocdevelop.mapper;

import com.activiti7.activiti7imoocdevelop.pojo.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * 查询用户信息的Mapper
 *
 * @author 多宝
 * @since 2022/7/24 20:24
 */
@Mapper
@Component
public interface UserInfoBeanMapper {

    /**
     * 根据用户名查询用户信息
     *     * @param userName 用户名
     * @return com.activiti7.activiti7imoocdevelop.pojo.UserInfoBean
     * @author 多宝
     * @since 2022/7/24 20:26
     */
    @Select("select * from user where username = #{username}")
    @SuppressWarnings("SpellCheckingInspection")
    UserInfoBean selectByUserName(@Param("username") String userName);

}
