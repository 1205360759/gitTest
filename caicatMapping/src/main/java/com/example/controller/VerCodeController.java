package com.example.controller;


import com.example.service.VerCodeService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class VerCodeController {

    @Autowired
    private VerCodeService verCodeService;

    @ResponseBody
    @GetMapping("/verCode")
    public void captcha(HttpServletResponse response, String uuid) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //获取图片验证码
        BufferedImage image = verCodeService.getCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @ResponseBody
    @GetMapping("/verCodeText")
    public String captcha(String uuid, String code) {
        boolean validate = verCodeService.validate(uuid, code);
        if (validate) {
            return "成功";
        } else {
            return "失败";
        }
    }
}
