package com.zeroxn.pay.core.enums;

/**
 * @Author: lisang
 * @DateTime: 2023/4/28 08:24
 * @Description: 支付方式枚举类，APPLETS微信支付宝都支付 WAP和DESKTOP只有支付宝支持 微信如果需要调用H5支付 选择WAP或DESKTOP均可
 */
public enum PayMethod {
    APPLETS,
    WAP,
    DESKTOP
}
