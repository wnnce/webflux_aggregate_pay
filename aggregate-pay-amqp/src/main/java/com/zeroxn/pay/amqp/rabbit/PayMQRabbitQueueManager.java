package com.zeroxn.pay.amqp.rabbit;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/17 上午11:52
 * @Description: RabbitMQ的queue管理类 负责queue name、key的管理和获取
 */
public class PayMQRabbitQueueManager {
    private final String namePrefix;
    private final String keyPrefix;
    private final String success = PayResult.SUCCESS.getValue();
    private final String refund = PayResult.REFUND.getValue();
    private final Map<String, String> nameMap = new HashMap<>();
    private final Map<String, String> keyMap = new HashMap<>();
    public PayMQRabbitQueueManager(String namePrefix, String keyPrefix){
        this.namePrefix = namePrefix;
        this.keyPrefix = keyPrefix;
    }

    public void addQueue(String name){
        this.add(name);
    }
    public void addQueue(PayPlatform platform){
        String name = platform.getValue();
        this.add(name);
    }
    public String getQueueName(PayPlatform platform, PayResult result){
        return nameMap.get(platform.getValue() + result.getValue());
    }
    public String getQueueKey(PayPlatform platform, PayResult result){
        return keyMap.get(platform.getValue() + result.getValue());
    }
    public String getSuccessKey(String name){
        return keyMap.get(name + PayResult.SUCCESS.getValue());
    }
    public String getSuccessName(String name){
        return nameMap.get(name + PayResult.SUCCESS.getValue());
    }
    public String getRefundName(String name){
        return nameMap.get(name + PayResult.REFUND.getValue());
    }
    public String getRefundKey(String name){
        return keyMap.get(name + PayResult.REFUND.getValue());
    }

    private void add(String name){
        String successKey = name + success;
        String refundKey = name + refund;
        nameMap.put(successKey, namePrefix + "." + name + "." + success);
        nameMap.put(refundKey, namePrefix + "." + name + "." + refund);
        keyMap.put(successKey, keyPrefix + "-" + name + "-" + success);
        keyMap.put(refundKey, keyPrefix + "-" + name + "-" + refund);
    }
}
