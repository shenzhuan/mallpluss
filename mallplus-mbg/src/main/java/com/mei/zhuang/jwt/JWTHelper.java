package com.mei.zhuang.jwt;


import com.mei.zhuang.constant.CommonConstant;
import com.mei.zhuang.util.RsaKeyHelper;
import com.mei.zhuang.util.StringHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by john on 2017/9/10.
 */
public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();
    /**
     * 密钥加密token
     * @param jwtInfo 用户信息
     * @param priKeyPath 私钥路径
     * @param expire token过期时间（单位：秒）
     * @return token
     * @throws Exception 生成token异常信息
     */
    public static String generateToken(IJWTInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        return Jwts.builder()
                .setSubject(jwtInfo.getUniqueName())
                .claim(CommonConstant.JWT_KEY_USER_ID, jwtInfo.getId())
                .claim(CommonConstant.JWT_KEY_NAME, jwtInfo.getName())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath))
                .compact();
    }

    /**
     * 公钥解析token
     *
     * @param token token
     * @return 解析后的公钥
     * @throws Exception 解析公钥异常信息
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
    	return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token token
     * @param pubKeyPath 公钥地址
     * @return 用户信息
     * @throws Exception 从token获取用户信息异常
     */
    public static IJWTInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        return new JWTInfo(body.getSubject(), StringHelper.getObjectValue(body.get(CommonConstant.JWT_KEY_USER_ID)), StringHelper.getObjectValue(body.get(CommonConstant.JWT_KEY_NAME)));
    }

    /**
     * 获取token过期时间
     * @param token token
     * @param pubKeyPath 公钥路径
     * @return token过期时间
     * @throws Exception 异常信息
     */
    public static Date getTokenExpire(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }

}
