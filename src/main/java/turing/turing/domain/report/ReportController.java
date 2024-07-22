package turing.turing.domain.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.member.Role;
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
    public ResponseEntity<ReportResDto.CreateDto> createReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportReqDto.CreateDto reportReq){
        return ResponseEntity.ok(reportService.createReport(userDetails, reportReq));
    }
    @Operation(summary = " 리포트 단일 조회 API")
    @GetMapping("{reportId}")
    public ResponseEntity<ReportResDto.ReadDto> readReport(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable (name="reportId") Long reportId){
        return ResponseEntity.ok( reportService.readReport(userDetails, reportId));
    }

    @Operation(summary = " 리포트 전체 조회 API")
    @GetMapping("all")
    public ResponseEntity<List<ReportReadAllDto>> readAllReport(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(reportService.readAllReport(userDetails));
    }


    @Operation(summary = " 리포트 단락 수정 API")
    @PatchMapping("")
    public ResponseEntity<ReportResDto> updateReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportReqDto.UpdateDto updateDto){
        reportService.updateReport(userDetails, updateDto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "리포트 진입 시 과외 학생 정보")
    @GetMapping("/init")
    public ResponseEntity<List<ReportResDto.StudentInfoDto>> checkStudentInfoForReport(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(reportService.checkStudentInfoForReport(userDetails));

    }

}
