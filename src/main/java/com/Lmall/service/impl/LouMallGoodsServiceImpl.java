/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service.impl;

import com.Lmall.api.vo.LouMallSearchGoodsVO;
import com.Lmall.dao.MallGoodsMapper;
import com.Lmall.service.LouMallGoodsService;
import com.Lmall.entity.MallGoods;
import com.Lmall.util.BeanUtil;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LouMallGoodsServiceImpl implements LouMallGoodsService {

    @Autowired
    private MallGoodsMapper goodsMapper;

    @Override
    public MallGoods getMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult searchMallGoods(PageQueryUtil pageUtil) {
        List<MallGoods> goodsList = goodsMapper.findMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalMallGoodsBySearch(pageUtil);
        List<LouMallSearchGoodsVO> louMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            louMallSearchGoodsVOS = BeanUtil.copyList(goodsList, LouMallSearchGoodsVO.class);
            for (LouMallSearchGoodsVO louMallSearchGoodsVO : louMallSearchGoodsVOS) {
                String goodsName = louMallSearchGoodsVO.getGoodsName();
                String goodsIntro = louMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    louMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    louMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(louMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
