package com.example.utils;

import org.junit.Test;

import java.util.Map;
import java.util.UUID;

/**
 * @author yp
 * @date 2019/7/4 14:50
 */
public class TokenUtil{

    /**
     * 使用UUID 生成token
     * @return
     */
    public  static  String createTKN(){
        // 创建 GUID 对象
        UUID uuid = UUID.randomUUID();
        // 得到对象产生的ID
        String token = uuid.toString();
        token = token.replaceAll("-", "");
//        System.out.println(token);
        //保存后下次就可以通过用户的token在数据库来做个验证
        return token;
    }


    /**
     * 验证用户的token值是否正确
     * @param account
     * @param tokenValue
     * @return 正确--true，错误--false
     */
    public static boolean checkTKN(String account, String tokenValue){
        //检查该token是否属于该用户
        if(account.equals(tokenValue)){
            return true;
        }else{
            return false;
        }
    }

    @Test
    public static void testTkn(String [] args){
        System.out.println(createTKN());
    }
}
