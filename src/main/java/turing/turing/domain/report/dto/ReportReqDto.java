package turing.turing.domain.report.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class ReportReqDto {

    private Long studyRoomId;

    private String name;

    private String subject;

    private String comment;

    private String attitude;

    private String request;

    private boolean pay;


}
