package com.xuefei.modules.system.repository;

import com.xuefei.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:21
 */

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     * @param email 邮箱
     * @return /
     */
    User findByEmail(String email);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    User findByPhone(String phone);
}
