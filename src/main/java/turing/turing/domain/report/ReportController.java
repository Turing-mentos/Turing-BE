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
        //memberId와 role만 필요 -> Authen~~ 에서 얻어오기 (jwt)
        //이건 예제
//        Long member = 1L;
//        String memberRole = "TEACHER";
        return ResponseEntity.ok(reportService.readAllReport(userDetails));
    }


    @Operation(summary = " 리포트 단락 수정 API")
    @PatchMapping("")
    public ResponseEntity<ReportResDto> updateReport(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReportReqDto.UpdateDto updateDto){
        reportService.updateReport(userDetails, updateDto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "리포트 최초 진입 시 과외 정보 여부 확인 API ")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkConditionForReport(@AuthenticationPrincipal CustomUserDetails userDetails){

//        Long memberId =1L;
//        String memberRole ="TEACHER";
        return ResponseEntity.ok(reportService.checkConditionForReport(userDetails));

    }

}
