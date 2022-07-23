package com.marconi.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.marconi.reggie.pojo.DO.User;

import javax.servlet.http.HttpSession;

/**
 * @author Marconi
 * @date 2022/7/23
 */
public interface UserService extends IService<User> {

    /**
     * 获取验证码
     * @param phoneNumber 手机号
     * @return 是否获取成功
     */
    boolean sendValidateCode(String phoneNumber);

    /**
     * 用户登录接口
     * @param number
     * @param code
     * @return
     */
    boolean login(String number, String code, HttpSession httpSession);
}
