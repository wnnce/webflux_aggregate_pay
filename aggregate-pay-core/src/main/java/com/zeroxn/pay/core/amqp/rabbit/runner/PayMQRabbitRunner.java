package com.zeroxn.pay.core.amqp.rabbit.runner;

import com.zeroxn.pay.core.amqp.rabbit.PayMQRabbitQueueManager;
import com.zeroxn.pay.core.register.ModuleRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: lisang
 * @DateTime: 2023/6/17 上午11:37
 * @Description:
 */
public class PayMQRabbitRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(PayMQRabbitRunner.class);
    private final ModuleRegistry moduleRegistry;
    private final String exchangeName;
    private final AmqpAdmin amqpAdmin;
    private final PayMQRabbitQueueManager queueManager;
    public PayMQRabbitRunner(ModuleRegistry moduleRegistry, String exchangeName, AmqpAdmin amqpAdmin,
                             PayMQRabbitQueueManager queueManager){
        this.moduleRegistry = moduleRegistry;
        this.exchangeName = exchangeName;
        this.amqpAdmin = amqpAdmin;
        this.queueManager = queueManager;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始运行Rabbit的自动配置...");
        List<String> moduleNames = moduleRegistry.getModuleNames();
        if (CollectionUtils.isEmpty(moduleNames)){
            logger.warn("自动配置结束，没有已注册的支付模块...");
            return;
        }
        logger.info("获取支付模板成功，注册模块数量：{}", moduleNames.size());
        moduleNames.forEach(moduleName -> {
            queueManager.addQueue(moduleName);
            String successName = queueManager.getSuccessName(moduleName);
            String refundName = queueManager.getRefundName(moduleName);
            amqpAdmin.declareQueue(new Queue(successName));
            amqpAdmin.declareQueue(new Queue(refundName));
            String successKey = queueManager.getSuccessKey(moduleName);
            String refundKey = queueManager.getRefundKey(moduleName);
            amqpAdmin.declareBinding(
                    new Binding(successName, Binding.DestinationType.QUEUE, exchangeName, successKey, null)
            );
            logger.info("{}绑定Queue到Exchange成功，queueName：{}，bindingKey：{}", moduleName, successName, successKey);
            amqpAdmin.declareBinding(
                    new Binding(refundName, Binding.DestinationType.QUEUE, exchangeName, refundKey, null)
            );
            logger.info("{}绑定Queue到Exchange成功，queueName：{}，bindingKey：{}", moduleName, refundName, refundKey);
        });
        logger.info("Rabbit的自动配置运行结束...");
    }
}
