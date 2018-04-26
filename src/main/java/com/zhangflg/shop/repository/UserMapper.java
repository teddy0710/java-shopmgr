package com.zhangflg.shop.repository;

import com.zhangflg.shop.bean.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * UserMapper 数据访问类
 *
 * @author xlei @qq 251425887 @tel 13352818008
 * @version 1.0
 * @Email dlei0009@163.com
 * @date 2018-04-26 11:54:00
 */
@Repository
public interface UserMapper {

    @Select("select * from ec_user where login_name=#{loginName}")
    User login(String loginName);
}