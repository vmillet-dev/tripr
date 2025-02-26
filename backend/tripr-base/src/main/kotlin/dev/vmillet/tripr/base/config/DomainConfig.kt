package dev.vmillet.tripr.base.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EntityScan("dev.vmillet.tripr.base.domain")
@EnableJpaRepositories("dev.vmillet.tripr.base.repos")
@EnableTransactionManagement
class DomainConfig
