package com.example.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class  StringToJson {

    public static Map<String, String> stringToJson(String string){
        JSONObject data = JSONObject.fromObject(string);//解析第一层数据
        int status = data.optInt("status");
        String msg = data.optString("msg");
        String result = data.optString("result");
        System.out.println("status:     "+status+"msg:     "+msg+"result:     "+result);
        if(status == 0) {
            JSONObject secondData = JSONObject.fromObject(result);//解析第二层数据
            String barcode = secondData.optString("barcode");
            String name = secondData.optString("name");
            String ename = secondData.optString("ename");
            String unspsc = secondData.optString("unspsc");
            String brand = secondData.optString("brand");
            String type = secondData.optString("type");
            String width = secondData.optString("width");
            String height = secondData.optString("height");
            String depth = secondData.optString("depth");
            String origincountry = secondData.optString("origincountry");
            String originplace = secondData.optString("originplace");
            String assemblycountry = secondData.optString("assemblycountry");
            String barcodetype = secondData.optString("barcodetype");
            String catena = secondData.optString("catena");
            String isbasicunit = secondData.optString("isbasicunit");
            String packagetype = secondData.optString("packagetype");
            String grossweight = secondData.optString("grossweight");
            String netweight = secondData.optString("netweight");
            String description = secondData.optString("description");
            String keyword = secondData.optString("keyword");
            String pic = secondData.optString("pic");
            String price = secondData.optString("price");
            String licensenum = secondData.optString("licensenum");
            String healthpermitnum = secondData.optString("healthpermitnum");
            String netcontent = secondData.optString("netcontent");
            String company = secondData.optString("company");
            String expirationdate = secondData.optString("expirationdate");
            Map<String, String> map = new HashMap<String, String>();
            map.put("barcode", barcode);
            map.put("name", name);
            map.put("ename", ename);
            map.put("unspsc", unspsc);
            map.put("brand", brand);
            map.put("type", type);
            map.put("width", width);
            map.put("height", height);
            map.put("depth", depth);
            map.put("origincountry", origincountry);
            map.put("originplace", originplace);
            map.put("assemblycountry", assemblycountry);
            map.put("barcodetype", barcodetype);
            map.put("catena", catena);
            map.put("isbasicunit", isbasicunit);
            map.put("packagetype", packagetype);
            map.put("grossweight", grossweight);
            map.put("netweight", netweight);
            map.put("description", description);
            map.put("keyword", keyword);
            map.put("pic", pic);
            map.put("price", price);
            map.put("licensenum", licensenum);
            map.put("healthpermitnum", healthpermitnum);
            map.put("netcontent", netcontent);
            map.put("company", company);
            map.put("expirationdate", expirationdate);

            System.out.println("  barcode:" + barcode + "  name:" + name + "  ename:" + ename + "  unspsc:" + unspsc);
            return map;
        }else{
            Map<String, String> map = new HashMap<String, String>();
            return map;
        }
    }

    /**
     * 判断一个时间是否在当前时间的7天之内
     * @param addtime
     * @param now
     * @return
     */
    public static boolean isLatestWeek(Date addtime, Date now){
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(now);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        if(before7days.getTime() < addtime.getTime()){
            return true;
        }else{
            return false;
        }
    }

}
