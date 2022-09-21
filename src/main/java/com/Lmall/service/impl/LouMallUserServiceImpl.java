package com.Lmall.service.impl;
import com.Lmall.api.param.MallUserUpdateParam;
import com.Lmall.dao.MallUserMapper;
import com.Lmall.dao.MallUserTokenMapper;
import com.Lmall.util.MD5Util;
import com.Lmall.common.Constants;
import com.Lmall.common.LouMallException;
import com.Lmall.common.ServiceResultEnum;
import com.Lmall.entity.MallUser;
import com.Lmall.entity.MallUserToken;
import com.Lmall.service.LouMallUserService;
import com.Lmall.util.NumberUtil;
import com.Lmall.util.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;


@Service
public class LouMallUserServiceImpl extends ServiceImpl<MallUserMapper,MallUser> implements LouMallUserService {
    @Autowired
    private MallUserMapper mallUserMapper;
    @Autowired
    private MallUserTokenMapper mallUserTokenMapper;

    @Override
    public String register(String loginName, String password) {

        QueryWrapper<MallUser> wrapper=new QueryWrapper<>();
        wrapper.eq("nic_kName",loginName).eq("password_md5",password);
        if (mallUserMapper.selectOne(wrapper) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        registerUser.setIntroduceSign(Constants.USER_INTRO);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insert(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        QueryWrapper<MallUser> wrapper=new QueryWrapper<>();
        wrapper.eq("login_name",loginName).eq("password_md5",passwordMD5);
        MallUser user = mallUserMapper.selectOne(wrapper);
        if (user != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
            MallUserToken mallUserToken = mallUserTokenMapper.selectById(user.getUserId());
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (mallUserToken == null) {
                mallUserToken = new MallUserToken();
                mallUserToken.setUserId(user.getUserId());
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (mallUserTokenMapper.insert(mallUserToken) > 0) {
                    //新增成功后返回
                    return token;
                }
            } else {
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //更新
                QueryWrapper<MallUserToken> wrapper2=new QueryWrapper<>();
                wrapper2.eq("user_id",user.getUserId());
                if (mallUserTokenMapper.update(mallUserToken,wrapper2) > 0) {
                    //修改成功后返回
                    return token;
                }
            }

        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    /**
     * 获取token值
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
        QueryWrapper<MallUser> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        MallUser user = mallUserMapper.selectOne(wrapper);
        if (user == null) {
            LouMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(mallUser.getNickName());
        user.setPasswordMd5(mallUser.getPasswordMd5());
        user.setIntroduceSign(mallUser.getIntroduceSign());
        if (mallUserMapper.updateById(user) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean logout(Long userId) {
        return mallUserTokenMapper.deleteById(userId) > 0;
    }
}
