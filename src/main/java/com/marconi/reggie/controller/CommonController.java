package com.marconi.reggie.controller;

import com.marconi.reggie.common.Response;
import com.marconi.reggie.config.properties.CommonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 *用于处理文件上传等请求
 * @author Marconi
 * @date 2022/7/16
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Autowired
    CommonProperties commonProperties;

    /**
     * 上传图片文件
     * @param file 接收的form-data文件
     * @return
     */
    @PostMapping("/upload")
    public Response uploadImage(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String directory = commonProperties.getImageDirectory();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString()+suffix;
        File dir = new File(directory);
        if (!dir.exists()){
            dir.mkdir();
        }
        File image = new File(directory, filename);
        if (image.exists()){
            return new Response(400,"该文件已经存在!");
        }
        try {
            file.transferTo(image);
            return new Response(200,"上传成功!",filename);
        }catch (IOException e){
            e.printStackTrace();
            return new Response(500,"上传图片失败！");
        }
    }

    @GetMapping("/download")
    public Response downloadImage(HttpServletResponse response, String name){
        String directory = commonProperties.getImageDirectory();
        File file = new File(directory, name);
        int len;
        byte[] bytes = new byte[1024];
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Response(200,"success");
    }
}
