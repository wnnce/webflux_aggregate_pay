package com.zeroxn.pay.module.jdpay.web;

import com.zeroxn.pay.module.jdpay.validation.JdPayValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: lisang
 * @DateTime: 2023-09-11 10:30:31
 * @Description: 京东支付参数Dto类
 */
public class JdPayParamsDto {
    @NotBlank(message = "订单ID不能为空", groups = {JdPayValidation.ConfirmValidation.class, JdPayValidation.RefundValidation.class})
    @Schema(description = "商户系统内的订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;
    @NotBlank(message = "商品名称不能为空", groups = {JdPayValidation.ConfirmValidation.class})
    @Schema(description = "商品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tradeName;
    @Schema(description = "商品详情", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tradeDesc;
    @NotNull(message = "订单金额不能为空", groups = {JdPayValidation.ConfirmValidation.class, JdPayValidation.RefundValidation.class})
    @Range(min = 0, message = "订单金额不能小于0", groups = {JdPayValidation.ConfirmValidation.class, JdPayValidation.RefundValidation.class})
    @Schema(description = "订单金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;

    @NotBlank(message = "订单退款金额不能为空", groups = JdPayValidation.RefundValidation.class)
    @Range(min = 0, message = "订单退款金额不能小于0", groups = JdPayValidation.RefundValidation.class)
    @Schema(description = "订单退款金额，不能大于订单的支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer refundAmount;
    @NotNull(message = "订单类型不能为空", groups = {JdPayValidation.ConfirmValidation.class})
    @Range(min = 0, max = 1, message = "订单类型错误", groups = {JdPayValidation.ConfirmValidation.class})
    @Schema(description = "订单类型，0：实物、1：虚拟", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer orderType;
    @Schema(description = "支付成功后的跳转Url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String returnUrl;
    @NotBlank(message = "用户标识不能为空", groups = JdPayValidation.ConfirmValidation.class)
    @Schema(description = "商户系统内唯一的用户标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;
    @NotBlank(message = "业务类型不能为空", groups = JdPayValidation.ConfirmValidation.class)
    @Schema(description = "支付的业务类型，查询京东支付的通道类型定义", requiredMode = Schema.RequiredMode.REQUIRED)
    private String channelType;

    @NotNull(message = "支付平台类型不能为空", groups = JdPayValidation.ConfirmValidation.class)
    @Range(min = 0, max = 1, message = "支持平台类型参数错误", groups = JdPayValidation.ConfirmValidation.class)
    @Schema(description = "支付平台类型，0：H5、1：PC")
    private Integer terraceType;

    @NotBlank(message = "退款流水号不能为空", groups = JdPayValidation.RefundValidation.class)
    @Schema(description = "订单的退款流水号，订单多次退款流水号需不同", requiredMode = Schema.RequiredMode.REQUIRED)
    private String refundId;

    public JdPayParamsDto(){}

    public JdPayParamsDto(String orderId, String tradeName, String tradeDesc, Integer total, Integer refundAmount, Integer orderType, String returnUrl, String userId, String channelType, Integer terraceType, String refundId) {
        this.orderId = orderId;
        this.tradeName = tradeName;
        this.tradeDesc = tradeDesc;
        this.total = total;
        this.refundAmount = refundAmount;
        this.orderType = orderType;
        this.returnUrl = returnUrl;
        this.userId = userId;
        this.channelType = channelType;
        this.terraceType = terraceType;
        this.refundId = refundId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public Integer getTerraceType() {
        return terraceType;
    }

    public void setTerraceType(Integer terraceType) {
        this.terraceType = terraceType;
    }

    public Integer getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
    }
}
