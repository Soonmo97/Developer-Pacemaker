package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.developer_pacemaker.dto.report.ReportCreateDTO;
import project.developer_pacemaker.dto.report.ReportDTO;
import project.developer_pacemaker.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping()
    public ResponseEntity<?> getMyReport(@AuthenticationPrincipal String uSeq){
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            List<ReportDTO> reportList = reportService.getReportListByuSeq(uSeqLong);

            return new ResponseEntity<>(reportList, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to load report data");
        }

    }

    @PostMapping()
    public ResponseEntity<String> saveReport(@AuthenticationPrincipal String uSeq, @RequestBody ReportCreateDTO report){
        try{
            Long uSeqLong = Long.parseLong(uSeq);
            reportService.saveReport(uSeqLong, report);
            return new ResponseEntity<>("Your report saved successfully", HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println("e=========="+e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to save report data");
        }

    }

    @PatchMapping("/{rSeq}")
    public ResponseEntity<String> updateReport(@AuthenticationPrincipal String uSeq, @RequestBody ReportCreateDTO report, @PathVariable long rSeq){
        Long uSeqLong = Long.parseLong(uSeq);

        boolean update = reportService.updateReportByrSeq(rSeq, uSeqLong, report);

        if(update){
            return ResponseEntity.ok("update successful");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update report data");
        }
    }

    @PatchMapping("/delete/{rSeq}")
    public ResponseEntity<String> deleteReport(@AuthenticationPrincipal String uSeq, @PathVariable long rSeq){
        Long uSeqLong = Long.parseLong(uSeq);
        boolean deleted = reportService.deleteReportByrSeq(uSeqLong, rSeq);

        if(deleted){
            return ResponseEntity.ok("Deletion successful");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete report data");
        }

    }
}

