/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service;

import com.Lmall.entity.MallGoods;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;

public interface LouMallGoodsService {

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    MallGoods getMallGoodsById(Long id);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchMallGoods(PageQueryUtil pageUtil);
}
