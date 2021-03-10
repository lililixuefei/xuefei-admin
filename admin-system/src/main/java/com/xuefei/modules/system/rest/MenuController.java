package com.xuefei.modules.system.rest;

import com.xuefei.modules.system.service.MenuService;
import com.xuefei.modules.system.service.dto.MenuDto;
import com.xuefei.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 菜单管理
 * @author: xuefei
 * @date: 2021/03/09 15:37
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<Object> buildMenus(){
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menuDtos = menuService.buildTree(menuDtoList);
        return new ResponseEntity<>(menuService.buildMenus(menuDtos), HttpStatus.OK);
    }
}
