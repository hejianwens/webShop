package com.project.webshop.user.mapper;

import common.model.user.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {
    public Customer findCustomerByName(Customer customer);

    public void insert(Customer customer);
}
