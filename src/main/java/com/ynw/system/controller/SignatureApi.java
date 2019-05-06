package com.ynw.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "微信小程序接口",description = "微信小程序调用token接口")
public class SignatureApi {

    @Resource
    private RedisService redisService;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 微信小程序获取accessToken
     *
     * @author Mr.Wen
     * @time 2017年8月28日
     */
    // 网页授权接口
        public  String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxe08284e256b0e087&secret=3b77d0deeef0fb65c1b87a2f6201279b";

        public  Map<String, String> getAccessToken() {
            Map<String,String> tokenMap = getMap(GetPageAccessTokenUrl);
//            System.out.println(tokenMap.get("access_token"));
            return tokenMap;
    }

    /**
     *  复用
     * @param url
     * @return
     */
    public Map getMap(String url) {
        ResponseEntity responseEntity =restTemplate.getForEntity(url,String.class);
        String object = (String)responseEntity.getBody();
        Map<String,Object> map = JSONObject.parseObject(object, Map.class);
        return map;
    }

    /**
     * @author Mr.Wen
     * @description 获取ticket
     * @date 2018/3/29
     */
    public final static String GetJsApiTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public Map<String,Object> getJsApiTicket(String token) {
        String requestUrl = GetJsApiTicketUrl.replace("ACCESS_TOKEN", token);
        Map<String,Object> ticketMap = getMap(requestUrl);
//        System.out.println(ticketMap.get("ticket"));
        return ticketMap;
    }

    @PostMapping("/ynw-ms/getSignature")
    @ApiOperation(value = "请求微信获取参数", notes = "传入：跳转url（url)(必传)")
    @ApiImplicitParam(name = "url", value = "跳转url", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> getSignature(HttpServletResponse response, String url) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        String token;
        if (null != redisService.get("token")) {
            token = (String) redisService.get("token");
        } else {
            token = getAccessToken().get("access_token");
            redisService.put("token", token,7200);
        }
        System.out.println(token);
        //        url = java.net.URLDecoder.decode(url,"UTF-8");
        String nonceStr = UUIDUtil.getEUUID();//随机字符串
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
        String ticket = (String) getJsApiTicket(token).get("ticket");
        String str = "jsapi_ticket="+ticket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
        //将字符串进行sha1加密
        String signature = SHA.getSha1(str);
        Map<String,String> map=new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("ticket",ticket);
        map.put("nonceStr",nonceStr);
        map.put("signature",signature);
//        response.setStatus(HttpStatus.SC_NO_CONTENT); //HttpStatus.SC_NO_CONTENT = 204
        //跨域返回
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Token"); //当判定为预检请求后，设定允许请求的头部类型
        response.addHeader("Access-Control-Max-Age", "1");
        response.addHeader("Access-Control-Allow-Origin", "*");
        return ResultUtil.successResponse(map);
    }

}
