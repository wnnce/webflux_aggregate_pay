package com.zeroxn.pay.amqp.kafka;

import com.zeroxn.pay.core.enums.PayPlatform;
import com.zeroxn.pay.core.enums.PayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/18 下午5:29
 * @Description:
 */
public class PayMQKafkaTopicManager {
    private static final Logger logger = LoggerFactory.getLogger(PayMQKafkaTopicManager.class);
    private final String success = PayResult.SUCCESS.getValue();
    private final String refund = PayResult.REFUND.getValue();
    private final Map<String, Integer> partitionMap = new HashMap<>();
    private static int partitionNum = 0;

    public void add(String name){
        partitionMap.put(name + success, partitionNum);
        logger.info("{} - {}：Topic分区添加成功，分区索引：{}", name, success, partitionNum);
        partitionNum ++;
        partitionMap.put(name + refund, partitionNum);
        logger.info("{} - {}：Topic分区添加成功，分区索引：{}", name, success, partitionNum);
        partitionNum ++;
    }
    public Integer getPartition(PayPlatform platform, PayResult result){
        return partitionMap.get(platform.getValue() + result.getValue());
    }
    public Integer getSuccessPartition(PayPlatform platform){
        return partitionMap.get(platform.getValue() + success);
    }
    public Integer getRefundPartition(PayPlatform platform){
        return partitionMap.get(platform.getValue() + refund);
    }
    public Integer getSuccessPartition(String name){
        return partitionMap.get(name + success);
    }
    public Integer getRefundPartition(String name){
        return partitionMap.get(name + refund);
    }
    public int getPartitionSize(){
        return partitionMap.size();
    }
}
