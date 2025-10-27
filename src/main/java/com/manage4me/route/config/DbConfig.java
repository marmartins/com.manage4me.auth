package com.manage4me.route.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan({"import com.manage4me.route.entities"})
@EnableJpaAuditing
@EnableTransactionManagement
public class DbConfig {

}
