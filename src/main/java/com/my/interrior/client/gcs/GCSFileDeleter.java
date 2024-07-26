package com.my.interrior.client.gcs;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GCSFileDeleter {

    private final Storage storage;

    @Autowired
    public GCSFileDeleter(Storage storage) {
        this.storage = storage;
    }

    public void deleteFile(String fileUrl) {
        // URL에서 객체 이름과 폴더 이름 추출
        String[] urlParts = fileUrl.split("/", 4);
        String bucketName = urlParts[2];  // 버킷 이름은 URL의 3번째 부분
        String objectName = urlParts[3];  // 객체 이름은 URL의 4번째 부분

        // BlobId 생성
        BlobId blobId = BlobId.of(bucketName, objectName);

        // 파일 삭제
        boolean deleted = storage.delete(blobId);
        if (deleted) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File not found.");
        }
    }
}
