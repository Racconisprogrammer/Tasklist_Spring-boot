package com.example.tasklist.service.impl;

import com.example.tasklist.config.TestConfig;
import com.example.tasklist.domain.task.TaskImage;
import com.example.tasklist.service.props.MinioProperties;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskImageServiceImplTest {

    @MockBean
    private MinioClient minioClient;

    @MockBean
    private MinioProperties minioProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TaskImageServiceImpl taskImageService;

    MultipartFile file = null;
    InputStream inputStream = null;


    @Test
    @SneakyThrows
    void upload() {
        // Mock bucket name
        Mockito.when(minioProperties.getBucket())
                .thenReturn("test-bucket");

        // Mock file
        String fileName = "test.jpg";
        byte[] content = "test".getBytes();
        MultipartFile file = new MockMultipartFile(
                fileName,
                fileName,
                "image/jpeg",
                content
        );
        TaskImage taskImage = new TaskImage();
        taskImage.setFile(file);
        InputStream inputStream = new ByteArrayInputStream(content);

        // Upload
        String uploadedFileName = taskImageService.upload(taskImage);

        // Verify
        Assertions.assertNotNull(uploadedFileName);
        Assertions.assertTrue(uploadedFileName.endsWith(".jpg"));
    }
}

