package com.ynw.system.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Map;

/**
 * 极光推送工具类
 */

public class JiGuangPushUtil {

    //两个参数分别填写你申请的masterSecret和appKey
    private static JPushClient jPushClient = new JPushClient("9523f7cd8dd577fe27ac60fe", "59f8786ee57638cf0bfd4632");

    private static String title="意难忘";
    /**
     * 通知推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     *
     * @param type  推送方式：1、“tag”标签推送，2、“alias”别名推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容
     */

    private static void pushNotice(String type, String value, String alert) {
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.all());//设置接受的平台，all为所有平台，包括安卓、ios、和微软的
        //设置如果用户不在线、离线消息保存的时间
        Options options = Options.sendno();
        options.setTimeToLive(86400L);    //设置为86400为保存一天，如果不设置默认也是保存一天
        builder.setOptions(options);
        //设置推送方式
        comment(type, builder, value);
        //设置为采用通知的方式发送消息
        builder.setNotification(Notification.alert(alert));
        PushPayload pushPayload = builder.build();
        try {
            //进行推送，实际推送就在这一步
            PushResult pushResult = jPushClient.sendPush(pushPayload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



/**
     *  复用
     * @param type
     * @param builder
     * @param value
     */

    public static void comment(String type, PushPayload.Builder builder, String value) {
        if(type.equals("alias")){
            builder.setAudience(Audience.alias(value));//别名推送
        }else if(type.equals("tag")){
            builder.setAudience(Audience.tag(value));//标签推送
        }else{
            builder.setAudience(Audience.all());//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
        }
    }


    public static void jpushAndroid(Map<String, String> parm) { // 设置好账号的app_key和masterSecret
        // String appKey = "**************"; String masterSecret = "************";
        // 创建JPushClient(极光推送的实例)
        JPushClient jPushClient = new JPushClient("9523f7cd8dd577fe27ac60fe", "59f8786ee57638cf0bfd4632");
        //推送的关键,构造一个
        PushPayload payload = PushPayload.newBuilder() .setPlatform(Platform.android()).setAudience(Audience.all()).setNotification(Notification.android(parm.get("msg"), "这是title", parm)).setOptions(Options.newBuilder().setApnsProduction(false).build()).setMessage(Message.content(parm.get("msg"))) .build();

        try {
            PushResult pu = jPushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

/**
     * 自定义消息推送
     * 备注：推送方式不为空时，推送的值也不能为空；推送方式为空时，推送值不做要求
     * @param type 推送方式：1、“tag”标签推送，2、“alias”别名推送
     * @param value 推送的标签或别名值
     * @param alert 推送的内容
     */
    public  static  void pushMsg(String type, Map alert,String T,String... value){
        if (value==null) {
            return;
        }
        Audience audience=null;
        switch(type){
            //标签推送
            case "tag":
                break;
            //别名推送
            case "alias":
                audience=Audience.alias(value);
                break;
            //全部推送
            case "all":
                audience=Audience.all();
                break;
        }
        //Map<String,String> stringStringMap=JSONObject.parseObject(JSONObject.toJSONString(t),Map.class);
        String title=(String) alert.get("title");
        String body=(String) alert.get("body");
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.all());
        builder.setAudience(audience);
        builder.setNotification(
                Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(body)
                                .setTitle(title).addExtra("t", T).build())
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(IosAlert.newBuilder().setTitleAndBody(title,null,body).build()).setSound("default").incrBadge(1)
                                .addExtra("t", T).build()).build());
        builder.setOptions(Options.newBuilder()
                //设置生产环境true,开环境为false
                .setApnsProduction(true).build());
        builder.setMessage(Message.content((String) alert.get("body")));
        PushPayload pushPayload = builder.build();
        System.out.println(pushPayload.toJSON());
        try{
            PushResult pushResult=jPushClient.sendPush(pushPayload);
        }catch(Exception e){
           return ;//如果设备不存在则不进行推送
        }
    }


}
