package com.yml.icas.service;

import com.yml.icas.dto.ClaimDTO;
import com.yml.icas.dto.ClaimOPDDTO;
import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.model.Claim;
import com.yml.icas.model.ClaimData;
import com.yml.icas.model.Member;
import com.yml.icas.model.SchemeData;
import com.yml.icas.repository.ClaimDataRepo;
import com.yml.icas.repository.ClaimRepo;
import com.yml.icas.repository.MemberRepo;
import com.yml.icas.repository.SchemeDataRepo;
import com.yml.icas.service.interfaces.ClaimService;
import com.yml.icas.util.Converter;
import com.yml.icas.util.MyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClaimServiceImpl implements ClaimService {
    private static final ExampleMatcher SEARCH_CONDITIONS_MATCH_ANY = ExampleMatcher
            .matchingAny()
            .withMatcher("category", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("empNo", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("claimstatus", ExampleMatcher.GenericPropertyMatchers.exact())
            .withMatcher("year", ExampleMatcher.GenericPropertyMatchers.exact())
            // .withMatcher("claimstatus", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withIgnorePaths("category", "empNo", "claimstatus", "year");
    @Autowired
    ClaimRepo claimRepo;

    @Autowired
    ClaimDataRepo claimDataRepo;

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    SchemeDataRepo schemeDataRepo;

    public Integer addClaim(ClaimDTO claimDTO) {
        Integer claimId = null;
        if (claimDTO == null) return claimId;
        try {
            Claim c = ObjectMapper.mapToClaim(claimDTO);
            log.info("ready to save {}", c);
            claimId = (claimRepo.save(Objects.requireNonNull(c))).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claimId;
    }
    /*public Integer addClaimData(ClaimDTO claimDTO) {
        Integer claimId = null;
        if(claimDTO == null) return claimId;
        try {
            Claim c = ObjectMapper.mapToClaim(claimDTO);
            log.info("ready to save {}",c);
            claimId = (claimDataRepo.save(Objects.requireNonNull(c))).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claimId;
    }*/

    @Override
    public ResponseEntity<Integer> saveOpd(ClaimOPDDTO claimOPDDTO) {
        Integer serial;
        try {
            serial = (claimRepo.save(ObjectMapper.mapToClaimOPD(claimOPDDTO))).getId();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(serial, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> updateClaim(Map<String, Object>[] dataSets) {
        log.info("received Claim update data Set {}", (Object) dataSets);
        int rows = 0;
        Long voucherId = new Date().getTime();
        for (Map<String, Object> dataSet : dataSets) {
            try {
                if (dataSet.get("criteria").toString().equalsIgnoreCase("claimreject")) {
                    rows += claimRepo.claimReject((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"),
                            (String) dataSet.get("rejectRemarks"), MyConstants.TODAY());
                }else if (dataSet.get("criteria").toString().equalsIgnoreCase("headaccept")) {
                    rows += claimRepo.updateHeadAccept((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"),
                            (Integer) dataSet.get("acceptedBy"), MyConstants.TODAY());
                } else if (dataSet.get("criteria").toString().equalsIgnoreCase("forwordmec")) {
                    rows += claimRepo.forwardMEC((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"), MyConstants.TODAY());
                } else if (dataSet.get("criteria").toString().equalsIgnoreCase("opdupdate")) {
                    addClaimData(dataSet);
                    rows += claimRepo.opdComplete((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"),
                            Converter.toDouble(dataSet.get("deductionAmount")),
                            (String) dataSet.get("mecremarks"),
                            MyConstants.TODAY(),
                            Converter.toDate(dataSet.get("rejecteddate")),
                            (String) dataSet.get("rejectremarks")
                    );
                } else if (dataSet.get("criteria").toString().equalsIgnoreCase("claimdata")) {
                    rows += addClaimData(dataSet);
                    log.info("data saved {}", rows);
                } else if (dataSet.get("criteria").toString().equalsIgnoreCase("mec_approved")) {
                    rows += claimRepo.mecApproval((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"));

                } else if (dataSet.get("criteria").toString().equalsIgnoreCase("forwordfinance")) {
                    rows += claimRepo.forwardFinance((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"), MyConstants.TODAY(),
                            Double.parseDouble(String.valueOf(dataSet.get("paidAmount"))),
                            voucherId);
                }else if (dataSet.get("criteria").toString().equalsIgnoreCase("forwordpaid")) {
                    rows += claimRepo.forwardPaid((Integer) dataSet.get("id"),
                            (String) dataSet.get("claimStatus"), MyConstants.TODAY());
                }else if (dataSet.get("criteria").toString().equalsIgnoreCase("finalize")) {
                    rows += claimRepo.finalize((Integer) dataSet.get("id"),
                            Double.parseDouble(String.valueOf(dataSet.get("deductionAmount"))),
                            Double.parseDouble(String.valueOf(dataSet.get("paidAmount"))));

                    log.info("Double.parseDouble((String) dataSet.get(\"deductionAmount\") {} " +
                            "Double.parseDouble((String) dataSet.get(\"paidAmount\")) {}",
                            Double.parseDouble(String.valueOf(dataSet.get("deductionAmount"))),
                            Double.parseDouble(String.valueOf(dataSet.get("paidAmount"))));

                }

            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(rows, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(rows, HttpStatus.OK);
    }

    private Integer addClaimData(Map<String, Object> dataSet) {
        SchemeData scheme = schemeDataRepo.findByIdText((String) dataSet.get("idText"));
        ClaimData cd = new ClaimData();
        cd.setSchemeData(scheme);
        cd.setClaim(new Claim((int) dataSet.get("id")));
        cd.setRequestAmount(Converter.toDouble(dataSet.get("requestAmount")));
        cd.setClaimDataStatus((String) dataSet.get("claimDataStatus"));
        cd.setDeductionAmount(Converter.toDouble(dataSet.get("deductionAmount")));
        cd.setDeductionRemarks((String) dataSet.get("deductionRemarks"));
        cd.setRejectedDate(Converter.toDate(dataSet.get("rejectDate")));
        cd.setRejectRemarks((String) dataSet.get("rejectRemarks"));
        cd.setRemarks((String) dataSet.get("remarks"));
        log.info("saved ClaimData {}", cd);
        claimDataRepo.save(cd);
        return 1;
    }


    @Override
    public ClaimDTO getClaim(Integer claimId) {
        log.info("getClaim received params {}", claimId);
        try {
            Claim claim = claimRepo.findClaimById(claimId);
            return ObjectMapper.mapToClaimDTO(claim);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<ClaimDTO> getAllClaim(Map<String, String> params) {
        log.info("getAllClaim params {}", params);
        final Set<ClaimDTO> dd = new HashSet<>();
        try {
            Set<Claim> claimList = new HashSet<>();
            if (!params.get("empNo").isEmpty()) {
                Integer id = memberRepo.getMemberId(params.get("empNo"));
                if (!Objects.isNull(id)) {
                    if (Integer.parseInt(params.get("year")) == 0 && !params.get("claimStatus").equalsIgnoreCase("%")) {
                        claimList = new HashSet<>(claimRepo.getClaimData(
                                new Member(id),
                                params.get("claimStatus")));
                    } else if (Integer.parseInt(params.get("year")) == 0 && params.get("claimStatus").equalsIgnoreCase("%")) {
                        claimList = new HashSet<>(claimRepo.getClaimData(new Member(id)));
                    } else if (Integer.parseInt(params.get("year")) != 0 && !params.get("claimStatus").equalsIgnoreCase("%")) {
                        claimList = new HashSet<>(claimRepo.getClaimData(
                                new Member(id),
                                Integer.valueOf(params.get("year")),
                                params.get("claimStatus")));
                    }else if (Integer.parseInt(params.get("year")) != 0 && params.get("claimStatus").equalsIgnoreCase("%")) {
                        claimList = new HashSet<>(claimRepo.getClaimData(new Member(id),
                                Integer.valueOf(params.get("year"))));
                    }else{
                        claimList = new HashSet<>(claimRepo.getClaimData(
                                new Member(id), params.get("claimType"),
                                Integer.valueOf(params.get("year")),
                                params.get("claimStatus")));
                    }
                }
                log.info("params.get(\"empNo\") {}\n year {}\n claimStatus {}",
                        params.get("empNo"),
                        Integer.parseInt(params.get("year")), params.get("claimStatus"));

            } else {
                if (params.get("claimType").equalsIgnoreCase("%")){
                    log.info("params.get(\"claimStatus\") {}", params.get("claimStatus"));
                    claimList = new HashSet<>(claimRepo.findAllByClaimStatusLike(params.get("claimStatus")));
                }

                else {
                    log.info("params.get().equalsIgnoreCase {} {}", params.get("claimType"), params.get("claimStatus"));
                    claimList = new HashSet<>(claimRepo
                            .getClaims(params.get("claimType"), params.get("claimStatus")));
                }

            }
            dd.addAll(claimList.stream().map(ObjectMapper::mapToClaimDTO).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return dd;
        }
        log.info("getAllClaim for params.get(\"empNo\"){}\n {}", params.get("empNo"), dd);
        return dd;
    }

    @Override
    public Set<ClaimDTO> getDashboardData(Integer year, String empNo) {
        Set<ClaimDTO> dd;
        try {
            Member member = memberRepo.getMember(empNo);
            log.info("member {}", member);
            Set<Claim> claimList = new HashSet<>(claimRepo.getDashboardData(
                    member, year));
            log.info("claimList {}", claimList);
            dd = claimList.stream().map(ObjectMapper::mapToClaimDTO).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return dd;
    }

    @Override
    public long[] getVoucherIds() {
        try {
            return  claimRepo.getVoucherId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteClaimData(Integer id) {
        boolean ok = false;
        try {
            claimDataRepo.deleteById(id);
            ok = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("deleted {}", ok);
        return ok;
    }
}
