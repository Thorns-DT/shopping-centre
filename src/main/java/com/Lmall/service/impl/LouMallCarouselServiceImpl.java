/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service.impl;

import com.Lmall.api.vo.LouMallIndexCarouselVO;
import com.Lmall.dao.CarouselMapper;
import com.Lmall.entity.Carousel;
import com.Lmall.service.LouMallCarouselService;
import com.Lmall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class LouMallCarouselServiceImpl implements LouMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<LouMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<LouMallIndexCarouselVO> louMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            louMallIndexCarouselVOS = BeanUtil.copyList(carousels, LouMallIndexCarouselVO.class);
        }
        return louMallIndexCarouselVOS;
    }
}
