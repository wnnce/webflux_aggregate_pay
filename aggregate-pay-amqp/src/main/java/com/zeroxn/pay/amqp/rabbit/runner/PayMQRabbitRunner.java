package com.zeroxn.pay.amqp.rabbit.runner;

import com.zeroxn.pay.core.PayTemplate;
import com.zeroxn.pay.amqp.rabbit.PayMQRabbitQueueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: lisang
 * @DateTime: 2023/6/17 上午11:37
 * @Description:
 */
public class PayMQRabbitRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(PayMQRabbitRunner.class);
    private final ApplicationContext context;
    private final String exchangeName;
    private final AmqpAdmin amqpAdmin;
    private final PayMQRabbitQueueManager queueManager;
    public PayMQRabbitRunner(ApplicationContext context, String exchangeName, AmqpAdmin amqpAdmin,
                             PayMQRabbitQueueManager queueManager){
        this.context = context;
        this.exchangeName = exchangeName;
        this.amqpAdmin = amqpAdmin;
        this.queueManager = queueManager;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始运行Rabbit的自动配置...");
        Map<String, PayTemplate> templateMap = context.getBeansOfType(PayTemplate.class);
        List<PayTemplate> templateList = new ArrayList<>(templateMap.values());
        logger.info("获取支付模板实现类成功，实现类数量：{}", templateList.size());
        templateList.forEach(template -> {
            String name = template.getPlatformName();
            queueManager.addQueue(name);
            String successName = queueManager.getSuccessName(name);
            String refundName = queueManager.getRefundName(name);
            amqpAdmin.declareQueue(new Queue(successName));
            amqpAdmin.declareQueue(new Queue(refundName));
            String successKey = queueManager.getSuccessKey(name);
            String refundKey = queueManager.getRefundKey(name);
            amqpAdmin.declareBinding(
                    new Binding(successName, Binding.DestinationType.QUEUE, exchangeName, successKey, null)
            );
            logger.info("{}绑定Queue到Exchange成功，queueName：{}，bindingKey：{}", name, successName, successKey);
            amqpAdmin.declareBinding(
                    new Binding(refundName, Binding.DestinationType.QUEUE, exchangeName, refundKey, null)
            );
            logger.info("{}绑定Queue到Exchange成功，queueName：{}，bindingKey：{}", name, refundName, refundKey);
        });
        logger.info("Rabbit的自动配置运行结束...");

    }
}
