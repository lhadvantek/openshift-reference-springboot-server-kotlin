package no.skatteetaten.aurora.openshift.reference.springboot.service

import java.io.File
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    @Qualifier("default") val s3Client: S3Client,
    s3Properties: S3Properties
) {
    private val s3Bucket = s3Properties.buckets["default"] ?: throw RuntimeException("")

    fun putFileContent(keyName: String, fileContent: String) =
        putFile(keyName, File(keyName).apply { this.writeText(fileContent) } )

    private fun putFile(keyName: String, file: File) {
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        s3Client.putObject(
            PutObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build(),
            RequestBody.fromFile(file)
        )
        file.delete()
    }

    fun getFileContent(keyName: String): String {
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        return s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build()
        ).asUtf8String()
    }
}
