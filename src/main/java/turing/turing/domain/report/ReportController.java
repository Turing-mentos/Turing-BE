package turing.turing.domain.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.report.dto.ReportReadAllDto;
import turing.turing.domain.report.dto.ReportReqDto;
import turing.turing.domain.report.dto.ReportResDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = " 리포트 생성 API")
    @PostMapping("")
    public ResponseEntity<ReportResDto.CreateDto> createReport(@RequestBody ReportReqDto.CreateDto reportReq){
        return ResponseEntity.ok(reportService.createReport(reportReq));
    }
    @Operation(summary = " 리포트 단일 조회 API")
    @GetMapping("{reportId}")
    public ResponseEntity<ReportResDto.ReadDto> readReport(@PathVariable (name="reportId") Long reportId){
        return ResponseEntity.ok( reportService.readReport(reportId));
    }

    @Operation(summary = " 리포트 전체 조회 API")
    @GetMapping("all")
    public ResponseEntity<List<ReportReadAllDto>> readAllReport(){
        //memberId와 role만 필요 -> Authen~~ 에서 얻어오기 (jwt)
        //이건 예제
        Long member = 1L;
        String memberRole = "TEACHER";
        return ResponseEntity.ok(reportService.readAllReport(member,memberRole));
    }


    @Operation(summary = " 리포트 단락 수정 API")
    @PatchMapping("")
    public ResponseEntity<ReportResDto> updateReport(@RequestBody ReportReqDto.UpdateDto updateDto){
        reportService.updateReport(updateDto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "리포트 최초 진입 시 과외 정보 여부 확인 API ")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkConditionForReport(){

        Long memberId =1L;
        String memberRole ="TEACHER";
        return ResponseEntity.ok(reportService.checkConditionForReport(memberId, memberRole));

    }

}
