package project.developer_pacemaker.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.developer_pacemaker.dto.report.ReportCreateDTO;
import project.developer_pacemaker.dto.report.ReportDTO;
import project.developer_pacemaker.entity.ReportEntity;
import project.developer_pacemaker.entity.UserEntity;
import project.developer_pacemaker.repository.ReportRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {
    final private ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    public List<ReportDTO> getReportListByuSeq(Long uSeq) {
        Sort sort = sortByrSeq();
        List<ReportEntity> reportEntities = reportRepository.findByUser_uSeqAndIsDeleted(uSeq, false, sort);
        return reportEntities.stream()
                .map(report -> new ReportDTO(report.getTitle(), report.getContent(), report.getRegistered()))
                .collect(Collectors.toList());
    }

    private Sort sortByrSeq() {
        return Sort.by(Sort.Direction.DESC, "rSeq");
    }

    public void saveReport(Long uSeq, ReportCreateDTO report) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setTitle(report.getTitle());
        reportEntity.setContent(report.getContent());

        UserEntity userEntity = new UserEntity();
        userEntity.setUSeq(uSeq);

        reportEntity.setUser(userEntity);

        reportRepository.save(reportEntity);
    }

    public boolean updateReportByrSeq(long rSeq, long currentUSeq, ReportCreateDTO report) {
        try{
            Optional<ReportEntity> reportEntityOptional  = reportRepository.findById(rSeq);
            if(reportEntityOptional.isPresent()){
                ReportEntity reportEntity = reportEntityOptional.get();
                // 현재 로그인한 사용자의 레포트만 수정 가능
                if(reportEntity.getUser().getUSeq()!=currentUSeq){
                    return false;
                }
                reportEntity.setTitle(report.getTitle());
                reportEntity.setContent(report.getContent());
                reportRepository.save(reportEntity);
                return true;
            }else{
                return false;
            }

        }catch (Exception e){
                return false;
        }
    }

    public boolean deleteReportByrSeq(long currentUSeq, long rSeq) {
        try{
            ReportEntity reportEntity = reportRepository.findById(rSeq)
                    .orElseThrow(() -> new RuntimeException("Report data with rSeq " + rSeq + " not found"));
            if(reportEntity.getUser().getUSeq()!=currentUSeq){
                return false;
            }
            reportEntity.setDeleted(true);
            reportRepository.save(reportEntity);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
