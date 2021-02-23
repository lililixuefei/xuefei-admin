package com.xuefei.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import com.wf.captcha.base.Captcha;
import com.xuefei.config.RsaProperties;
import com.xuefei.exception.BadRequestException;
import com.xuefei.modules.security.config.bean.LoginCodeEnum;
import com.xuefei.modules.security.config.bean.LoginProperties;
import com.xuefei.modules.security.config.bean.SecurityProperties;
import com.xuefei.modules.security.security.TokenProvider;
import com.xuefei.modules.service.OnlineUserService;
import com.xuefei.modules.service.dto.AuthUserDto;
import com.xuefei.utils.RedisUtils;
import com.xuefei.utils.RsaUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: 授权、根据token获取用户详细信息
 * @author: xuefei
 * @date: 2020/12/04 12:59
 */
@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;

    private final RedisUtils redisUtils;

    @Resource
    private LoginProperties loginProperties;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDto authUser, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        return ResponseEntity.ok("authInfo");
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok("SecurityUtils.getCurrentUser()");
    }

    @RequestMapping(path = "/code")
    public ResponseEntity<Object> getCode(){
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}