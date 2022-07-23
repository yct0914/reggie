package com.marconi.reggie.controller;

import com.marconi.reggie.common.Response;
import com.marconi.reggie.pojo.DTO.UserDTO;
import com.marconi.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Marconi
 * @date 2022/7/23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/get_code")
    public Response getCode(@RequestBody Map<String,String> info){
        boolean send = userService.sendValidateCode(info.get("phone"));
        if (send){
            return new Response(200, "发送验证码成功!");
        }
        return new Response(400, "验证码发送失败!");
    }

    @PostMapping("/login")
    public Response login(@RequestBody UserDTO userDTO, HttpSession httpSession){
        String phone = userDTO.getPhone();
        String code = userDTO.getCode();
        boolean login = userService.login(phone, code, httpSession);
        if (login){
            return new Response(200,"登录成功!");
        }
        return new Response(400,"验证码错误!");
    }
}
