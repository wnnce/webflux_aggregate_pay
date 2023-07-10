package com.zeroxn.pay.module.union.config;

import com.zeroxn.pay.module.union.constant.UnionConstant;
import com.zeroxn.pay.module.union.utils.UnionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @Author: lisang
 * @DateTime: 2023/6/1 上午11:46
 * @Description: 云闪付配置类
 */
@ConfigurationProperties(prefix = "pay.union")
public class UnionPayProperties {
    private static final Logger logger = LoggerFactory.getLogger(UnionPayProperties.class);
    /**
     * 编码方式 支持UTF-8和GBK 默认UTF-8
     */
    private String charset;
    /**
     * 签名方法 01：RSA、11：SHA-256、12：SM3  默认RSA
     */
    private String signType;
    /**
     * 用于签名的加密字符串 SHA-256和SM3签名时必传
     */
    private String signKey;
    /**
     * 签名证书路径 支持 classpath:路径和绝对路径
     */
    private String signCertPath;
    /**
     * 签名证书密码
     */
    private String signCertPwd;
    /**
     * 签名证书类型 默认PKCS12
     */
    private String signCertType;
    /**
     * 敏感加密证书路径
     */
    private String encryptCertPath;
    /**
     * 中级证书路径
     */
    private String middleCertPath;
    /**
     * 根证书路径
     */
    private String rootCertPath;
    /**
     * 云闪付系统内的商户ID
     */
    private String merchantId;
    /**
     * 交易的币种 默认为人民币CNY：156
     */
    private String currency;
    /**
     * 支付成功后云闪付的异步通知地址
     */
    private String successNotifyUrl;
    /**
     * 退款成功后云闪付的异步通知地址
     */
    private String refundNotifyUrl;

    public UnionPayProperties(){}

    @ConstructorBinding
    public UnionPayProperties(@DefaultValue("UTF-8") String charset, @DefaultValue("01") String signType,
                              String signKey, String signCertPath,String signCertPwd, @DefaultValue("PKCS12") String signCertType,
                              String encryptCertPath, String middleCertPath, String rootCertPath, @NotNull String merchantId,
                              @DefaultValue("156") String currency, @NotNull String successNotifyUrl, @NotNull String refundNotifyUrl) throws Exception{
        this.charset = charset;
        this.signKey = signKey;
        this.signType = signType;
        this.signCertPath = signCertPath;
        this.signCertPwd = signCertPwd;
        this.signCertType = signCertType;
        this.encryptCertPath = encryptCertPath;
        this.middleCertPath = middleCertPath;
        this.rootCertPath = rootCertPath;
        this.merchantId = merchantId;
        this.currency = currency;
        this.successNotifyUrl = successNotifyUrl;
        this.refundNotifyUrl = refundNotifyUrl;
        // 验证数据是否被正确加载
        this.initValidated();
    }
    private void initValidated(){
        if(UnionUtils.isEmpty(signType)){
            logger.error("云闪付初始化失败，签名方式为空！");
            throw new NullPointerException("数据签名方式不能为空");
        }
        if(UnionConstant.SIGN_METHOD_RSA.equals(signType)){
            if(UnionUtils.isEmpty(signCertPath) || UnionUtils.isEmpty(signCertPwd)){
                logger.error("云闪付初始化失败，签名方式为RSA时，签名证书路径和密码不能为空！");
                throw new NullPointerException("签名证书路径和密码不能为空");
            }
        } else if (UnionConstant.SIGN_METHOD_SHA256.equals(signType) || UnionConstant.SIGN_METHOD_SM3.equals(signType)) {
            if(UnionUtils.isEmpty(signKey)){
                logger.error("云闪付初始化失败，签名方式为SHA-256或SM3时，密钥字符串不能为空！");
                throw new NullPointerException("数据加密密钥字符串不能为空");
            }
        }else {
            logger.error("云闪付初始化失败，不支持的加密方式");
            throw new RuntimeException("云闪付加密方式有误");
        }
    }

    public String getCharset() {
        return charset;
    }

    public String getSignType() {
        return signType;
    }

    public String getSignKey() {
        return signKey;
    }

    public String getSignCertPath() {
        return signCertPath;
    }

    public String getSignCertPwd() {
        return signCertPwd;
    }

    public String getSignCertType() {
        return signCertType;
    }

    public String getEncryptCertPath() {
        return encryptCertPath;
    }

    public String getMiddleCertPath() {
        return middleCertPath;
    }

    public String getRootCertPath() {
        return rootCertPath;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSuccessNotifyUrl() {
        return successNotifyUrl;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }
}
