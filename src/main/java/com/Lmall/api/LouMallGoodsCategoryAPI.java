/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.api;

import com.Lmall.api.vo.LouMallIndexCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.Lmall.common.LouMallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.service.LouMallCategoryService;
import com.Lmall.util.Result;
import com.Lmall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "3.楼楼商城分类页面接口")
@RequestMapping("/api/v1")
public class LouMallGoodsCategoryAPI {

    @Resource
    private LouMallCategoryService louMallCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<LouMallIndexCategoryVO>> getCategories() {
        List<LouMallIndexCategoryVO> categories = louMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            LouMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
