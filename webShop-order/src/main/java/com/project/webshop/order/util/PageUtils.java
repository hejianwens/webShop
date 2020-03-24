package com.project.webshop.order.util;


import com.github.pagehelper.PageInfo;
import common.model.PageResult;
import common.model.QueryParams;

/**
 * .*.@author.zhangwensen
 * .*.@date.2019/12/11
 * .
 */
public class PageUtils {

    /**
     * 将分页信息封装到统一的接口
     * @param pageRequest
     * @param pageInfo
     * @return
     */
    public static PageResult getPageResult(QueryParams pageRequest, PageInfo<?> pageInfo) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotalSize(pageInfo.getTotal());
        pageResult.setTotalPages(pageInfo.getPages());
        pageResult.setContent(pageInfo.getList());
        return pageResult;
    }
}
