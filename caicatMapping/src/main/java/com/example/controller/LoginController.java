package com.example.controller;


import com.example.common.LogEnumConsts;
import com.example.entity.SysUserTokenEntity;
import com.example.entity.User;
import com.example.entity.UserInfo;
import com.example.service.*;
import com.example.utils.TokenUtil;
import com.example.wapper.LoginLogWapper;
import com.example.wapper.UserWapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private VerCodeService verCodeService;

    /**
     * 用户登录
     *
     * @return 1 超管
     * 2 管理员
     * 3 普通用户
     */
    @ResponseBody
    @PostMapping("/login")
    public Object loginUser(@Param("username") String username, @Param("password") String password,
                            HttpServletRequest request, HttpServletResponse response, @Param("uuid") String uuid,
                            @Param("captcha") String captcha) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        boolean validate = verCodeService.validate(uuid, captcha);
        if (!validate) {
            resultMap.put("status", "500");
            resultMap.put("message","验证码不正确");
            return resultMap;
        }

//        List<Role> roleList = null;
        User user = userService.getByUsernamePassword(username, password);
        UserInfo userInfo = userService.getUserByUserName(username);
        Date thisErrorLoginTime = null;        // 修改的本次登陆错误时间
        Integer islocked = 0;            // 获取是否锁定状态
        if (ObjectUtils.isEmpty(userInfo)) {
            resultMap.put("status", "500");
            resultMap.put("message","该用户不存在");
            return resultMap;
        }
        if (user != null && user.getRoleId() != null && user.getRoleId() != "") {
            if (ObjectUtils.isEmpty(userInfo.getIsLocked())) {
                userInfo.setIsLocked(0);
            }
            if (userInfo.getIsLocked() == 1) {
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datestr = format.format(date);
                try {
                    thisErrorLoginTime = format.parse(datestr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date lastLoginErrorTime = null; // 最后一次登陆错误时间
                Long timeSlot = 0L;
                if (userInfo.getLastLoginErrorTime() == null) {
                    lastLoginErrorTime = thisErrorLoginTime;
                } else {
                    lastLoginErrorTime = userInfo.getLastLoginErrorTime();
                    timeSlot = thisErrorLoginTime.getTime() - lastLoginErrorTime.getTime();
                }
                if (timeSlot < 1800000) {    // 判断最后锁定时间,30分钟之内继续锁定
                    resultMap.put("status", "500");
                    resultMap.put("message","账户已被锁定，请" + (30 - Math.ceil((double) timeSlot / 60000)) + "分钟之后再次尝试");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                }
            }
//            roleList = roleService.getByRoleId(user.getRoleId());
            if (true) {
                UserWapper userWapper = new UserWapper(user.getUserId(), user.getRoleId(), user.getAccount(), user.getName(), user.getStatus(), user.getCreateTime(), user.getCreateUser(), user.getUpdateTime(), user.getUpdateUser(), user.getDescription());
                String token = createToken(user.getUserId());
                user.setLastLoginTime(new Date());
                userService.updateUser(user);//将token和最后登录时间一起更新
                userInfo.setIsLocked(0);
                userInfo.setLoginErrorCount(0);
                userService.addUser(userInfo);
                //添加登录日志
                loginLogService.saveLoginLog(user.getUserId(), new Date(), null, "登录日志", "用户登录成功", "SUCCEED", username);
                resultMap.put("user", userWapper);
                resultMap.put("token", token);
                resultMap.put("status", "200");
                resultMap.put("message","登录成功");
                return resultMap;
            } else {
                loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                resultMap.put("status", "500");
                resultMap.put("message","该用户没有角色，不能登录");
            }
        } else if (!userInfo.getPassword().equals(password)) { //大括号
            //!userInfo.getPassword().equals(MD5Util.MD5(password))
            if (userInfo.getIsLocked() == null) {
                userInfo.setIsLocked(0);
            } else {
                islocked = userInfo.getIsLocked();
            }
            if (userInfo.getLoginErrorCount() == null) {
                userInfo.setLoginErrorCount(0);
            }
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datestr = format.format(date);
            try {
                thisErrorLoginTime = format.parse(datestr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (islocked == 1) {    // 账户被锁定，被锁定时登陆错误次数一定是5，所以只判断一次
                Date lastLoginErrorTime = null; // 最后一次登陆错误时间
                Long timeSlot = 0L;
                if (userInfo.getLastLoginErrorTime() == null) {
                    lastLoginErrorTime = thisErrorLoginTime;
                } else {
                    lastLoginErrorTime = userInfo.getLastLoginErrorTime();
                    timeSlot = thisErrorLoginTime.getTime() - lastLoginErrorTime.getTime();
                }
                if (timeSlot < 1800000) {    // 判断最后锁定时间,30分钟之内继续锁定
                    double v = 30 - Math.ceil((double) timeSlot / 60000);
                    resultMap.put("status", "500");
                    resultMap.put("message","账户已被锁定，请" + v + "分钟之后再次尝试");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                } else {
                    //30分钟以后重置（不锁定）
                    userInfo.setIsLocked(0);
                    userInfo.setLoginErrorCount(1);
                    userInfo.setLastLoginErrorTime(thisErrorLoginTime);
                    userService.addUser(userInfo);
                    resultMap.put("status", "500");
                    resultMap.put("message","账户或密码错误，还有4次登陆机会");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                }
            } else if (userInfo.getLoginErrorCount() == 4) { //账户第五次登陆失败 ，此时登陆错误次数增加至5，以后错误仍是5，不再递增
                Date lastLoginErrorTime = null; // 最后一次登陆错误时间
                Long timeSlot = 0L;
                if (userInfo.getLastLoginErrorTime() == null) {
                    lastLoginErrorTime = thisErrorLoginTime;
                } else {
                    lastLoginErrorTime = userInfo.getLastLoginErrorTime();
                    timeSlot = thisErrorLoginTime.getTime() - lastLoginErrorTime.getTime();
                }
                if (timeSlot > 1800000) {//如果在30分钟以后，重置错误次数
                    userInfo.setLoginErrorCount(1);
                    userInfo.setLastLoginErrorTime(thisErrorLoginTime);
                    userService.addUser(userInfo);    //修改用户
                    resultMap.put("status", "500");
                    resultMap.put("message","账户或密码错误，还有4次登陆机会");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                }
                userInfo.setLoginErrorCount(5);
                userInfo.setIsLocked(1);
                userInfo.setLastLoginErrorTime(thisErrorLoginTime);
                userService.addUser(userInfo);    //修改用户
                resultMap.put("status", "500");
                resultMap.put("message","账户已被锁定，请30分钟之后再次尝试");
                loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                return resultMap;
            } else {    // 账户前四次登陆失败
                Date lastLoginErrorTime = null; // 最后一次登陆错误时间
                Long timeSlot = 0L;
                if (userInfo.getLastLoginErrorTime() == null) {
                    lastLoginErrorTime = thisErrorLoginTime;
                } else {
                    lastLoginErrorTime = userInfo.getLastLoginErrorTime();
                    timeSlot = thisErrorLoginTime.getTime() - lastLoginErrorTime.getTime();
                }
                if (timeSlot > 1800000) {//如果在30分钟以后，重置错误次数
                    userInfo.setLoginErrorCount(1);
                    userInfo.setLastLoginErrorTime(thisErrorLoginTime);
                    userService.addUser(userInfo);    //修改用户
                    resultMap.put("status", "500");
                    resultMap.put("message","账户或密码错误，还有4次登陆机会");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                } else {
                    userInfo.setLoginErrorCount(userInfo.getLoginErrorCount() + 1);
                    userInfo.setLastLoginErrorTime(thisErrorLoginTime);
                    userService.addUser(userInfo);    //修改用户
                    int i = 5 - userInfo.getLoginErrorCount();
                    resultMap.put("status", "500");
                    resultMap.put("message","账户或密码错误，还有" + i + "次登陆机会");
                    loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                    return resultMap;
                }
            }
        } else { //大括号
            islocked = userInfo.getIsLocked();
            if (islocked == 1) {
                Date lastLoginErrorTime = null; // 最后一次登陆错误时间
                Long timeSlot = 0L;
                if (userInfo.getLastLoginErrorTime() == null) {
                    lastLoginErrorTime = new Date();
                } else {
                    lastLoginErrorTime = userInfo.getLastLoginErrorTime();
                    timeSlot = new Date().getTime() - lastLoginErrorTime.getTime();
                }
                if (timeSlot < 1800000) {    // 判断最后锁定时间,30分钟之内继续锁定
                    resultMap.put("status", "500");
                    resultMap.put("message","账户已被锁定，请" + (30 - Math.ceil((double) timeSlot / 60000)) + "分钟之后再次尝试");
                    return resultMap;
                } else { // 判断最后锁定时间,30分钟之后登陆账户
//                    roleList = roleService.getByRoleId(user.getRoleId());
                    if (true) {
                        UserWapper userWapper = new UserWapper(user.getUserId(), user.getRoleId(), user.getAccount(), user.getName(), user.getStatus(), user.getCreateTime(), user.getCreateUser(), user.getUpdateTime(), user.getUpdateUser(), user.getDescription());
                        String token = createToken(user.getUserId());
                        user.setLastLoginTime(new Date());
                        userService.updateUser(user);//将token和最后登录时间一起更新
                        userInfo.setIsLocked(0);
                        userInfo.setLoginErrorCount(0);
                        userService.addUser(userInfo);
                        //添加登录日志
                        loginLogService.saveLoginLog(user.getUserId(), new Date(), null, "登录日志", "用户登录成功", "SUCCEED", username);
                        resultMap.put("user", userWapper);
                        resultMap.put("token", token);
                        resultMap.put("status", "200");
                        resultMap.put("message","登录成功");
                    } else {
                        loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
                        resultMap.put("status", "500");
                        resultMap.put("message","该用户没有角色，不能登录");
                    }
                }
            }
        }
//            else {
//                loginLogService.saveLoginLog(null, new Date(), null, "登录日志", "用户登录失败", "FAILED", username);
//                resultMap.put("500", "该用户不存在");
//            }
        return resultMap;
    }

    /**
     * 生成token
     *
     * @param userId
     * @return token
     */
    public String createToken(Long userId) {
        //生成一个token
        String token = TokenUtil.createTKN();//生成token
        //当前时间
        Date now = new Date();
        // Date expireTime = new Date(now.getTime() + 24 * 3600 * 1000);//24小时后过期
        Date expireTime = new Date(now.getTime() + 7200 * 1000);//2个小时后过期
        //判断是否生成过token
        SysUserTokenEntity tokenEntity = sysUserTokenService.selectById(userId);
        if (tokenEntity == null) {  //如果为空，插入新的token
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            sysUserTokenService.insert(tokenEntity);
            //如果过期时间小于当前时间，说明token已过期，更新token
        } else if (tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            sysUserTokenService.updateById(tokenEntity);
        } else {
            //如果token不为空，也没过期，返回原来的token并且重置过期时间（以前的逻辑）
//            tokenEntity.setUpdateTime(now);
//            tokenEntity.setExpireTime(expireTime);
//            sysUserTokenService.updateById(tokenEntity);
//            return tokenEntity.getToken();
            //如果token不为空，也没过期，只要登陆了，就重置token,后登录的会把先登陆的顶下去（现在的逻辑）
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            sysUserTokenService.updateById(tokenEntity);
        }
        return token;
    }

    /**
     * 用户退出
     *
     * @param username
     * @return
     */
    @ResponseBody
    @GetMapping("/logout")
    public Object logoutUser(@Param("username") String username, @Param("userId") String userId,
                             HttpServletRequest request, HttpServletResponse response) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
//        Enumeration em = request.getSession().getAttributeNames();
//        while(em.hasMoreElements()){
//            request.getSession().removeAttribute(em.nextElement().toString());//清空session
//            resultMap.put("200","退出登录");
//            loginLogService.saveLoginLog(user.getUserId(),new Date(),"登出日志","用户退出成功","SUCCEED",username);
//        }
        resultMap.put("200", "退出登录");
        loginLogService.saveLoginLog(Long.parseLong(userId), null, new Date(), "登出日志", "用户退出成功", "SUCCEED", username);
        return resultMap;
    }

    /**
     * 删除登录日志
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/deleteLoginLog")
    @ResponseBody
    public Object deleteByPrimaryKey(HttpServletRequest request, HttpServletResponse response,
                                     @Param("ids") String ids, @Param("userId") String userId) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        //得到登录信息
        User user = null;
        String username = "";
        if (StringUtils.isNoneBlank(userId)) {
            user = userService.getByUserId(Long.parseLong(userId));
            username = user.getAccount();
        } else {
            username = "未知";
        }
        //判断ids是否为空
        if (ids != null && !"".equals(ids)) {
            String[] arr = ids.split(",");
            for (String id : arr) {
                int ind = loginLogService.deleteByPrimaryKey(Long.valueOf(id));
                if (ind > 0) {
                    //生成日志
                    resultMap.put("200", "删除成功");
//                    operatorLogService.insertAPI(Long.parseLong(userId), LogEnumConsts.logType.del.getName(), "删除日志操作", "delete", new Date(), "SUCCEED", "删除操作日志成功", username);
                } else {
                    resultMap.put("500", "删除失败");
//                    operatorLogService.insertAPI(Long.parseLong(userId), LogEnumConsts.logType.del.getName(), "删除日志操作", "delete", new Date(), "FAILED", "删除操作日志失败", username);
                }
            }
        } else {
            resultMap.put("500", "删除失败");
//            operatorLogService.insertAPI(Long.parseLong(userId), LogEnumConsts.logType.del.getName(), "删除日志操作", "delete", new Date(), "FAILED", "删除操作日志失败", username);

        }
        return resultMap;
    }

    /**
     * 登录日志列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/getLoginLogList")
    public Object getLoginLogList() {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        List<LoginLogWapper> loginLogs = loginLogService.getLoginLogList();
        resultMap.put("loginLogList", loginLogs);
        return resultMap;
    }

    /**
     * 获取用户手册文档
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserManualDocument")
    public File downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取正式服务器
        String path = "/home/collect/tomcat_API_zmq8091/shouce/userDocument.pdf";
        //获取测试服务器文件
//        String path = "/collect/tomcat_API_zmq8091/shouce/userDocument.doc";
//        String path = "E:\\软开_work\\自贸区\\自贸区终验报告\\用户手册\\userDocument.doc";
        File file = new File(path);
        //2.获取要下载的文件名
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "utf-8"));
        //获取文件输入流
        InputStream in = new FileInputStream(path);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        while ((len = in.read(buffer)) > 0) {
            //将缓冲区的数据输出到客户端浏览器
            out.write(buffer, 0, len);
        }
        in.close();
        return file;
    }

}
