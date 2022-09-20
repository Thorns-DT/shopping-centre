/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service.impl;

import com.Lmall.api.vo.LouMallIndexConfigGoodsVO;
import com.Lmall.dao.IndexConfigMapper;
import com.Lmall.dao.MallGoodsMapper;
import com.Lmall.entity.IndexConfig;
import com.Lmall.entity.MallGoods;
import com.Lmall.service.LouMallIndexConfigService;
import com.Lmall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LouMallIndexConfigServiceImpl implements LouMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private MallGoodsMapper goodsMapper;

    @Override
    public List<LouMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<LouMallIndexConfigGoodsVO> louMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<MallGoods> mallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            louMallIndexConfigGoodsVOS = BeanUtil.copyList(mallGoods, LouMallIndexConfigGoodsVO.class);
            for (LouMallIndexConfigGoodsVO louMallIndexConfigGoodsVO : louMallIndexConfigGoodsVOS) {
                String goodsName = louMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = louMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    louMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    louMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return louMallIndexConfigGoodsVOS;
    }
}
