/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.api;

import com.Lmall.api.vo.IndexInfoVO;
import com.Lmall.api.vo.LouMallIndexCarouselVO;
import com.Lmall.api.vo.LouMallIndexConfigGoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.Lmall.common.Constants;
import com.Lmall.common.IndexConfigTypeEnum;
import com.Lmall.service.LouMallCarouselService;
import com.Lmall.service.LouMallIndexConfigService;
import com.Lmall.util.Result;
import com.Lmall.util.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "1.楼楼商城首页接口")
@RequestMapping("/api/v1")
public class LouMallIndexAPI {

    @Resource
    private LouMallCarouselService louMallCarouselService;

    @Resource
    private LouMallIndexConfigService louMallIndexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<LouMallIndexCarouselVO> carousels = louMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<LouMallIndexConfigGoodsVO> hotGoodses = louMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<LouMallIndexConfigGoodsVO> newGoodses = louMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<LouMallIndexConfigGoodsVO> recommendGoodses = louMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
