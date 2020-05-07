package no.skatteetaten.aurora.openshift.reference.springboot

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Bean
    fun webClient(builder: WebClient.Builder): WebClient = builder.build()
}
