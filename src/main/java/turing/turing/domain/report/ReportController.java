package turing.turing.domain.report;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import turing.turing.domain.auth.CustomUserDetails;
import turing.turing.domain.report.dto.response.ReportReadAllDto;
import turing.turing.domain.report.dto.request.ReportRequestDto;
import turing.turing.domain.report.dto.response.ReportResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = " 리포트 생성 API")
    @PostMapping("")
    public ResponseEntity<ReportResponseDto.CreateDto> createReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportRequestDto.CreateDto reportReq){
        return ResponseEntity.ok(reportService.createReport(userDetails, reportReq));
    }
    @Operation(summary = " 리포트 단일 조회 API")
    @GetMapping("{reportId}")
    public ResponseEntity<ReportResponseDto.ReadDto> readReport(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable (name="reportId") Long reportId){
        return ResponseEntity.ok( reportService.readReport(userDetails, reportId));
    }

    @Operation(summary = " 리포트 전체 조회 API")
    @GetMapping("all")
    public ResponseEntity<List<ReportReadAllDto>> readAllReport(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(reportService.readAllReport(userDetails));
    }


    @Operation(summary = " 리포트 단락 수정 API")
    @PatchMapping("")
    public ResponseEntity<ReportResponseDto> updateReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportRequestDto.UpdateDto updateDto){
        reportService.updateReport(userDetails, updateDto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "리포트 진입 시 과외 학생 정보")
    @GetMapping("/init")
    public ResponseEntity<List<ReportResponseDto.StudentInfoDto>> checkStudentInfoForReport(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(reportService.checkStudentInfoForReport(userDetails));

    }

}
