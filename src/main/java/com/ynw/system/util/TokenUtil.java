package com.ynw.system.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mysql.cj.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * App登陆Token验证的工具类
 * @author Adonis
 */
public class TokenUtil {
    public final static String SUCCESS="Token success";
    public final static String Fail="Token fail";
    //token秘钥,请勿泄露,请勿随便更改
    private static final String SECRET="JKKLJOKADBCA";
    //token过期时间为30天
    public static final int calendarField= Calendar.DATE;
    public static final int calendarInterval=30;

    /**
     * 根据用户ID来生成Token
     * @param acUserId
     * @return
     * @throws Exception
     */
public static String createToken(String acUserId)throws Exception{
        //token签发时间
    Date iatDate = new Date();
    // token失效时间
    Calendar nowTime = Calendar.getInstance();
    nowTime.add(calendarField, calendarInterval);
    Date expiresDate = nowTime.getTime();

    // token的header
    Map<String, Object> map = new HashMap<>();
    map.put("alg", "HS256");
    map.put("typ", "JWT");

    // 创建 token
    // param backups {iss:Service, aud:APP}
    String token = JWT.create().withHeader(map) // header头部
            .withClaim("iss", "Service") // payload载荷
            .withClaim("aud", "APP").withClaim("user_id", null == acUserId ? null : acUserId)
            .withClaim("exp",expiresDate)
            .withClaim("iat",iatDate)
            .withIssuedAt(iatDate) // sign time
            .withExpiresAt(expiresDate) // expire time
            .sign(Algorithm.HMAC256(SECRET)); // signature签名
         return token;
}

    /**
     * 根据Token来获取信息
     * @param token
     * @return
     */
    public static Map<String, Claim> verifyToken(String token){
        DecodedJWT jwt = null;
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        return jwt.getClaims();
    }
    /**
     * 从token中获取UserId,
     * @param token
     * @return
     * @throws Exception
     */
    public static String getAcUserId(String token){
        Map<String,Claim> claimMap=verifyToken(token);
        Claim userClaim=claimMap.get("user_id");
        if(null==userClaim|| StringUtils.isNullOrEmpty(userClaim.asString()))throw new RuntimeException("token验证错误");
        return userClaim.asString();
    }
    public static Date getExpTime(String token){
        Map<String,Claim> claimMap=verifyToken(token);
        Claim expClaim=claimMap.get("exp");
        if(null==expClaim)throw new RuntimeException("token验证错误");
        return expClaim.asDate();
    }
}
