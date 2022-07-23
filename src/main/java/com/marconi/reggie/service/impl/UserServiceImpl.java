package com.marconi.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marconi.reggie.common.RedisUtil;
import com.marconi.reggie.common.SmsUtils;
import com.marconi.reggie.mapper.UserMapper;
import com.marconi.reggie.pojo.DO.User;
import com.marconi.reggie.service.UserService;
import com.sun.prism.impl.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marconi
 * @date 2022/7/23
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean sendValidateCode(String phoneNumber) {

        Map<String, Object> res;
        try {
            res = SmsUtils.sendShortMessage(phoneNumber);
        } catch (Exception e) {
            log.error("用户手机号:{}获取验证码失败",phoneNumber);
            return false;
        }
        if ((Integer) res.get("status") == 1){
            String validateCode = (String) res.get("code");
            log.info("生成验证码成功:{}", validateCode);
            //将生成的验证码存入Redis中 5分钟内有效
            redisUtil.set(phoneNumber, validateCode, 1000*60*5);
            return true;
        }
        String message = (String) res.get("message");
        log.error("SMS发送失败:{}", message);
        return false;
    }

    @Override
    public boolean login(String phone, String code, HttpSession httpSession) {
        String trueCode = (String) redisUtil.get(phone);
        if (Objects.equals(code, trueCode)){
            //验证码验证通过 予以登录
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User one = this.getOne(queryWrapper);
            if (Objects.isNull(one)){
                //用户还没有注册 自动注册
                User user = new User();
                //TODO 默认值替换
                user.setName("叶春亭");
                user.setSex("男");
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            else{
                if (one.getStatus() == 0){
                    //禁用状态
                    one.setStatus(1);
                    this.updateById(one);
                }
                //已经存在用户 且没被禁用 正常登录
                Long id = one.getId();
                httpSession.setAttribute("userId", id);
            }
            return true;
        }
        return false;
    }
}
