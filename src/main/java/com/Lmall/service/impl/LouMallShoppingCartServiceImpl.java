package com.Lmall.service.impl;

import com.Lmall.api.param.SaveCartItemParam;
import com.Lmall.api.param.UpdateCartItemParam;
import com.Lmall.api.vo.LouMallShoppingCartItemVO;
import com.Lmall.dao.MallGoodsMapper;
import com.Lmall.dao.MallShoppingCartItemMapper;
import com.Lmall.common.Constants;
import com.Lmall.common.LouMallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.entity.MallGoods;
import com.Lmall.entity.MallShoppingCartItem;
import com.Lmall.service.LouMallShoppingCartService;
import com.Lmall.util.BeanUtil;
import com.Lmall.util.PageQueryUtil;
import com.Lmall.util.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LouMallShoppingCartServiceImpl implements LouMallShoppingCartService {

    @Autowired
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    @Override
    public String saveMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        QueryWrapper<MallShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("goods_id",saveCartItemParam.getGoodsId()).eq("is_deleted",0);
        MallShoppingCartItem temp = mallShoppingCartItemMapper.selectOne(queryWrapper);
        if (temp != null) {
            //已存在则修改该记录
            LouMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        MallGoods mallGoods = mallGoodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        //商品为空
        if (mallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        QueryWrapper<MallShoppingCartItem> queryWrapper2 = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("is_deleted",0);
        int totalItem = Integer.parseInt(Long.toString(mallShoppingCartItemMapper.selectCount(queryWrapper)));
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        MallShoppingCartItem mallShoppingCartItem = new MallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, mallShoppingCartItem);
        mallShoppingCartItem.setUserId(userId);
        //保存记录
        if (mallShoppingCartItemMapper.insert(mallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        MallShoppingCartItem mallShoppingCartItemUpdate = mallShoppingCartItemMapper.selectById(updateCartItemParam.getCartItemId());
        if (mallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!mallShoppingCartItemUpdate.getUserId().equals(userId)) {
            LouMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        mallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        mallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartItemMapper.updateById(mallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public MallShoppingCartItem getCartItemById(Long shoppingCartItemId) {
        MallShoppingCartItem mallShoppingCartItem = mallShoppingCartItemMapper.selectById(shoppingCartItemId);
        if (mallShoppingCartItem == null) {
            LouMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId) {
        return mallShoppingCartItemMapper.deleteById(shoppingCartItemId) > 0;
    }

    @Override
    public List<LouMallShoppingCartItemVO> getMyShoppingCartItems(Long mallUserId) {
        List<LouMallShoppingCartItemVO> louMallShoppingCartItemVOS = new ArrayList<>();
        QueryWrapper<MallShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",mallUserId).last("limit "+Integer.toString(Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER));

        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectList(queryWrapper);
        return getLouMallShoppingCartItemVOS(louMallShoppingCartItemVOS, mallShoppingCartItems);
    }

    @Override
    public List<LouMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long mallUserId) {
        List<LouMallShoppingCartItemVO> louMallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            LouMallException.fail("购物项不能为空");
        }
        QueryWrapper<MallShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",mallUserId).eq("cart_item_ids",cartItemIds);
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(mallShoppingCartItems)) {
            LouMallException.fail("购物项不能为空");
        }
        if (mallShoppingCartItems.size() != cartItemIds.size()) {
            LouMallException.fail("参数异常");
        }
        return getLouMallShoppingCartItemVOS(louMallShoppingCartItemVOS, mallShoppingCartItems);
    }

    /**
     * 数据转换
     *
     * @param louMallShoppingCartItemVOS
     * @param mallShoppingCartItems
     * @return
     */
    private List<LouMallShoppingCartItemVO> getLouMallShoppingCartItemVOS(List<LouMallShoppingCartItemVO> louMallShoppingCartItemVOS, List<MallShoppingCartItem> mallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(mallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> mallGoodsIds = mallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<MallGoods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(mallGoodsIds);
            Map<Long, MallGoods> mallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(mallGoods)) {
                mallGoodsMap = mallGoods.stream().collect(Collectors.toMap(MallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (MallShoppingCartItem mallShoppingCartItem : mallShoppingCartItems) {
                LouMallShoppingCartItemVO louMallShoppingCartItemVO = new LouMallShoppingCartItemVO();
                BeanUtil.copyProperties(mallShoppingCartItem, louMallShoppingCartItemVO);
                if (mallGoodsMap.containsKey(mallShoppingCartItem.getGoodsId())) {
                    MallGoods mallGoodsTemp = mallGoodsMap.get(mallShoppingCartItem.getGoodsId());
                    louMallShoppingCartItemVO.setGoodsCoverImg(mallGoodsTemp.getGoodsCoverImg());
                    String goodsName = mallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    louMallShoppingCartItemVO.setGoodsName(goodsName);
                    louMallShoppingCartItemVO.setSellingPrice(mallGoodsTemp.getSellingPrice());
                    louMallShoppingCartItemVOS.add(louMallShoppingCartItemVO);
                }
            }
        }
        return louMallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<LouMallShoppingCartItemVO> louMallShoppingCartItemVOS = new ArrayList<>();
        QueryWrapper<MallShoppingCartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("is_deleted = 0",pageUtil);
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectList(queryWrapper);
        int total =Integer.parseInt(Long.toString(mallShoppingCartItemMapper.selectCount(queryWrapper)));
        PageResult pageResult = new PageResult(getLouMallShoppingCartItemVOS(louMallShoppingCartItemVOS, mallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
