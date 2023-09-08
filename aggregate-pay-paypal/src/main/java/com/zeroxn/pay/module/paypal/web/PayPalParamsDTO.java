package com.zeroxn.pay.module.paypal.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: lisang
 * @DateTime: 2023-09-08 13:14:42
 * @Description: PayPal下单支付参数DTO类
 */
@Schema(description = "PayPal下单支付参数类")
public class PayPalParamsDTO {
    @NotBlank(message = "预扣款ID不能为空")
    @Schema(description = "订单的预扣款ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String referenceId;
    @Schema(description = "订单描述信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @NotNull(message = "订单金额不能为空")
    @Range(min = 0, message = "订单金额不能低于0")
    @Schema(description = "订单的总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;
    @NotBlank(message = "同意支付跳转URL不能为空")
    @Schema(description = "用户同意扣款后的跳转URL，跳转时会带上PayPal生成的Token和订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String returnUrl;
    @NotBlank(message = "取消支付跳转URL不能为空")
    @Schema(description = "用户不同意扣款跳转的URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cancelUrl;

    public PayPalParamsDTO(){}

    public PayPalParamsDTO(String referenceId, String description, Integer total, String returnUrl, String cancelUrl) {
        this.referenceId = referenceId;
        this.description = description;
        this.total = total;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
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

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}
