package turing.turing.global.s3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

}