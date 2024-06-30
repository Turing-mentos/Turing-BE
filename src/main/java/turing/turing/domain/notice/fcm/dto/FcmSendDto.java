package turing.turing.domain.notice.fcm.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
//클라이언트에서 전달 받은 객체
public class FcmSendDto {
    private String token;

    private String title;

    private String body;

    private String category;

}
