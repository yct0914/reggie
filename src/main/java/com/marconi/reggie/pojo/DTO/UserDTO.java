package com.marconi.reggie.pojo.DTO;

import com.marconi.reggie.pojo.DO.User;
import lombok.Data;

/**
 * @author Marconi
 * @date 2022/7/23
 */
@Data
public class UserDTO extends User {

    private String code;
}
