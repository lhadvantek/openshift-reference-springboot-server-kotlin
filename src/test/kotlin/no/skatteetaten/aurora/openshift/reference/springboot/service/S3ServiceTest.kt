package no.skatteetaten.aurora.openshift.reference.springboot.service

import io.findify.s3mock.S3Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class S3ServiceTest {

    @Autowired
    private lateinit var s3Client: S3Client

    @Autowired
    private lateinit var s3Service: S3Service

    private val s3Mock = S3Mock.Builder().withInMemoryBackend().withPort(9000).build()

    @BeforeAll
    fun setup() {
        s3Mock.start()
        s3Client.createBucket(CreateBucketRequest.builder().bucket("default").build())
    }

    @Test
    fun `verify is able to put and get file from s3`() {
        val expectedFileContent = "my awesome test file"

        assertDoesNotThrow {
            s3Service.putFileContent("myFile.txt", expectedFileContent)
        }

        val fileContent = assertDoesNotThrow {
            s3Service.getFileContent("myFile.txt")
        }


        assertThat(fileContent).isEqualTo(expectedFileContent)
    }
}