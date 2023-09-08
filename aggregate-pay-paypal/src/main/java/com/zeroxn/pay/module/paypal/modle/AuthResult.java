package com.zeroxn.pay.module.paypal.modle;

/**
 * @Author: lisang
 * @DateTime: 2023/7/10 下午3:54
 * @Description: 获取认证Token返回数据DTO
 */
public record AuthResult(String scope, String access_token, String token_type, String app_id, Long expires_in, String nonce) {
}
