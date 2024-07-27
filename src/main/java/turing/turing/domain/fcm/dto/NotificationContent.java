package turing.turing.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationContent {
    private String title;
    private String body;
    private Long targetId;

}
