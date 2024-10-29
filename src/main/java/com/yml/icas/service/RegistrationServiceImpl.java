package com.yml.icas.service;

import com.yml.icas.model.Member;
import com.yml.icas.repository.MemberRegistrationRepo;
import com.yml.icas.service.interfaces.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    MemberRegistrationRepo memberRegistrationRepo;

    @Override
    public Boolean isRegistered(Integer year, String empNo) {
        Member m = null;//memberRegistrationRepo.findMemberRegistrationByYearAndMember_EmpNo(year, empNo);
        return m == null;
    }
}
