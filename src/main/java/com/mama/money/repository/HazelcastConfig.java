package com.mama.money.repository;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.mama.money.model.MoneyTransferRequest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HazelcastConfig {

    @Bean
    public Config hazelCastConfig() {
        final Config config = new Config();
        final MapConfig mapConf = new MapConfig();
        mapConf.setTimeToLiveSeconds(300);
        config.getMapConfigs().put("moneyTransferRequest", mapConf);
        return config;
    }

}
