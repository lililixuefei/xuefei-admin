package com.xuefei.modules.security.service;

import com.xuefei.exception.BadRequestException;
import com.xuefei.exception.EntityNotFoundException;
import com.xuefei.modules.security.config.bean.LoginProperties;
import com.xuefei.modules.security.service.dto.JwtUserDto;
import com.xuefei.modules.system.service.UserService;
import com.xuefei.modules.system.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:40
 */
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 用户信息缓存
     */
    static Map<String, JwtUserDto> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtUserDto jwtUserDto = null;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache.get(username);
            searchDb = false;
        }
        if (searchDb) {
            UserDto user;
            try {
                user = userService.findByName(username);
                System.out.println(Objects.isNull(user));
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (user == null) {
                throw new UsernameNotFoundException("");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                jwtUserDto = new JwtUserDto(
                        user
                );
                userDtoCache.put(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}

