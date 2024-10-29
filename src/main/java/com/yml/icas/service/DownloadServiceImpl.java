package com.yml.icas.service;

import com.yml.icas.dto.ClaimDTO;
import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.model.Claim;
import com.yml.icas.model.Member;
import com.yml.icas.model.MemberRegistration;
import com.yml.icas.repository.ClaimRepo;
import com.yml.icas.repository.MemberRegistrationRepo;
import com.yml.icas.repository.MemberRepo;
import com.yml.icas.service.interfaces.DownloadService;
import com.yml.icas.util.IcasUtil;
import com.yml.icas.util.MyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    ClaimRepo claimRepo;

    @Autowired
    MemberRegistrationRepo memberRegistrationRepo;

    @Override
    public ResponseEntity<byte[]> downloadApplication(Integer year, String empNo) {
        try {
            Member member = memberRepo.findByEmpNoIgnoreCase(empNo);
            member = addRegistration(member);

            if (Objects.isNull(member))
                return new ResponseEntity<>((empNo + " not Found").getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
            byte[] pdf = IcasUtil.genApplication(member);

            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", year + "_" + empNo + ".pdf");
            //create the report in PDF format
            log.info("fetch Member {}", member);
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(MyConstants.ERROR_MSG1.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadOPDApplication(Integer claimId) {
        try {
            Claim claim = claimRepo.findClaimById(claimId);
            log.info("findClaimById {}", claim);
            if (Objects.isNull(claim))
                return new ResponseEntity<>((claimId + " not Found").getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
            ClaimDTO claimDTO = ObjectMapper.mapToClaimDTO(claim);
            log.info("claimDTO {}", claimDTO);
            byte[] pdf = IcasUtil.genOPDApplication(claimDTO);

            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "ClaimOPD" + ".pdf");
            //create the report in PDF format

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(MyConstants.ERROR_MSG1.getBytes(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] downloadVoucher(long voucherId) {
        try {
            List<Claim> claims = claimRepo.findAllByVoucherId(voucherId);
            log.info("voucher claim {}", claims);
            Set<ClaimDTO> claimDtos = claims.stream().map(ObjectMapper::mapToClaimDTO).collect(Collectors.toSet());
            byte[] pdf = IcasUtil.genVoucher(voucherId, claimDtos);

            //create the report in PDF format

            return pdf;
        } catch (Exception e) {
            log.info(e.toString());
            return MyConstants.ERROR_MSG1.getBytes();
        }
    }

    public Member addRegistration(Member member) {
        Set<MemberRegistration> regData = memberRegistrationRepo.findByMember(new Member(member.getId()));
        member.getMemberRegistrations().addAll(regData.stream()
                .map(rd -> {
                    MemberRegistration mr = new MemberRegistration();
                    mr.setSchemeType(rd.getSchemeType());
                    mr.setYear(rd.getYear());
                    mr.setAcceptedDate(rd.getAcceptedDate());
                    mr.setAcceptedBy(rd.getAcceptedBy());
                    return mr;
                }).collect(Collectors.toSet()));
        return member;
    }
}
