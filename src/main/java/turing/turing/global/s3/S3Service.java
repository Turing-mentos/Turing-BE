package turing.turing.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import turing.turing.global.exception.RestApiException;
import turing.turing.global.exception.errorCode.CommonErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service implements FileService{

    private final S3Component s3Component;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(MultipartFile file) {

        // 파일 이름 생성
        String fileName = createFileName(file.getOriginalFilename());

        // 파일 변환
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        // 파일 업로드
        try(InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(s3Component.getBucket(), fileName, inputStream, objectMetadata);
            log.info("File successfully uploaded: {}", fileName);
        } catch (IOException e) {
            log.error("Error uploading file: " + fileName, e);
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }

        return getFileUrl(fileName);
    }

    @Override
    public void deleteFile(String fileUrl) {

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

        // 파일 삭제
        amazonS3Client.deleteObject(s3Component.getBucket(), fileName);
        log.info("File successfully deleted: {}", fileName);
    }

    @Override
    public String getFileUrl(String fileName) {

        return amazonS3Client.getUrl(s3Component.getBucket(), fileName).toString();
    }

    // UUID를 활용해 파일 이름 랜덤 생성
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일의 확장자를 가져옴
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch(StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.", fileName));
        }
    }
}