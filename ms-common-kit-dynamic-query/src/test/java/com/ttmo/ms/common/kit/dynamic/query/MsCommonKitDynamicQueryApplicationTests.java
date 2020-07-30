package com.ttmo.ms.common.kit.dynamic.query;

import com.ttmo.ms.common.kit.dynamic.query.aspect.DynamicQueryAspect;
import com.ttmo.ms.common.kit.dynamic.query.repository.TargetRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
class MsCommonKitDynamicQueryApplicationTests {

    @Bean
    public DynamicQueryAspect dynamicQueryAspect() {
        return new DynamicQueryAspect();
    }

    @Bean
    public TargetRepository targetRepository() {
        return new TargetRepository();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(MsCommonKitDynamicQueryApplicationTests.class);

        TargetRepository targetRepository = run.getBean(TargetRepository.class);

        System.out.println("Start test:\n==============================");

        System.out.println("lower_camel, lower_underscore"
            .equals(targetRepository.targetMethod("lowerCamel, lower_underscore")));

        System.out.println("lower_camel,lower_underscore"
            .equals(targetRepository.targetMethod(new String[]{"lowerCamel", "lower_underscore"})));
    }

}
