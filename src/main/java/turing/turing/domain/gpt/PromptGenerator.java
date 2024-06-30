package turing.turing.domain.gpt;


import org.springframework.stereotype.Service;
import turing.turing.domain.report.dto.ReportReqDto;

@Service
public class PromptGenerator {


    public static final String generatePrompt1(ReportReqDto reportReq) {
        return "과외 학생에 대한 리포트를 생성할거야. 학부모 전달하기 위한 용도야!!!.존댓말로 써줘. 마무리 멘트는 필요없어\n" +
                "학생 이름은 " + reportReq.getName() +
                ", 학생 피드백으로는 " + reportReq.getComment() + "이라고 말하고 싶어.\n 인사말, 지난 수업 진행 방식 이라는 소제목 2개를 생성해 구분하여 생성해줘. 소제목에는 [] 붙여줘 \n 말투가 너무 딱딱하지 않게, 사람이 쓴 것 처럼 정성스레 작성해줘";
    }

    public static final String generatePrompt2(ReportReqDto reportReq) {
        return "과외 학생에 대한 리포트를 생성할거야. 학부모 전달하기 위한 용도야!!. 존댓말로 써줘, 그리고 적극성, 성실성, 집중도를 기준으로 '" + reportReq.getAttitude() + "'에 해당 돼.\n" +
                "마무리 멘트로 학부모에게 전할 당부의 말씀으로는 '" + reportReq.getRequest() + "' 라는 내용을 말하고 싶어.\n" +
                "과외비는 '30만원'이고 입금기한은 '5월 30일'이야.\n 학생 피드백, 과외비, 마무리 멘트로 3개의 소제목을 생성해 구분하여 생성해줘.\n 소제목에는 [] 붙여줘 말투가 너무 딱딱하지 않게, 사람이 쓴 것 처럼 정성스레 작성해줘";
    }
}