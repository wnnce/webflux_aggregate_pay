package com.zeroxn.pay.core.entity;

/**
 * @Author: lisang
 * @DateTime: 2023/4/27 19:23
 * @Description: 下单参数封装类
 */
public class PayParams {
    /**
     * 订单id 商户系统内唯一 微信/支付宝通用
     */
    private final String orderId;
    /**
     * 订单退款id 订单退款时需要 微信/支付宝通用
     */
    private String orderRefundId;
    /**
     * 用户id 微信下单参数叫做 openId 支付宝下单参数叫做 buyer_id
     */
    private String userId;
    /**
     * 商品描述 支付宝下单参数叫做 subject
     */
    private String description;
    /**
     * 订单金额 单位：分 支付宝下单参数叫做 total_amount 支付宝中下单金额以元为单位
     */
    private final Integer total;
    /**
     * 订单退款金额 单位：分 不得大于订单支付金额
     */
    private Integer refundTotal;
    /**
     * 微信H5下单参数 用户的ip地址
     */
    private String ipAddress;
    /**
     * 微信H5下单参数 用户的设备类型 参考：IOS/Android
     */
    private String type;
    /**
     * 支付宝手机网站下单参数 支付中断时返回的地址
     */
    private String quitUrl;
    /**
     * 支付宝电脑下单支付模式
     */
    private String qrMode;
    /**
     * 支付宝电脑下单qrMode == 4 时，自定义收款码尺寸
     */
    private Long qrWidth;
    /**
     * 订单退款描述
     */
    private String refundDescription;

    public PayParams(BuilderWechatApplets builder){
        this.orderId = builder.orderId;
        this.description = builder.description;
        this.userId = builder.openId;
        this.total = builder.total;
    }
    public PayParams(BuilderWechatH5 builder){
        this.orderId = builder.orderId;
        this.description = builder.description;
        this.total = builder.total;
        this.ipAddress = builder.ipAddress;
        this.type = builder.type;
    }
    public PayParams(BuilderAliPayApplets builder){
        this.orderId = builder.orderId;
        this.description = builder.description;
        this.userId = builder.userId;
        this.total = builder.total;
    }
    public PayParams(BuilderAliPayWap builder){
        this.orderId = builder.orderId;
        this.description = builder.description;
        this.total = builder.total;
        this.quitUrl = builder.quitUrl;
    }
    public PayParams(BuilderAlipayDesktop builder){
        this.orderId = builder.orderId;
        this.description = builder.description;
        this.total = builder.total;
        this.qrMode = builder.qrMode;
        this.qrWidth = builder.qrWidth;
    }
    public PayParams(BuilderRefund builder){
        this.orderId = builder.orderId;
        this.orderRefundId = builder.orderRefundId;
        this.total = builder.total;
        this.refundTotal = builder.refundTotal;
        this.refundDescription = builder.refundDescription;
    }
    // 微信支付小程序下单参数
    public static class BuilderWechatApplets{
        private String orderId;
        private String description;
        private String openId;
        private Integer total;
        public BuilderWechatApplets setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderWechatApplets setDescription(String description){
            this.description = description;
            return this;
        }
        public BuilderWechatApplets setOpenId(String openId){
            this.openId = openId;
            return this;
        }
        public BuilderWechatApplets setTotal(Integer total){
            this.total = total;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }
    // 微信支付H5下单参数
    public static class BuilderWechatH5{
        private String orderId;
        private String description;
        private Integer total;
        private String ipAddress;
        private String type;
        public BuilderWechatH5 setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderWechatH5 setDescription(String description){
            this.description = description;
            return this;
        }
        public BuilderWechatH5 setTotal(Integer total){
            this.total = total;
            return this;
        }
        public BuilderWechatH5 setIpAddress(String ipAddress){
            this.ipAddress = ipAddress;
            return this;
        }
        public BuilderWechatH5 setType(String type){
            this.type = type;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }
    // 支付宝支付小程序下单参数
    public static class BuilderAliPayApplets{
        private String orderId;
        private String description;
        private String userId;
        private Integer total;
        public BuilderAliPayApplets setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderAliPayApplets setDescription(String description){
            this.description = description;
            return this;
        }
        public BuilderAliPayApplets setUserId(String userId){
            this.userId = userId;
            return this;
        }
        public BuilderAliPayApplets setTotal(Integer total){
            this.total = total;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }
    // 支付宝支付手机网站下单参数
    public static class BuilderAliPayWap{
        private String orderId;
        private String description;
        private Integer total;
        private String quitUrl;
        public BuilderAliPayWap setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderAliPayWap setDescription(String description){
            this.description = description;
            return this;
        }
        public BuilderAliPayWap setTotal(Integer total){
            this.total = total;
            return this;
        }
        public BuilderAliPayWap setQuitUrl(String quitUrl){
            this.quitUrl = quitUrl;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }
    // 支付宝支付电脑网站下单参数
    public static class BuilderAlipayDesktop{
        private String orderId;
        private String description;
        private Integer total;
        private String qrMode;
        private Long qrWidth;
        public BuilderAlipayDesktop setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderAlipayDesktop setDescription(String description){
            this.description = description;
            return this;
        }
        public BuilderAlipayDesktop setTotal(Integer total){
            this.total = total;
            return this;
        }
        public BuilderAlipayDesktop setQrMode(String qrMode){
            this.qrMode = qrMode;
            return this;
        }
        public BuilderAlipayDesktop setQrWidth(Long qrWidth){
            this.qrWidth = qrWidth;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }
    // 订单退款参数 通用
    public static class BuilderRefund{
        private String orderId;
        private String orderRefundId;
        private Integer total;
        private Integer refundTotal;
        private String refundDescription;
        public BuilderRefund setOrderId(String orderId){
            this.orderId = orderId;
            return this;
        }
        public BuilderRefund setOrderRefundId(String orderRefundId){
            this.orderRefundId = orderRefundId;
            return this;
        }
        public BuilderRefund setTotal(Integer total){
            this.total = total;
            return this;
        }
        public BuilderRefund setRefundTotal(Integer refundTotal){
            this.refundTotal = refundTotal;
            return this;
        }
        public BuilderRefund setRefundDescription(String refundDescription){
            this.refundDescription = refundDescription;
            return this;
        }
        public PayParams build(){
            return new PayParams(this);
        }
    }


    /**
     * 获取微信支付需要的金额
     * @return 返回原始金额
     */
    public Integer getWechatTotal(){
        return this.total;
    }

    /**
     * 获取支付宝支付所需的金额
     * @return 返回原始金额 * 0.01
     */
    public Double getAlipayTotal(){
        return this.total * 0.01;
    }

    /**
     * 获取微信支付订单退款金额
     * @return 返回退款金额
     */
    public Integer getWechatRefundTotal(){
        return this.refundTotal;
    }

    /**
     * 获取支付宝订单退款
     * @return 返回退款金额 * 0.01
     */
    public Double getAlipayRefundTotal(){
        return this.refundTotal * 0.01;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderRefundId() {
        return orderRefundId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getType() {
        return type;
    }

    public String getQuitUrl() {
        return quitUrl;
    }

    public String getQrMode() {
        return qrMode;
    }

    public Long getQrWidth() {
        return qrWidth;
    }

    public String getRefundDescription() {
        return refundDescription;
    }

    @Override
    public String toString() {
        return "PayParam{" +
                "orderId='" + orderId + '\'' +
                ", orderRefundId='" + orderRefundId + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", total=" + total +
                ", refundTotal=" + refundTotal +
                ", ipAddress='" + ipAddress + '\'' +
                ", type='" + type + '\'' +
                ", quitUrl='" + quitUrl + '\'' +
                '}';
    }
}