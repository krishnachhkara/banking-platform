package com.krishna.banking.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableSpringDataWebSupport(
        pageSerializationMode =
                EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class PaginationConfig {

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer(){
        return resolver -> resolver.setMaxPageSize(100);
    }
}
