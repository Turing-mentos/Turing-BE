package turing.turing.global.s3;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    //파일 업로드
    String uploadFile(MultipartFile file);

    //파일 삭제
    void deleteFile(String fileUrl);

    //파일 URL 조회
    String getFileUrl(String fileName);

}