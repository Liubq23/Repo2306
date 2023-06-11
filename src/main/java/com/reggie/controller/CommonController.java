package com.reggie.controller;

import com.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 * @author 刘秉奇
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie_take_out.path}")
    private String basePath;

    //原始文件名

/*返回文件名，供页面保存到数据库*/
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){/*跟前端的变量名保持一致*/
        log.info("获取文件:{}",file.toString());

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//得到后缀.jpg

        //UUID重新生成文件名（防重复）
        String fileName = UUID.randomUUID().toString() + suffix;

        //判断配置文件指向的路径是否存在
        File dir = new File(basePath + fileName);
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));/*通过配置文件application配置*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  Result.success(fileName);
    }

    /**
     * 文件下载，图片展示
     * @param name 文件名
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            //输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            //设置响应的是什么类型文件
            response.setContentType("image/jpg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            fileInputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
