package com.ttmo.ms.common.kit.springboot.starter.dynamic.query;


import com.ttmo.ms.common.kit.dynamic.query.aspect.DynamicQueryAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author Jover Zhang
 */
@Slf4j
@Configuration
public class MsCommonKitDynamicQueryAutoConfigure {

    @Value("${ttmo.banner:false}")
    boolean showBanner;

    @Bean
    @ConditionalOnMissingBean
    public DynamicQueryAspect dynamicQueryAspect() {
        log.info("Init ms-common-kit-dynamic-query");
        return new DynamicQueryAspect(showBanner);
    }

}
