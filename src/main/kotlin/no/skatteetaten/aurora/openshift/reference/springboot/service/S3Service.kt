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
    @Qualifier("default") val s3Bucket: S3Properties.S3Bucket
) {
    init {
        putHostFile()
    }

    fun putHostFile() {
        val file = File("/etc/hosts")
        putFile(file.name, file)
        println(getFileContent(file.name))
    }

    fun putFile(keyName: String, file: File) {
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        s3Client.putObject(
            PutObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build(),
            RequestBody.fromFile(file)
        )
    }

    fun getFileContent(keyName: String): String {
        val fullKeyName = "${s3Bucket.objectPrefix}/$keyName"
        return s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(s3Bucket.bucketName).key(fullKeyName).build()
        ).asUtf8String()
    }
}
