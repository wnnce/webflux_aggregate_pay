package com.zeroxn.pay.module.union.web;

import com.zeroxn.pay.core.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: lisang
 * @DateTime: 2023/6/6 下午7:27
 * @Description: 云闪付请求参数实体类
 */
@Schema(description = "云闪付支付参数")
public class UnionParamDTO {
    @NotBlank(message = "商户订单号不能为空", groups = {ValidationGroups.UnionRefundValidation.class, ValidationGroups.UnionRefundValidation.class})
    @Schema(description = "商户系统内的订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;
    @NotNull(message = "订单金额不能为空", groups = {ValidationGroups.UnionWapValidation.class, ValidationGroups.UnionRefundValidation.class})
    @Range(min = 0, message = "订单金额不能低于0", groups = {ValidationGroups.UnionWapValidation.class, ValidationGroups.UnionRefundValidation.class})
    @Schema(description = "订单总金额，单位：分", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer total;
    @NotBlank(message = "返回的商户地址不能为空", groups = ValidationGroups.UnionWapValidation.class)
    @Schema(description = "支付成功后的商户跳转地址，会带上请求参数", requiredMode = Schema.RequiredMode.REQUIRED)
    private String frontUrl;
    @Schema(description = "订单描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;
    @NotBlank(message = "订单的QueryId不能为空", groups = ValidationGroups.UnionRefundValidation.class)
    @Schema(description = "云闪付系统内交易订单的queryId，可从查询接口中获取，订单退款时需要", requiredMode = Schema.RequiredMode.REQUIRED)
    private String queryId;
    @NotNull(message = "订单退款金额不能为空", groups = ValidationGroups.UnionRefundValidation.class)
    @Range(min = 0, message = "订单退款金额不能低于0", groups = ValidationGroups.UnionRefundValidation.class)
    @Schema(description = "订单的退款金额，不能大于订单的支付金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer refundTotal;
    public UnionParamDTO() {}

    public UnionParamDTO(String orderId, Integer total, String frontUrl, String description, String queryId, Integer refundTotal) {
        this.orderId = orderId;
        this.total = total;
        this.frontUrl = frontUrl;
        this.description = description;
        this.queryId = queryId;
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

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Integer getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(Integer refundTotal) {
        this.refundTotal = refundTotal;
    }
}
