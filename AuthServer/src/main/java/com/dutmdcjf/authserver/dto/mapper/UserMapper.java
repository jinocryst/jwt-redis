package com.dutmdcjf.authserver.dto.mapper;

import com.dutmdcjf.authserver.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface UserMapper {
    Map<String, Object> getUserBySignIn(@Param("email") String email, @Param("password") String password);

    User getUserById(@Param("idx") String id);
}
