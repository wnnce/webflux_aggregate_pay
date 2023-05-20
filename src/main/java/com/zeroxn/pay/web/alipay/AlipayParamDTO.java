package com.zeroxn.pay.web.alipay;

import com.zeroxn.pay.core.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: lisang
 * @DateTime: 2023/5/19 下午12:13
 * @Description: 支付宝支付参数DTO类
 */
@Schema(description = "支付宝支付参数")
public class AlipayParamDTO {
    @NotBlank(message = "商户订单号不能为空", groups = {ValidationGroups.AlipayWapValidation.class,
            ValidationGroups.AlipayDesktopValidation.class, ValidationGroups.AlipayAppletsValidation.class,
            ValidationGroups.AlipayRefundValidation.class})
    @Schema(description = "商户系统内的订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;
    @NotNull(message = "订单金额不能为空", groups = {ValidationGroups.AlipayWapValidation.class,
            ValidationGroups.AlipayDesktopValidation.class, ValidationGroups.AlipayAppletsValidation.class,
            ValidationGroups.AlipayRefundValidation.class})
    @Range(min = 0, message = "订单金额不能低于0", groups = {ValidationGroups.AlipayWapValidation.class,
            ValidationGroups.AlipayDesktopValidation.class, ValidationGroups.AlipayAppletsValidation.class})
    @Schema(description = "订单总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;
    @NotBlank(message = "订单标题不能为空", groups = {ValidationGroups.AlipayWapValidation.class,
            ValidationGroups.AlipayDesktopValidation.class, ValidationGroups.AlipayAppletsValidation.class})
    @Schema(description = "订单标题，不能使用特殊字符", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    @NotBlank(message = "买家支付宝ID不能为空", groups = {ValidationGroups.AlipayAppletsValidation.class})
    @Schema(description = "买家支付宝用户ID，小程序支付需要", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String userId;
    @Schema(description = "手机网站支付，用户付款中途退出返回商户网站的地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String quitUrl;
    @Range(min = 0, max = 4, message = "扫码支付方式错误", groups = ValidationGroups.AlipayDesktopValidation.class)
    @Schema(description = "PC扫码支付的方式, 0:订单码，1：订单码（小），3：订单码（迷你），4：订单码（自定义尺寸），2：订单码-跳转模式（默认）")
    private Integer qrMode;
    @Range(min = 20, message = "二维码宽度最小20", groups = ValidationGroups.AlipayDesktopValidation.class)
    @Schema(description = "自定义二维码宽度，最小20")
    private Integer qrWidth;
    @NotBlank(message = "订单退款ID不能为空", groups = {ValidationGroups.AlipayRefundValidation.class})
    @Schema(description = "订单退款ID，仅订单退款接口需要")
    private String refundId;
    @NotNull(message = "订单退款金额不能为空", groups = {ValidationGroups.AlipayRefundValidation.class})
    @Range(min = 0, message = "订单退款金额不能低于0", groups = {ValidationGroups.AlipayRefundValidation.class})
    @Schema(description = "订单退款金额，单位：分")
    private Integer refundTotal;
    public AlipayParamDTO() {
    }

    public AlipayParamDTO(String orderId, Integer total, String description, String userId, String quitUrl, Integer qrMode, Integer qrWidth, String refundId, Integer refundTotal) {
        this.orderId = orderId;
        this.total = total;
        this.description = description;
        this.userId = userId;
        this.quitUrl = quitUrl;
        this.qrMode = qrMode;
        this.qrWidth = qrWidth;
        this.refundId = refundId;
        this.refundTotal = refundTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuitUrl() {
        return quitUrl;
    }

    public void setQuitUrl(String quitUrl) {
        this.quitUrl = quitUrl;
    }

    public Integer getQrMode() {
        return qrMode;
    }

    public void setQrMode(Integer qrMode) {
        this.qrMode = qrMode;
    }

    public Integer getQrWidth() {
        return qrWidth;
    }

    public void setQrWidth(Integer qrWidth) {
        this.qrWidth = qrWidth;
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
}
