package turing.turing.global.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("turing.turing.domain.auth.apple")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class OpenFeignConfig {

}
