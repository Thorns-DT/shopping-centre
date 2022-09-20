/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service;

import com.Lmall.api.param.SaveCartItemParam;
import com.Lmall.api.param.UpdateCartItemParam;
import com.Lmall.api.vo.LouMallShoppingCartItemVO;
import com.Lmall.entity.MallShoppingCartItem;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;

import java.util.List;

public interface LouMallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 获取购物项详情
     *
     * @param shoppingCartItemId
     * @return
     */
    MallShoppingCartItem getCartItemById(Long shoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param shoppingCartItemId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param mallUserId
     * @return
     */
    List<LouMallShoppingCartItemVO> getMyShoppingCartItems(Long mallUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param mallUserId
     * @return
     */
    List<LouMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long mallUserId);

    /**
     * 我的购物车(分页数据)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
