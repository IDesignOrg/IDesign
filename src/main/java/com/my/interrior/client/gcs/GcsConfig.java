package com.my.interrior.client.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
// GCS 토큰을 받기위해 만듬
@Configuration
public class GcsConfig {
	
	/**
     * Bean 정의 메서드: Google Cloud Storage 클라이언트를 생성합니다.
     * @return Google Cloud Storage 클라이언트 객체
     * @throws IOException JSON 인증 파일을 읽는 동안 발생할 수 있는 예외
     */
    @Bean
    public Storage storage() throws IOException {
        // ClassPathResource 객체를 생성하여 JSON 인증 파일의 경로를 지정합니다.
        // 파일 이름은 필요에 따라 수정할 수 있습니다.
        ClassPathResource credentialsPath = new ClassPathResource("persuasive-feat-426613-s5-a789fe6d3bc5.json");

        // 지정된 경로에서 JSON 인증 파일을 읽기 위해 InputStream을 엽니다.
        InputStream credentialsStream = credentialsPath.getInputStream();

        // InputStream을 사용하여 GoogleCredentials 객체를 생성합니다.
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        // GoogleCredentials 객체를 사용하여 StorageOptions 빌더를 구성하고
        // Google Cloud Storage 클라이언트를 생성합니다.
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
