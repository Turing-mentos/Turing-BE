package turing.turing.domain.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ReportResDto> createReport(@RequestBody ReportReqDto reportReq){
        reportService.createReport(reportReq);
        return ResponseEntity.ok(null);

    }
    @Operation(summary = " 리포트 단일 조회 API")
    @GetMapping("{reportId}")
    public ResponseEntity<ReportResDto> readReport(@PathVariable (name="reportId") Long reportId){
        ReportResDto reportResDto = reportService.readReport(reportId);
        return ResponseEntity.ok(reportResDto);
    }

    @Operation(summary = " 리포트 전체 조회 API")
    @GetMapping("all/{studyRoomId}")
    public ResponseEntity<List<ReportResDto>> readAllReport(@PathVariable (name="studyRoomId") Long studyRoomId){
        List<ReportResDto> reportResDto = reportService.readAllReport(studyRoomId);
        return ResponseEntity.ok(reportResDto);
    }

    @Operation(summary = " 리포트 단락 삭제 API")
    @DeleteMapping("{reportId}/{paragraphNum}")
    public ResponseEntity<ReportResDto> deleteReport(@PathVariable (name="reportId") Long reportId, @PathVariable (name="paragraphNum") int paragraphNum ){
         reportService.deleteReport(reportId, paragraphNum);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = " 리포트 단락 수정 API")
    //requestBody만 사용해도 되지만 path variable 사용: delete와 일관성을 위해
    @PatchMapping("{reportId}/{paragraphNum}")
    public ResponseEntity<ReportResDto> updateReport(@PathVariable (name="reportId") Long reportId, @PathVariable (name="paragraphNum") int paragraphNum,
                                                     @RequestBody String  reportUpdateContent){
        reportService.updateReport(reportId, paragraphNum, reportUpdateContent);
        return ResponseEntity.ok(null);
    }
}
