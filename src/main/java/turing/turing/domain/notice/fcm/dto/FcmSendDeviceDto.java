package turing.turing.domain.notice.fcm.dto;


import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// DB에서 조회해 오는 FCM Token 값을 조회할 수 있는 DTO
public class FcmSendDeviceDto {
    private String dvcTkn;
    private String category;
}
