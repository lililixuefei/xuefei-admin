package com.xuefei.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xuefei.exception.BadRequestException;
import com.xuefei.exception.EntityExistException;
import com.xuefei.modules.system.domain.Menu;
import com.xuefei.modules.system.domain.Role;
import com.xuefei.modules.system.domain.User;
import com.xuefei.modules.system.repository.RoleRepository;
import com.xuefei.modules.system.repository.UserRepository;
import com.xuefei.modules.system.service.RoleService;
import com.xuefei.modules.system.service.dto.RoleDto;
import com.xuefei.modules.system.service.dto.RoleQueryCriteria;
import com.xuefei.modules.system.service.dto.RoleSmallDto;
import com.xuefei.modules.system.service.dto.UserDto;
import com.xuefei.modules.system.service.mapstruct.RoleMapper;
import com.xuefei.modules.system.service.mapstruct.RoleSmallMapper;
import com.xuefei.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: xuefei
 * @date: 2021/03/09 15:45
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleSmallMapper roleSmallMapper;
    private final RedisUtils redisUtils;
    private final UserRepository userRepository;
//    private final UserCacheClean userCacheClean;

    @Override
    public List<RoleDto> queryAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "level");
        return roleMapper.toDto(roleRepository.findAll(sort));
    }

    @Override
    public List<RoleDto> queryAll(RoleQueryCriteria criteria) {
        return roleMapper.toDto(roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public Object queryAll(RoleQueryCriteria criteria, Pageable pageable) {
        Page<Role> page = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(roleMapper::toDto));
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public RoleDto findById(long id) {
        Role role = roleRepository.findById(id).orElseGet(Role::new);
        ValidationUtil.isNull(role.getId(), "Role", "id", id);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Role resources) {
        if (roleRepository.findByName(resources.getName()) != null) {
            throw new EntityExistException(Role.class, "username", resources.getName());
        }
        roleRepository.save(resources);
    }

    @Override
    public void update(Role resources) {

    }

    @Override
    public void delete(Set<Long> ids) {

    }

    @Override
    public List<RoleSmallDto> findByUsersId(Long id) {
        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUserId(id)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void untiedMenu(Long menuId) {
        // 更新菜单
        roleRepository.untiedMenu(menuId);
    }

//    @Override
//    public List<RoleSmallDto> findByUsersId(Long id) {
//        return roleSmallMapper.toDto(new ArrayList<>(roleRepository.findByUserId(id)));
//    }

    @Override
    public Integer findByRoles(Set<Role> roles) {
        if (roles.size() == 0) {
            return Integer.MAX_VALUE;
        }
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(findById(role.getId()));
        }
        return Collections.min(roleDtos.stream().map(RoleDto::getLevel).collect(Collectors.toList()));
    }

    @Override
    public void updateMenu(Role resources, RoleDto roleDTO) {

    }

    @Override
    @Cacheable(key = "'auth:' + #p0.id")
    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        Set<Role> roles = roleRepository.findByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                .map(Menu::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public void verification(Set<Long> ids) {

    }

    @Override
    public void download(List<RoleDto> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDto role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public List<Role> findInMenuId(List<Long> menuIds) {
        return roleRepository.findInMenuId(menuIds);
    }


}

