package no.skatteetaten.aurora.openshift.reference.springboot.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "s3")
@ConstructorBinding
data class S3Properties(
    val buckets: Map<String, S3Bucket>
) {
    data class S3Bucket(
        val serviceEndpoint: String,
        val accessKey: String,
        val secretKey: String,
        val objectPrefix: String,
        val bucketRegion: String,
        val bucketName: String
    )
}
