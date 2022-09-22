/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.service.impl;

import com.Lmall.api.vo.LouMallUserAddressVO;
import com.Lmall.dao.MallUserAddressMapper;
import com.Lmall.common.LouMallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.entity.MallUserAddress;
import com.Lmall.service.LouMallUserAddressService;
import com.Lmall.util.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LouMallUserAddressServiceImpl implements LouMallUserAddressService {

    @Autowired
    private MallUserAddressMapper userAddressMapper;

    @Override
    public List<LouMallUserAddressVO> getMyAddresses(Long userId) {
        QueryWrapper<MallUserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<MallUserAddress> myAddressList = userAddressMapper.selectList(wrapper);
        List<LouMallUserAddressVO> louMallUserAddressVOS = BeanUtil.copyList(myAddressList, LouMallUserAddressVO.class);
        return louMallUserAddressVOS;
    }

    @Override
    @Transactional
    public Boolean saveUserAddress(MallUserAddress mallUserAddress) {
        Date now = new Date();
        QueryWrapper<MallUserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("address_id",mallUserAddress.getUserId()).eq("default_flag",1).eq("is_deleted",0).apply("limit 1");
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //添加默认地址，需要将原有的默认地址修改掉
            MallUserAddress defaultAddress = userAddressMapper.selectOne(wrapper);
            if (defaultAddress != null) {
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateById(defaultAddress);
                if (updateResult < 1) {
                    //未更新成功
                    LouMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        return userAddressMapper.insert(mallUserAddress) > 0;
    }

    @Override
    public Boolean updateMallUserAddress(MallUserAddress mallUserAddress) {
        MallUserAddress tempAddress = getMallUserAddressById(mallUserAddress.getAddressId());
        Date now = new Date();
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //修改为默认地址，需要将原有的默认地址修改掉
            QueryWrapper<MallUserAddress> wrapper = new QueryWrapper<>();
            wrapper.eq("address_id",mallUserAddress.getUserId()).eq("default_flag",1).eq("is_deleted",0).apply("limit 1");
            MallUserAddress defaultAddress = userAddressMapper.selectOne(wrapper);
            if (defaultAddress != null && !defaultAddress.getAddressId().equals(tempAddress)) {
                //存在默认地址且默认地址并不是当前修改的地址
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateById(defaultAddress);
                if (updateResult < 1) {
                    //未更新成功
                    LouMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        mallUserAddress.setUpdateTime(now);
        return userAddressMapper.updateById(mallUserAddress) > 0;
    }

    @Override
    public MallUserAddress getMallUserAddressById(Long addressId) {

        MallUserAddress mallUserAddress = userAddressMapper.selectById(addressId);
        if (mallUserAddress == null) {
            LouMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallUserAddress;
    }

    @Override
    public MallUserAddress getMyDefaultAddressByUserId(Long userId) {
        QueryWrapper<MallUserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("address_id",userId).eq("default_flag",1).eq("is_deleted",0).apply("limit 1");

        return userAddressMapper.selectOne(wrapper);
    }

    @Override
    public Boolean deleteById(Long addressId) {
        return userAddressMapper.deleteById(addressId) > 0;
    }
}
