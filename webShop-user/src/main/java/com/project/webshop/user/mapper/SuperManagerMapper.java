package com.project.webshop.user.mapper;

import common.model.user.SuperManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SuperManagerMapper {

    SuperManager findByName(@Param("account") String account);

}
