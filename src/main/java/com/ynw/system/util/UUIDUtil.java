package com.ynw.system.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


/**
 * @author yuancheng
 * @description UUID生成工具类
 * @since 2017/4/11 0011 下午 4:26
 */
public class UUIDUtil {
    /**
     * 获得一个UUID
     *
     * @return String UUID
     * @throws Exception
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.replace("-", "");
    }

    private final static char[] BASE_32_CODE={'Q','W','E','R','T','Y','U','P',
            'A','S','D','F','G','H','J','K','L'
            ,'Z','X','C','V','B','N','M'
            ,'2','8','9','4','7','6','3','5'};

    /**
     * 根据用户的no值生成指定长度的32进制数然后根据每位数从BASE_32_CODE里面进行取值
     * @param no 用户的no值
     * @param length 长度
     * @return
     */
    public static String getBase32ReferralCode(Integer no,Integer length){
        long nowTime= System.currentTimeMillis();
        StringBuffer code=new StringBuffer();
        do {
            code.append(BASE_32_CODE[no % 32]);

        }while ((no=no/32)>0);
        code= code.reverse();
        while (code.length()<length){
            code.append(BASE_32_CODE[Math.toIntExact(nowTime % 32)]);
            nowTime=nowTime/32;
        }
        return code.toString();
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     * @throws Exception
     */
    public static String getEUUID() {
        synchronized ("EUUID") {
            String s = getUUID();
            //去掉“-”符号
            return s.replace("-", "").substring(0, 15) + new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());
        }
    }

    /**
     * 获得一个订单号
     *
     * @return String orderNum
     * @throws InterruptedException
     */
    public static String getOrderNum() throws Exception {
        synchronized ("OrderNum") {
            Thread.sleep(1);
            return getSJNum(3) + (new SimpleDateFormat("yyMdHmS").format(new Date())) + getSJNum(1);
        }
    }

    /**
     * 获得一个交易流水号
     *
     * @return String
     * @throws InterruptedException
     */
    public static String getTradeNo() throws Exception {
        synchronized ("TradeNo") {
            Thread.sleep(1);

            return (new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date())) + getSJNum(8);
        }
    }

    /**
     * 获得一个18位的交易流水号
     * @return
     * @throws Exception
     */
    public static String getSFTradeNo() throws Exception {
        synchronized ("TradeNo") {
            Thread.sleep(1);

            return (new SimpleDateFormat("yyyyMMdd").format(new Date())) + getSJNum(10);
        }
    }


    /**
     * 获得一个唯一码
     *
     * @return String
     * @throws InterruptedException
     */
    public static String getUniqueNo() throws Exception {
        synchronized ("UniqueNo") {
            Thread.sleep(1);
            return (new SimpleDateFormat("yyMdHmS").format(new Date()));
        }
    }

    /**
     * 获得随机码
     *
     * @return String
     * @throws InterruptedException
     */
    public static String getSJCode(char[] codeSequence, int length) {
        if (codeSequence == null || codeSequence.length == 0) {
            codeSequence = new char[]{
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L',
                    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                    'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                    'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                    'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};
        }
        StringBuffer code = new StringBuffer();
        //创建一个随机数生成器类
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(codeSequence[random.nextInt(codeSequence.length)]);
        }

        return code.toString();
    }

    /**
     * 获得数字随机码
     *
     * @return String
     * @throws InterruptedException
     */
    public static String getSJNum(int length) {
        char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return getSJCode(codeSequence, length);
    }


    //public static void main(String[] args) {
   //     try {

    //        System.out.println(getSFTradeNo());



     //   } catch (Exception e) {
     //       e.printStackTrace();
     //   }

   // }
}
