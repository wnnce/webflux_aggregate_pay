package com.zeroxn.pay.core.amqp.kafka.runner;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.core.amqp.kafka.PayMQKafkaTopicManager;
import com.zeroxn.pay.core.config.ModuleRegistry;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.TopicBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/18 下午5:19
 * @Description:
 */
public class PayMQKafkaRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(PayMQKafkaRunner.class);
    private final ModuleRegistry moduleRegistry;
    private final PayMQKafkaTopicManager topicManager;
    private final AdminClient adminClient;
    private final String topicName;
    public PayMQKafkaRunner(String topicName, ModuleRegistry moduleRegistry, AdminClient adminClient, PayMQKafkaTopicManager topicManager){
        this.moduleRegistry = moduleRegistry;
        this.adminClient = adminClient;
        this.topicManager = topicManager;
        this.topicName = topicName;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始运行Kafka的自动配置...");
        List<String> moduleNames = moduleRegistry.getModuleNames();
        logger.info("获取支付模块成功，模块数量：{}", moduleNames.size());
        moduleNames.forEach(topicManager::add);
        int partitionSize = topicManager.getPartitionSize();
        NewTopic newTopic = TopicBuilder
                .name(topicName)
                .partitions(partitionSize)
                .replicas(1)
                .build();
        adminClient.createTopics(Collections.singleton(newTopic));
        logger.info("KafKa Topic创建成功，name：{}，partitions：{}，replicas：1", topicName, partitionSize);
        adminClient.close();
        logger.info("Kafka自动配置运行结束...");
    }
}
