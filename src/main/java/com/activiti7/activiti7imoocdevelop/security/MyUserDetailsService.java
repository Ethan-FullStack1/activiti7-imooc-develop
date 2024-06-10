package com.activiti7.activiti7imoocdevelop.security;

import com.activiti7.activiti7imoocdevelop.mapper.UserInfoBeanMapper;
import com.activiti7.activiti7imoocdevelop.pojo.UserInfoBean;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 我的用户服务
 *
 * @author 多宝
 * @since 2022/7/24 19:50
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyUserDetailsService implements UserDetailsService {

    private final UserInfoBeanMapper userInfoBeanMapper;

    @Override
    @SneakyThrows
    @SuppressWarnings("all")
    public UserDetails loadUserByUsername(
            String username) {
        /*String password = passwordEncoder().encode("111");
        // 没有做任何数据库校验
        return new User(username, password,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(
                                "ROLE_ACTIVITI_USER"));*/

        // 读取数据库判断用户
        // 如果用户是null，抛出异常
        // 返回用户信息

        // username = Constants.USER_BAJIE;

        UserInfoBean userInfoBean =
                userInfoBeanMapper.selectByUserName(username);
        if (Objects.isNull(userInfoBean)) {
            throw new UsernameNotFoundException("数据库中无此用户");
        }

        return userInfoBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
