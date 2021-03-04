package com.xuefei.modules.system.service.impl;

import com.xuefei.exception.EntityNotFoundException;
import com.xuefei.modules.system.domain.User;
import com.xuefei.modules.system.repository.UserRepository;
import com.xuefei.modules.system.service.UserQueryCriteria;
import com.xuefei.modules.system.service.UserService;
import com.xuefei.modules.system.service.dto.UserDto;
import com.xuefei.modules.system.service.mapstruct.UserMapper;
import com.xuefei.utils.PageUtil;
import com.xuefei.utils.QueryHelp;
import com.xuefei.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 11:25
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Object queryAll(UserQueryCriteria criteria, Pageable pageable) {
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    public List<UserDto> queryAll(UserQueryCriteria criteria) {
        List<User> users = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return userMapper.toDto(users);
    }

    @Override
    @Cacheable(key = "'id:' + #p0")
    @Transactional(rollbackFor = Exception.class)
    public UserDto findById(long id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(), "User", "id", id);
        return userMapper.toDto(user);
    }

    @Override
    public void create(User resources) {

    }

    @Override
    public void update(User resources) {

    }

    @Override
    public void delete(Set<Long> ids) {

    }


    @Override
    public UserDto findByName(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new EntityNotFoundException(User.class, "name", userName);
        } else {
            return userMapper.toDto(user);
        }
    }

    @Override
    public void updatePass(String username, String encryptPassword) {

    }

    @Override
    public Map<String, String> updateAvatar(MultipartFile file) {
        return null;
    }

    @Override
    public void updateEmail(String username, String email) {

    }

}

