/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.api;

import com.Lmall.api.param.SaveCartItemParam;
import com.Lmall.api.param.UpdateCartItemParam;
import com.Lmall.api.vo.LouMallShoppingCartItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.Lmall.common.Constants;
import com.Lmall.common.LouMallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.config.annotation.TokenToMallUser;
import com.Lmall.entity.MallUser;
import com.Lmall.entity.MallShoppingCartItem;
import com.Lmall.service.LouMallShoppingCartService;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;
import com.Lmall.util.Result;
import com.Lmall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "5.楼楼商城购物车相关接口")
@RequestMapping("/api/v1")
public class LouMallShoppingCartAPI {

    @Resource
    private LouMallShoppingCartService louMallShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<LouMallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser MallUser loginMallUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(louMallShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<LouMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUser loginMallUser) {
        return ResultGenerator.genSuccessResult(louMallShoppingCartService.getMyShoppingCartItems(loginMallUser.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                           @TokenToMallUser MallUser loginMallUser) {
        String saveResult = louMallShoppingCartService.saveMallCartItem(saveCartItemParam, loginMallUser.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                             @TokenToMallUser MallUser loginMallUser) {
        String updateResult = louMallShoppingCartService.updateMallCartItem(updateCartItemParam, loginMallUser.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{shoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateMallShoppingCartItem(@PathVariable("shoppingCartItemId") Long shoppingCartItemId,
                                             @TokenToMallUser MallUser loginMallUser) {
        MallShoppingCartItem cartItemById = louMallShoppingCartService.getCartItemById(shoppingCartItemId);
        if (!loginMallUser.getUserId().equals(cartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = louMallShoppingCartService.deleteById(shoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<LouMallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUser loginMallUser) {
        if (cartItemIds.length < 1) {
            LouMallException.fail("参数异常");
        }
        int priceTotal = 0;
        List<LouMallShoppingCartItemVO> itemsForSettle = louMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            LouMallException.fail("参数异常");
        } else {
            //总价
            for (LouMallShoppingCartItemVO louMallShoppingCartItemVO : itemsForSettle) {
                priceTotal += louMallShoppingCartItemVO.getGoodsCount() * louMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                LouMallException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
