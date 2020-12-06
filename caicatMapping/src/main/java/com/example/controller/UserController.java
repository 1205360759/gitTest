package com.example.controller;


import com.example.common.LogEnumConsts;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.utils.MD5Util;
import com.example.wapper.UserParamWapper;
import com.example.wapper.UserWapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping(value = "/api")
public class UserController {
    @Autowired
    private UserService userService;


    /**
     * 查用户列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/userList")
    public Object getUserList(@Param("userId")Long userId) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<UserWapper> userList = null;
        if(userId != null){
            User user = userService.getByUserId(userId);
            if (user == null){
                //当前用户id不存在
                userList = null;
            }else{
                if ("1".equals(user.getRoleId()) ){
                    //超管，列表显示所有
                    userList = userService.getUserList(null);
                }else if ("3".equals(user.getRoleId())|| "2".equals(user.getRoleId())){
                    //普通用户和管理员 ，列表只显示当前用户信息
                    userList = userService.getUserList(userId);
                }else{
                    //不存在用户列表
                    userList = null;
                }
            }
        }
        resultMap.put("userList", userList);
        return resultMap;
    }

    /**
     * 用户新增
     *1 超管
     *2 管理员
     *3 普通用户
     * @param userParamWapper
     * @return
     */
    @ResponseBody
    @PostMapping("/saveUser")
    public Object saveUser(UserParamWapper userParamWapper) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if (StringUtils.isBlank(userParamWapper.getRoleId())){
            resultMap.put("500", "新增失败,角色编号不能为空");
            return resultMap;
        }else {
            if ("2".equals(userParamWapper.getRoleId()) || "3".equals(userParamWapper.getRoleId())) {//新增管理员与普通用户时
                User userEn = new User();
                if (userParamWapper != null) {
                    User user = userService.getByAccout(userParamWapper.getAccount());
                    if (user != null) {
                        resultMap.put("500", "新增失败,用户名已存在");
                        resultMap.put("failed", 0);
                    } else {
                        userEn.setPassword(MD5Util.MD5(userParamWapper.getPassword()));
                        userEn.setStatus("ENABLE");
                        userEn.setCreateTime(new Date());
                        userEn.setRoleId(userParamWapper.getRoleId());
                        userEn.setAccount(userParamWapper.getAccount());
                        userEn.setName(userParamWapper.getName());
                        userEn.setPhone(userParamWapper.getPhone());
                        userEn.setEmail(userParamWapper.getEmail());
                        userEn.setDescription(userParamWapper.getDescription());
                        int index = userService.saveUser(userEn);
                        User userAccount = null;
                        if (userParamWapper.getUserId() != "" && userParamWapper.getUserId() != null) {
                            userAccount = userService.getByUserId(Long.parseLong(userParamWapper.getUserId()));
                        }
                        if (userParamWapper.getUserId() == "" || userParamWapper.getUserId() == null) {
                            userAccount = new User(0L, "未知用户");
                        }
                        if (index > 0) {
                            //添加操作日志
                            resultMap.put("200", "新增成功");
//                            operatorLogService.insertAPI((StringUtils.isNoneBlank(userAccount.getAccount()) ? userAccount.getUserId() : null), LogEnumConsts.logType.add.getName(), "新增用户", "add", new Date(), "SUCCEED", "新增用户成功", (StringUtils.isNoneBlank(userAccount.getAccount()) ? userAccount.getAccount() : "未知"));
                            return resultMap;
                        } else {
                            resultMap.put("500", "新增失败");
//                            operatorLogService.insertAPI((StringUtils.isNoneBlank(userAccount.getAccount()) ? userAccount.getUserId() : null), LogEnumConsts.logType.add.getName(), "新增用户", "add", new Date(), "FAILED", "新增用户失败", (StringUtils.isNoneBlank(userAccount.getAccount()) ? userAccount.getAccount() : "未知"));
                            return resultMap;
                        }
                    }
                }
            }
            if ("1".equals(userParamWapper.getRoleId())) {//新增超管
                resultMap.put("500", "不能新增超级管理员");
                return resultMap;
            }
            if (!"1".equals(userParamWapper.getRoleId()) && !"2".equals(userParamWapper.getRoleId()) && !"3".equals(userParamWapper.getRoleId())) {
                resultMap.put("500", "新增失败,角色只能为管理员或者普通用户");
                return resultMap;
            }
        }
        return  resultMap;
    }

    /**
     * 更新前查询
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/doUpdateUser")
    public Object doUpdateUser (@Param("userId")Long userId){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        User user = userService.getByUserId(userId);
        if(user!= null){
            UserWapper userWapper = new UserWapper(user.getUserId(),user.getRoleId(),user.getAccount(),user.getName(),user.getStatus(),user.getCreateTime(),user.getCreateUser(),user.getUpdateTime(),user.getUpdateUser(),user.getDescription());
            resultMap.put("user",userWapper);
        }else {
            resultMap.put("500","未查到该用户信息");
        }
        return resultMap;
    }

    /**
     * 更新用户信息
     *
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/updateUser")
    public Object updateUser(UserParamWapper userParamWapper,
                             @Param("userId") String userId,@Param("loginUserId")String loginUserId,
                             HttpServletRequest request,HttpServletResponse response) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        User userAccount = null;
        if(StringUtils.isNoneBlank(loginUserId)){
            userAccount = userService.getByUserId(Long.parseLong(loginUserId));//系统登录的用户
        }
        String username = "";
        if(userAccount != null){
            username = userAccount.getAccount();
        }
        if(StringUtils.isNoneBlank(userId)){
            User user_old = userService.getByUserId(Long.parseLong(userId));//当前更新的用户
            if (user_old != null) {
                user_old.setUpdateTime(new Date());
                user_old.setUpdateUser(Long.parseLong(loginUserId));
                user_old.setEmail(userParamWapper.getEmail());
                user_old.setPhone(userParamWapper.getPhone());
                user_old.setName(userParamWapper.getName());
                user_old.setDescription(userParamWapper.getDescription());
                int index = userService.updateUser(user_old);
                if (index > 0) {
                    //添加操作日志
                    resultMap.put("200", "更新成功");
//                    operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.add.getName(),"更新用户","update",new Date(),"SUCCEED","更新用户操作成功",username);
                } else {
                    resultMap.put("500", "更新失败");
//                    operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.add.getName(),"更新用户","update",new Date(),"FAILED","更新用户操作失败",username);
                }
            } else {
                resultMap.put("500", "更新失败");
//                operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.add.getName(),"更新用户","update",new Date(),"FAILED","更新用户操作失败",username);
            }
        }
        return resultMap;
    }

    /**
     * 删除用户
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @GetMapping("/deleteUser")
    public Object deleteUser (@Param("userId") String userId,@Param("loginUserId")String loginUserId,
                              HttpServletRequest request,HttpServletResponse response){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object> ();
        User userAccount = null;
        String username = "";
        if(StringUtils.isNoneBlank(userId)){
            userAccount =  userService.getByUserId(Long.parseLong(userId));
            if(userAccount!= null){
                username = userAccount.getAccount();
            }else {
                username = "未知";
            }
        }
        if(userId == null){
            resultMap.put("500","用户id不存在");
//            operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.del.getName(),"删除用户","delete",new Date(),"FIALED","删除用户失败",username);
        }else {
            User user = userService.getByUserId(Long.parseLong(userId));
            if(user != null ){
                if (user.getUserId() == 1){
                    resultMap.put("304","不能删除超级管理员！");
//                    operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.del.getName(),"删除用户","delete",new Date(),"FIALED","删除超级管理员失败",username);
                    return  resultMap;
                }else{
                    int index = userService.deleteByUserId(Long.parseLong(userId));
                    if(index > 0){
                        //添加操作日志
                        resultMap.put("200","删除成功");
                        resultMap.put("success",1);
//                        operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.del.getName(),"删除用户","delete",new Date(),"SUCCEED","删除用户操作成功",username);
                    }else {
                        resultMap.put("500","删除失败");
//                        operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.del.getName(),"删除用户","delete",new Date(),"FAILED","删除用户操作失败",username);
                    }
                }
            }else{
                resultMap.put("500","用户不存在不可删除！");
//                operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.del.getName(),"删除用户","delete",new Date(),"FIALED","删除用户失败",username);
            }
        }
        return resultMap;
    }

    /**
     * 更新用户密码查询接口
     * @param userId
     * @return
     */
    @ResponseBody
    @GetMapping("/doUpdateUserPwd")
    public Object doUpdateUserPwd(@Param("userId") String userId){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        User user = userService.getByUserId(Long.parseLong(userId));
        if(user != null) {
            resultMap.put("user", user);
        }else {
            resultMap.put("500","用户不存在");
        }
        return  resultMap;
    }

    /**
     * 更新用户密码
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @ResponseBody
    @PostMapping("/updateUserPwd")
    public Object updateUserPwd(@Param("oldPwd") String oldPwd,@Param("newPwd")String newPwd,
                                @Param("userId")String userId,@Param("loginUserId")String loginUserId){
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(userId)&&StringUtils.isNoneBlank(loginUserId)){
            User user = userService.getByUserId(Long.parseLong(userId));//更新密码的用户
            User userlogin = userService.getByUserId(Long.parseLong(loginUserId));//当前登录的用户
            if(user != null&& userlogin != null){
                String currPwd = user.getPassword();//加密后库里存的密码
                String oldPwdMd5 = MD5Util.MD5(oldPwd);//用户输入的原密码
                String username = userlogin.getAccount();//登录用户名
                if(oldPwdMd5.equals(currPwd)){
                    user.setPassword(MD5Util.MD5(newPwd));//旧密码与新密码匹配
                    user.setUpdateTime(new Date());//更新时间
                    user.setUpdateUser(Long.parseLong(userId));
                    int index = userService.updateUser(user);
                    if (index > 0){
                        resultMap.put("200","更改密码成功");
                        resultMap.put("success",1);
//                        operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.edit.getName(),"更新用户密码","update",new Date(),"SUCCEED","更改用户密码操作成功",username);
                    }else{
                        resultMap.put("500","更改密码失败");
//                        operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.edit.getName(),"更新用户密码","update",new Date(),"FAILED","更改用户密码操作失败",username);
                    }
                }else{
                    resultMap.put("500","原密码与新密码不一致");
//                    operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.edit.getName(),"更新用户密码","update",new Date(),"FAILED","更改用户密码操作失败",username);
                }
            }else {
                resultMap.put("500","用户不存在");
//                operatorLogService.insertAPI(Long.parseLong(loginUserId), LogEnumConsts.logType.edit.getName(),"更新用户密码","update",new Date(),"FAILED","更改用户密码操作失败,用户不存在","");
            }
        }
        return  resultMap;
    }

}
