package com.leyou.cart.properties;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @Author: 98050
 * @Time: 2018-10-25 16:12
 * @Feature: jwt属性
 */
//@ConfigurationProperties(prefix = "leyou.jwt")
@Configuration
@RefreshScope
public class JwtProperties {
    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 公钥地址
     */
    @Value("${leyou.jwt.pubKeyPath}")
    private String pubKeyPath;

    /**
     * cookie名字
     */
    @Value("${leyou.jwt.cookieName}")
    private String cookieName;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * @PostConstruct :在构造方法执行之后执行该方法
     */
    @PostConstruct
    public void init(){
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("获取公钥失败！", e);
            throw new RuntimeException();
        }
    }
}
