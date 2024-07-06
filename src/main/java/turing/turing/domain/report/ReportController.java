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
    public ResponseEntity<ReportResDto.CreateDto> createReport(@RequestBody ReportReqDto.CreateDto reportReq){
        return ResponseEntity.ok(reportService.createReport(reportReq));
    }
    @Operation(summary = " 리포트 단일 조회 API")
    @GetMapping("{reportId}")
    public ResponseEntity<ReportResDto.ReadDto> readReport(@PathVariable (name="reportId") Long reportId){
        return ResponseEntity.ok( reportService.readReport(reportId));
    }

    @Operation(summary = " 리포트 전체 조회 API")
    @GetMapping("all/{studyRoomId}")
    public ResponseEntity<List<ReportResDto.ReadListDto>> readAllReport(){
        //memberId와 role만 필요 -> Authen~~ 에서 얻어오기 (jwt)
        //이건 예제
        Long member = 1L;
        String memberRole = "TEACHER";
        List<ReportResDto.ReadListDto> reportResDto = reportService.readAllReport(member,memberRole);
        return ResponseEntity.ok(reportResDto);
    }


    @Operation(summary = " 리포트 단락 수정 API")
    @PatchMapping("")
    public ResponseEntity<ReportResDto> updateReport(@RequestBody ReportReqDto.UpdateDto updateDto){
        reportService.updateReport(updateDto);
        return ResponseEntity.ok(null);
    }
}
