package no.skatteetaten.aurora.openshift.reference.springboot.controllers

import com.fasterxml.jackson.databind.JsonNode
import no.skatteetaten.aurora.AuroraMetrics
import no.skatteetaten.aurora.AuroraMetrics.StatusValue.CRITICAL
import no.skatteetaten.aurora.AuroraMetrics.StatusValue.OK
import no.skatteetaten.aurora.openshift.reference.springboot.service.SometimesFailingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

/*
 * An example controller that shows how to do a REST call and how to do an operation with a operations metrics
 * There should be a metric called http_client_requests http_server_requests and operations
 */
@RestController
class ExampleController(
    private val webClient: WebClient,
    private val metrics: AuroraMetrics,
    private val sometimesFailingService: SometimesFailingService
) {

    @GetMapping("/api/example/ip")
    fun ip(): Map<String, Any> {
        val response = webClient
            .get()
            .uri("http://httpbin.org/ip")
            .retrieve()
            .bodyToMono<JsonNode>()
            .block()

        val ip = response?.get("origin")?.textValue() ?: "Ip missing from response"

        return mapOf("ip" to ip)
    }

    @GetMapping("/api/example/sometimes")
    fun example(): Map<String, Any> {
        return metrics.withMetrics(METRIC_NAME) {
            val wasSuccessful = sometimesFailingService.performOperationThatMayFail()
            if (wasSuccessful) {
                metrics.status(METRIC_NAME, OK)
                mapOf("result" to "Sometimes I succeed")
            } else {
                metrics.status(METRIC_NAME, CRITICAL)
                throw RuntimeException("Sometimes I fail")
            }
        }
    }

    companion object {

        private val METRIC_NAME = "sometimes"
    }
}
