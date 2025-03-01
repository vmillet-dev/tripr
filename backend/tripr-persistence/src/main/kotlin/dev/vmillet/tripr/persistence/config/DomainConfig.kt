package dev.vmillet.tripr.persistence.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EntityScan("dev.vmillet.tripr.persistence.domain")
@EnableJpaRepositories("dev.vmillet.tripr.persistence.repository")
@EnableTransactionManagement
open class DomainConfig
