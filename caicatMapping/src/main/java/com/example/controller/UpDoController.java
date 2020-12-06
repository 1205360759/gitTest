package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Encoder;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")
public class UpDoController {

    //下载路径
    String DOWNLOAD_PATH = "/collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile/download";
    //上传路径
    String UPLOAD_PAHT = "/collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile/upload";
    //模板下载路径
    String TEMPLATE_PAHT = "/collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile1/samples";

    //打印log日志
    private static final Log logger = LogFactory.getLog(UpDoController.class);


    //获取下载文件夹下的文件
    @RequestMapping(value = "/getFiles")
    public List<String> getFiles() {
//        String path = "d:/666";
        String path = DOWNLOAD_PATH;
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                //包含路径和文件名
//                files.add(tempList[i].toString());
                //文件名，不包含路径
                String fileName = tempList[i].getName();
                files.add(fileName);
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }


    //上传
    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST)
    public String getAllData(MultipartHttpServletRequest request, MultipartFile filePart) {
        Iterator<String> fileNames = request.getFileNames();
        if (!ObjectUtils.isEmpty(filePart)) {
            //前端传过来的文件名 用户id 用户名 时间戳拼接
            String fileName = request.getParameter("fileName");

            String substring = fileName.substring(0, fileName.lastIndexOf("."));

            String next = fileNames.next();
            //上传得内容
            MultipartFile file = request.getFile(next);
            //文件名
            String originalFilename = file.getOriginalFilename();
            //后缀名
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
//            String originalFilename = userid+"-" + username + "." + suffix;

            //  /collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile/upload
            //测试上传得路径
//            String filePath = "d:/666";
            String filePath = UPLOAD_PAHT;
            //判断目录是否存在，不存在，新建目录
            File fileDie = new File(filePath);
            if (!fileDie.exists()) {
                fileDie.mkdir();
            }
            //上传得路径加文件名
            String f = filePath + "/" + fileName;
//            String f = filePath + "/" + substring+".html";
            File up = new File(f);
            try {
                filePart.transferTo(up);
                HashMap<String, Object> result = new HashMap<>();
                result.put("status", "200");
                result.put("message", "成功");
                logger.info("上传成功");
                return JSON.toJSONString(result);
            } catch (Exception e) {
                e.printStackTrace();
                HashMap<String, Object> result = new HashMap<>();
                result.put("status", "500");
                result.put("message", "失败");
                logger.info("上传失败");
                return JSON.toJSONString(result);
            }
        } else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("status", "500");
            result.put("message", "失败");
            logger.info("上传失败");
            return JSON.toJSONString(result);
        }
    }

    //下载 分析后的文件
    @RequestMapping("/downloadFileExcel")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) throws Exception {
        //设置响应头,下载内容类型
        response.setContentType("application/octet-stream");
        // /collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile/download
        //下载路径
//        String filePath = "d:/666";
        String filePath = DOWNLOAD_PATH;
        //前端传过来的文件名
        //String fileName = request.getParameter("fileName");
        //判断文件是否存在
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            //乱码问题
            String header = request.getHeader("User-Agent");
            if (header.contains("Firefox")) {
                //火狐
                fileName = "=?utf-8?B?" + new BASE64Encoder().encode(fileName.getBytes("utf-8")) + "?=";
            } else {
                //其他浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //通过文件地址，将文件转为输入流
            FileInputStream in = new FileInputStream(file);
            //通过输出流将刚才已经转为输入流的文件 输出给用户
            OutputStream out = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            //len = in.read(bytes);
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            in.close();
            out.close();
            logger.info("下载成功");
        } else { //不存在
            response.setCharacterEncoding("UTF-8");
            JSONObject json = new JSONObject();
            json.put("status", "500");
            json.put("message", "文件不存在");
            response.getWriter().write(json.toString());
        }
    }

    //文件是否存在
    @RequestMapping("/itExist")
    public void itExist(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) throws Exception {
        //设置响应头,下载内容类型
        response.setContentType("application/octet-stream");
//        String filePath = "d:/666";
        String filePath = DOWNLOAD_PATH;
        //前端传过来的文件名
        //String fileName = request.getParameter("fileName");
        //判断文件是否存在
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            response.setCharacterEncoding("UTF-8");
            JSONObject json = new JSONObject();
            json.put("status", "200");
            json.put("message", "文件分析完成可以下载");
            response.getWriter().write(json.toString());
            logger.info("文件分析完成可以下载");
        } else { //不存在
            response.setCharacterEncoding("UTF-8");
            JSONObject json = new JSONObject();
            json.put("status", "500");
            json.put("message", "文件不存在");
            response.getWriter().write(json.toString());
            logger.info("文件不存在");
        }
    }

    //下载 固定的模板
    @RequestMapping("/downTemplate")
    public void downTemplate(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) throws Exception {
        //设置响应头,下载内容类型
        response.setContentType("application/octet-stream");
        // /collect/tomcat_API/tomcat_caictpic_API8097/webapps/datafile/download
        //下载路径
//        String filePath = "d:/666/6";
        String filePath = TEMPLATE_PAHT;
        //前端传过来的文件名
        //String fileName = request.getParameter("fileName");
        //判断文件是否存在
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            //乱码问题
            String header = request.getHeader("User-Agent");
            if (header.contains("Firefox")) {
                //火狐
                fileName = "=?utf-8?B?" + new BASE64Encoder().encode(fileName.getBytes("utf-8")) + "?=";
            } else {
                //其他浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //通过文件地址，将文件转为输入流
            FileInputStream in = new FileInputStream(file);
            //通过输出流将刚才已经转为输入流的文件 输出给用户
            OutputStream out = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            //len = in.read(bytes);
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            in.close();
            out.close();
            logger.info("模板下载成功");
        } else { //不存在
            response.setCharacterEncoding("UTF-8");
            JSONObject json = new JSONObject();
            json.put("status", "500");
            json.put("message", "文件不存在");
            response.getWriter().write(json.toString());
        }
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("500MB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("500MB");
        return factory.createMultipartConfig();
    }


}
