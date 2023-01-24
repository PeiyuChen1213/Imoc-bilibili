package com.bilibili.service.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilibili.domain.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 返回token的工具类
 * @date 1/22/2023 3:30 PM
 */
public class TokenUtil {

    private static String generateToken(Long id, int TimeUnit, int expireTimeInterval) throws Exception {
        //写一个相关的算法
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        //和token过期时间相关
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //设置token 30s过期
        calendar.add(TimeUnit, expireTimeInterval);
        return JWT.create().withKeyId(String.valueOf(id)).withIssuer("chenpeiyu")
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
    }

    /**
     * token 解密
     *
     * @param token
     * @return 返回用户id userId
     * @throws Exception
     */
    public static Long verifyToken(String token) {
        try {
            //写一个相关的算法
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            //jwt token解密
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            //这里的keyId就是userId
            String userId = verify.getKeyId();
            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555", "token 过期！");
        } catch (Exception e) {
            throw new ConditionException("非法用户的token!");
        }
    }

    public static String generateAccessToken(Long id) throws Exception {
        return generateToken(id, Calendar.HOUR, 1);
    }

    public static String generateRefreshToken(Long id) throws Exception {
        return generateToken(id, Calendar.DAY_OF_MONTH, 7);
    }
}
