/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.dao;

import com.Lmall.entity.MallShoppingCartItem;
import com.Lmall.util.PageQueryUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallShoppingCartItemMapper extends BaseMapper<MallShoppingCartItem> {
//    int deleteByPrimaryKey(Long cartItemId);
//
//    int insert(MallShoppingCartItem record);
//
//    int insertSelective(MallShoppingCartItem record);
//
//    MallShoppingCartItem selectByPrimaryKey(Long cartItemId);
//
//    MallShoppingCartItem selectByUserIdAndGoodsId(@Param("mallUserId") Long mallUserId, @Param("goodsId") Long goodsId);
//
//    List<MallShoppingCartItem> selectByUserId(@Param("mallUserId") Long mallUserId, @Param("number") int number);
//
//    List<MallShoppingCartItem> selectByUserIdAndCartItemIds(@Param("mallUserId") Long mallUserId, @Param("cartItemIds") List<Long> cartItemIds);
//
//    int selectCountByUserId(Long mallUserId);
//
//    int updateByPrimaryKeySelective(MallShoppingCartItem record);
//
//    int updateByPrimaryKey(MallShoppingCartItem record);
//
//    int deleteBatch(List<Long> ids);
//
//    List<MallShoppingCartItem> findMyCartItems(PageQueryUtil pageUtil);
//
//    int getTotalMyCartItems(PageQueryUtil pageUtil);
}