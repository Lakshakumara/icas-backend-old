package com.yml.icas.service;

import com.yml.icas.dto.BeneficiaryDTO;
import com.yml.icas.dto.DependantDTO;
import com.yml.icas.dto.MemberDTO;
import com.yml.icas.dto.ObjectMapper;
import com.yml.icas.model.*;
import com.yml.icas.repository.*;
import com.yml.icas.service.interfaces.MemberService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.yml.icas.util.IcasUtil.genApplication;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private MemberRegistrationRepo memberRegistrationRepo;

    @Autowired
    private MemberDependantDataRepo memberDependantDataRepo;

    @Autowired
    private DependantRepo dependantRepo;

    @Autowired
    private BeneficiaryRepo beneficiaryRepo;

    @Autowired
    private BeneficiaryDataRepo beneficiaryDataRepo;


    @Override
    public ResponseEntity<Integer> updateMember(String criteria, Map<String, Object> dataSet) {
        log.info("dataSet {}", dataSet);
        Integer rows = 0;
        if (criteria.equalsIgnoreCase("registerOpen")) {
            if (dataSet.get("selector").toString().equalsIgnoreCase("all")) {
                rows = memberRepo.updateRegistrationAll((Integer) dataSet.get("year"));
            } else {
                rows = memberRepo.updateRegistrationMember(dataSet.get("selector").toString(), (Integer) dataSet.get("year"));
            }
        } else if (criteria.equalsIgnoreCase("role")) {
            List<String> dd = (List<String>) dataSet.get("newrole");


            Set<Role> roles = dd.stream()
                    .map(Role::new).collect(Collectors.toSet());

            /*roles.addAll(dd.stream()
                    .map(r -> {
                        return new Role(r);
                    }).collect(Collectors.toSet()));
            */
            if (dataSet.get("memberId") != null) {
                Member mm = memberRepo.getMember((String) dataSet.get("empNo"));
                mm.getRoles().clear();
                mm.getRoles().addAll(roles);

                log.info("saving data Id {} \nRoles {}", mm.getId(), mm.getRoles());
                memberRepo.save(mm);
                rows+=1;
                //rows = memberRepo.updateRoles((Integer) dataSet.get("memberId"), roles);
            }

        } else if (criteria.equalsIgnoreCase("memberAccept")) {
            log.info("acceptedBy{} {} memberId{}", dataSet.get("acceptedBy"), LocalDate.now(), dataSet.get("memberId"));
            if (dataSet.get("memberId") != null) {
                rows = memberRegistrationRepo.acceptRegistration((Integer) dataSet.get("acceptedBy"),
                        LocalDate.now(), (Integer) dataSet.get("memberId"));
            }
            log.info("{} rows updated ", rows);
        }
        return new ResponseEntity<>(rows, HttpStatus.OK);
    }

    @Deprecated
    @Transactional
    @Async // TODO check
    @Override
    public ResponseEntity<byte[]> signUp(MemberDTO memberDTO) {
        ResponseEntity<byte[]> response = null;
        try {
            if (!validateMemberRest(memberDTO)) {
                return new ResponseEntity<>(new byte[]{'i', 'n', 'v', 'a', 'l', 'i', 'd'}, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Member member = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());
            Member mapMember = ObjectMapper.mapToMember(memberDTO);
            if (Objects.isNull(member)) {
                memberRepo.save(mapMember);
            }
            Member savedMember = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());

            MemberRegistration memberRegistration = ObjectMapper.mapToMemberRegistration(memberDTO.getMemberRegistrations());
            MemberRegistration oldReg = memberRegistrationRepo.findByYearAndMember(memberRegistration.getYear(), savedMember);
            if (Objects.isNull(oldReg)) {
                memberRegistration.setMember(savedMember);
                memberRegistrationRepo.save(memberRegistration);
            } else {
                throw new Exception("Already register for the year " + memberRegistration.getYear());
            }


            Set<DependantDTO> dependants = memberDTO.getDependants();
            dependants.forEach(d -> {
                Dependant dependant = new Dependant();
                dependant.setId(d.getId());
                dependant.setName(d.getName());
                dependant.setNic(d.getNic());
                dependant.setDob(d.getDob());
                Dependant newDep = dependantRepo.save(dependant);
                MemberDependantData mdd = new MemberDependantData();
                mdd.setRegisterYear(memberRegistration.getYear());
                mdd.setRelationship(d.getRelationship());
                mdd.setDependant(newDep);
                mdd.setMember(savedMember);
                memberDependantDataRepo.save(mdd);
            });

            Set<BeneficiaryDTO> beneficiaries = memberDTO.getBeneficiaries();
            beneficiaries.forEach(d -> {
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setId(d.getId());
                beneficiary.setName(d.getName());
                beneficiary.setNic(d.getNic());
                Beneficiary newBen = beneficiaryRepo.save(beneficiary);
                BeneficiaryData bnd = new BeneficiaryData();
                bnd.setPercent(d.getPercent());
                bnd.setRelationship(d.getRelationship());
                bnd.setBeneficiary(newBen);
                bnd.setRegisterDate(d.getRegisterDate());
                bnd.setMember(savedMember);
                beneficiaryDataRepo.save(bnd);
            });
            log.info("Saved Member {}", savedMember);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("some error occurred");
            response = new ResponseEntity<>(new byte[]{'e', 'r', 'r', 'o', 'r'}, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            Member successMember = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());
            //sendEmailToNewMember(requestMap.get("email"));
            log.info("finally successMember member {}", successMember);
            if (!Objects.isNull(successMember))
                response = new ResponseEntity<>(genApplication(successMember), HttpStatus.OK);
        }
        return response;
    }

    @Transactional
    @Override
    public Object signUpNew(MemberDTO memberDTO) {
        Object response = null;
        try {
            if (!validateMemberRest(memberDTO)) {
                return "invalid data";
            }
            Member member = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());
            Member mapMember = ObjectMapper.mapToMember(memberDTO);
            if (Objects.isNull(member)) {
                memberRepo.save(mapMember);
            }
            Member savedMember = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());

            MemberRegistration memberRegistration = ObjectMapper.mapToMemberRegistration(memberDTO.getMemberRegistrations());
            MemberRegistration oldReg = memberRegistrationRepo.findByYearAndMember(memberRegistration.getYear(), savedMember);
            if (Objects.isNull(oldReg)) {
                memberRegistration.setMember(savedMember);
                memberRegistrationRepo.save(memberRegistration);
            } else {
                log.info("already register for current year");
                return "Already register for the year " + memberRegistration.getYear();
            }
            Set<DependantDTO> dependants = memberDTO.getDependants();
            dependants.forEach(d -> {
                Dependant dependant = new Dependant();
                dependant.setId(d.getId());
                dependant.setName(d.getName());
                dependant.setNic(d.getNic());
                dependant.setDob(d.getDob());
                Dependant newDep = dependantRepo.save(dependant);
                MemberDependantData mdd = new MemberDependantData();
                mdd.setRegisterYear(memberRegistration.getYear());
                mdd.setRelationship(d.getRelationship());
                mdd.setDependant(newDep);
                mdd.setMember(savedMember);
                memberDependantDataRepo.save(mdd);
            });

            Set<BeneficiaryDTO> beneficiaries = memberDTO.getBeneficiaries();
            beneficiaries.forEach(d -> {
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setId(d.getId());
                beneficiary.setName(d.getName());
                beneficiary.setNic(d.getNic());
                Beneficiary newBen = beneficiaryRepo.save(beneficiary);
                BeneficiaryData bnd = new BeneficiaryData();
                bnd.setPercent(d.getPercent());
                bnd.setRelationship(d.getRelationship());
                bnd.setBeneficiary(newBen);
                bnd.setRegisterDate(d.getRegisterDate());
                bnd.setMember(savedMember);
                beneficiaryDataRepo.save(bnd);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Member successMember = memberRepo.findByEmpNoIgnoreCase(memberDTO.getEmpNo());
            //sendEmailToNewMember(requestMap.get("email"));
            log.info("finally successMember member {}", successMember);
            if (!Objects.isNull(successMember))
                response = successMember;
        }
        return response;
    }

    @Override
    public ResponseEntity<MemberDTO> getMember(String empNo) {
        try {
            Member member = memberRepo.findByEmpNoIgnoreCase(empNo);
            if (member == null)
                return new ResponseEntity<>(new MemberDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
            member = addRegistration(member);
            log.info("member Retrieve {} ", member);
            return new ResponseEntity<>(ObjectMapper.mapToMemberDTO(member), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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

    private boolean validateMemberRest(MemberDTO memberDTO) {
        return !memberDTO.getEmpNo().isEmpty() && !memberDTO.getName().isEmpty();
        //&& requestMap.containsKey("email") && requestMap.containsKey("nic");
    }

    @Override
    public ResponseEntity<String[]> getRelationShip(String rs) {
        try {
            return new ResponseEntity<>(memberDependantDataRepo.findDistinctByRelationshipLike(rs), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new String[]{}, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Set<MemberDTO>> searchMember(Map<String, Object> searchParams) {
        try {
            List<MemberRegistration> mt;
            Set<MemberDTO> memberDTOSet = new HashSet<>();
            if (searchParams.get("searchFor").toString().equalsIgnoreCase("notAccept")) {
                mt = memberRegistrationRepo.findByAcceptedDateNull();
                memberDTOSet = mt.stream().map(mtt -> ObjectMapper.mapToMemberDTO(mtt.getMember())).collect(Collectors.toSet());
            } else if (searchParams.get("searchFor").toString().equalsIgnoreCase("accepted")) {
                mt = memberRegistrationRepo.findByAcceptedDate((Date) searchParams.get("acceptDate"));
                memberDTOSet = mt.stream().map(mtt -> ObjectMapper.mapToMemberDTO(mtt.getMember())).collect(Collectors.toSet());
            }
            if (searchParams.get("searchFor").toString().equalsIgnoreCase("name")) {
                List<Member> mm = memberRepo.findAllByNameContainsIgnoreCase((String) searchParams.get("searchText"));
                memberDTOSet = mm.stream().map(ObjectMapper::mapToMemberDTO).collect(Collectors.toSet());
            }
            if (searchParams.get("searchFor").toString().equalsIgnoreCase("empNo")) {
                List<Member> mm = memberRepo.findAllByEmpNoContainsIgnoreCase((String) searchParams.get("searchText"));

                memberDTOSet = mm.stream().map(ObjectMapper::mapToMemberDTO).collect(Collectors.toSet());
            }
            log.info("searchParams from {} result \n{}", searchParams, memberDTOSet);
            return new ResponseEntity<>(memberDTOSet, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
        private Member getMemberDepDataFromMap(MemberDTO memberDTO) {
            Set<MemberDependantData> setmddp = new HashSet<>();
            MemberDependantData mdd = new MemberDependantData();
            mdd.setRelationship(memberDTO.get("relationship").toString());
            mdd.setRegisterDate((Date) memberDTO.get("registrationDate"));
            setmddp.add(mdd);

            Member member = new Member();
            member.setEmpNo(memberDTO.getEmpNo());
            member.setName(memberDTO.getName());
            member.setAddress(memberDTO.getAddress());
            member.setEmail(memberDTO.getEmail());
            member.setContactNo(memberDTO.getContactNo());
            member.setCivilStatus(memberDTO.getCivilStatus());
            member.setNic(memberDTO.getNic());
            member.setSex(memberDTO.getSex());
            member.setDob(memberDTO.getDob());
            member.setDesignation(memberDTO.getDesignation());
            member.setDepartment(memberDTO.getDepartment());
            member.setPassword(memberDTO.getPassword());
            member.setStatus(memberDTO.getStatus();
            member.setRole(memberDTO.getRole());
            member.setDeleted(memberDTO.isDeleted());

            List<Object> dd = Arrays.asList(memberDTO.get("dependants"));
            log.info(dd.toString());
            for (Object o:dd) {
                //log.info(o.toString());
                Map<String, Object> depmap = (Map<String, Object>) o;
                dep.setNic((String) depmap.get("nic"));
                dep.setName((String) depmap.get("name"));
                //dep.setDob((Date)depmap.get("dob"));
            }

            member.setDependantData(setmddp);
            return member;
        }
    */
    //@Transactional
    public Member saveMember(Member member) {
        member.getDependantData().forEach(d -> {
            log.info("Dependant {}", d);
            //d.setMember(member);
            dependantRepo.save(d.getDependant());
            // memDepDao.save(d);
        });
        // memberdao.save(member);
       /* Member newMember = new Member();
        newMember.setName(member.getName());

        newMember.getDependantData()
                 .addAll(member.getDependantData()
                        .stream()
                        .map(d -> {
                            MemberDependantData dependant = d;
                            //dependant.getDependantData()
                            return dependant;
                        }).collect(Collectors.toSet()));
*/
        log.info("saving {} ", member);
        return memberRepo.findByEmpNoIgnoreCase(member.getEmpNo());
    }
}