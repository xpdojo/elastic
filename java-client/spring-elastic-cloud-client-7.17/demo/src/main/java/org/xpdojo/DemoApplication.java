package org.xpdojo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * ComponentScan(basePackages) 설정을 별도로 추가하지 않으면
 * 기본적으로 @SpringBootApplication 애노테이션이 있는 패키지가 basePackage다.
 * 다른 모듈의 Bean을 스캔하려면 동일한 레벨에 위치시킨다.
 */
// @org.springframework.context.annotation.ComponentScan(basePackages = {"org.xpdojo"})
/**
 * A bean with that name has already been defined
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        // 클래스가 JSON 데이터의 모든 프로퍼티를 포함할 필요없다.
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }

}

