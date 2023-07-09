package com.zeroxn.pay.module.wechat.web;

import com.zeroxn.pay.core.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: lisang
 * @DateTime: 2023/5/21 下午12:01
 * @Description: 微信支付参数DTO类
 */
@Schema(description = "微信支付参数")
public class WechatParamDTO {
    /**
     * 商户系统内的订单ID 必需
     */
    @NotBlank(message = "订单ID不能为空", groups = {ValidationGroups.WechatH5Validation.class,
            ValidationGroups.WechatAppletsValidation.class, ValidationGroups.WechatRefundValidation.class})
    @Schema(description = "商户系统内的订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;
    /**
     * 退款单号
     */
    @NotBlank(message = "订单退款ID不能为空", groups = {ValidationGroups.WechatRefundValidation.class})
    @Schema(description = "订单的退款ID，请求退款接口时必需", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refundId;
    /**
     * 退款金额 单位：分
     */
    @NotNull(message = "订单退款金额不能为空", groups = {ValidationGroups.WechatRefundValidation.class})
    @Range(min = 0, message = "订单退款金额不能小于0", groups = ValidationGroups.WechatRefundValidation.class)
    @Schema(description = "订单的退款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer refundTotal;
    /**
     * 小程序下单需要 微信系统内的用户ID
     */
    @NotBlank(message = "小程序支付用户ID不能为空", groups = {ValidationGroups.WechatAppletsValidation.class})
    @Schema(description = "微信支付小程序支付买家的用户ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String userId;
    /**
     * 订单描述
     */
    @NotBlank(message = "订单描述不能为空", groups = {ValidationGroups.WechatH5Validation.class,
            ValidationGroups.WechatAppletsValidation.class, ValidationGroups.WechatRefundValidation.class})
    @Schema(description = "订单描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    /**
     * 订单总金额，单位：分
     */
    @NotNull(message = "订单金额不能为空", groups = {ValidationGroups.WechatH5Validation.class,
            ValidationGroups.WechatAppletsValidation.class, ValidationGroups.WechatRefundValidation.class})
    @Range(min = 0, message = "订单金额不能低于0", groups = {ValidationGroups.WechatH5Validation.class,
            ValidationGroups.WechatAppletsValidation.class, ValidationGroups.WechatRefundValidation.class})
    @Schema(description = "订单总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;
    /**
     * 用户IP地址 H5下单必需
     */
    @NotBlank(message = "H5下单IP地址不能为空", groups = {ValidationGroups.WechatH5Validation.class})
    @Schema(description = "微信支付H5下单买家的IP地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ipAddress;
    /**
     * H5下单的场景类型 必需 例如：Android、IOS、Wap
     */
    @NotBlank(message = "H5下单场景类型不能为空", groups = {ValidationGroups.WechatH5Validation.class})
    @Schema(description = "微信支付H5下单的场景类型，例如：Android、IOS、Wap", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;
    /**
     * H5下单的应用名称
     */
    @Schema(description = "微信支付H5下单的应用名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String appName;
    /**
     * H5下单的网站链接
     */
    @Schema(description = "微信支付H5下单的网站链接", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String appUrl;
    /**
     * H5下单的IOS bundleID
     */
    @Schema(description = "微信支付H5下单IOS的bundleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bundleId;
    /**
     * H5下单的Android平台软件包名
     */
    @Schema(description = "微信支付H5下单的Android包名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String packageName;
    /**
     * 退款原因
     */
    @Schema(description = "退款原因", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reason;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Integer getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(Integer refundTotal) {
        this.refundTotal = refundTotal;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
