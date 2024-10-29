package com.yml.icas.util;

import com.yml.icas.dto.*;
import com.yml.icas.model.Member;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class IcasUtil {
    public static byte[] genApplication(Member member) {
        Map<String, Object> empParams = new HashMap<>();
        empParams.put("title", "APPLICATION FORM TO JOIN THE MEDICAL WELFARE SCHEME FOR " +
                "PERMANENT EMPLOYEES IN THE UNIVERSITY");

        MemberDTO memberDTO = ObjectMapper.mapToMemberDTO(member);

        assert memberDTO != null;
        String ageText = calAge(memberDTO.getDob(), new Date());

        empParams.put("schemeType", memberDTO.getCurrentRegistration().getSchemeType());

        empParams.put("empNo", memberDTO.getEmpNo());
        empParams.put("name", memberDTO.getName());
        empParams.put("address", memberDTO.getAddress());
        empParams.put("email", memberDTO.getEmail());
        empParams.put("contactNo", memberDTO.getContactNo());
        empParams.put("civilStatus", memberDTO.getCivilStatus());
        empParams.put("nic", memberDTO.getNic());
        empParams.put("dob", memberDTO.getDob());
        empParams.put("age", ageText);
        empParams.put("sex", memberDTO.getSex());
        empParams.put("designation", memberDTO.getDesignation());
        empParams.put("department", memberDTO.getDepartment());

        //empParams.put("member", new JRBeanCollectionDataSource(Arrays.asList(memberDTO)));

        empParams.put("dependants", new JRBeanCollectionDataSource(memberDTO.getDependants()));

        empParams.put("beneficiary", new JRBeanCollectionDataSource(memberDTO.getBeneficiaries()));
        try {
           InputStream reportStream = IcasUtil.class.getResourceAsStream("/memberApplication.jrxml");
           if (reportStream == null) {
               throw new FileNotFoundException("JRXML file not found in classpath");
           }
           JasperPrint empReport = JasperFillManager.fillReport(
                   JasperCompileManager.compileReport(reportStream), // compile from InputStream
                   empParams, // dynamic parameters passed
                   new JREmptyDataSource()
           );
            log.info("Generated {} ");
            return JasperExportManager.exportReportToPdf(empReport);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }

    public static String calAge(Date dob, Date md) {
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(dob);
        Calendar measureDate = Calendar.getInstance();
        measureDate.setTime(md);

        int years = measureDate.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int months = 0;
        int days = 0;
        int currMonth = measureDate.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        long ageDaysLong = measureDate.getTime().getTime() - birthDay.getTime().getTime();
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (measureDate.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                months--;
            }
        } else if (months == 0 && measureDate.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        } else if (months > 0 && measureDate.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            months--;
        }
        //Calculate the days
        if (measureDate.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = measureDate.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (measureDate.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            Calendar td = Calendar.getInstance();
            td.setTimeInMillis(measureDate.getTimeInMillis());
            int today = td.get(Calendar.DAY_OF_MONTH);
            td.add(Calendar.MONTH, -1);
            days = td.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay
                    .get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        Float ageDays = (float) TimeUnit.DAYS.convert(ageDaysLong, TimeUnit.MILLISECONDS);
        return years + "y " + months + "m " + days + "d";
    }

    public static byte[] genOPDApplication(ClaimDTO claimDTO) {
        Map<String, Object> empParams = new HashMap<>();
        String pdfTemplate = (claimDTO.getCategory().equalsIgnoreCase("opd")) ?
                "opdApplication" : "SHApplicationFront";
        empParams.put("schemeType", claimDTO.getMember().getCurrentRegistration().getSchemeType());
        empParams.put("empNo", claimDTO.getMember().getEmpNo());
        empParams.put("name", claimDTO.getMember().getName());
        empParams.put("NIC", claimDTO.getMember().getNic());
        empParams.put("department", claimDTO.getMember().getDepartment());
        empParams.put("contactNo", claimDTO.getMember().getContactNo());
        empParams.put("dependants", new JRBeanCollectionDataSource(claimDTO.getMember().getDependants()));
        Set<ClaimDTO> data = new HashSet<>();
        data.add(claimDTO);

        try {
            InputStream reportStream = IcasUtil.class.getResourceAsStream("/" + pdfTemplate + ".jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("JRXML file not found in classpath");
            }

            JasperPrint empReport = JasperFillManager.fillReport(JasperCompileManager
                            .compileReport(reportStream)
                    , empParams // dynamic parameters passed
                    , new JRBeanCollectionDataSource(data)
            );
            if (!claimDTO.getCategory().equalsIgnoreCase("opd")) {
                JasperPrint backPage;
                InputStream reportStreamBack = IcasUtil.class.getResourceAsStream("/SHApplicationBack.jrxml");
                if (reportStreamBack == null) {
                    throw new FileNotFoundException("JRXML file not found in classpath");
                }
                backPage = JasperFillManager.fillReport(JasperCompileManager
                                .compileReport(reportStreamBack)
                        , empParams, new JRBeanCollectionDataSource(data));
                empReport.addPage((backPage.getPages()).get(0));
            }
            return JasperExportManager.exportReportToPdf(empReport);

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }

    public static byte[] genVoucher(long voucherId, Set<ClaimDTO> claimDTO) {
        Map<String, Object> empParams = new HashMap<>();
        empParams.put("voucherId", new Date(voucherId));

        try {
            InputStream reportStream = IcasUtil.class.getResourceAsStream("/voucher.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("JRXML file not found in classpath");
            }

            JasperPrint empReport =
                    JasperFillManager.fillReport(JasperCompileManager.compileReport(reportStream)
                    , empParams // dynamic parameters passed
                    , new JRBeanCollectionDataSource(claimDTO)
            );
            return JasperExportManager.exportReportToPdf(empReport);

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }

     /* @Deprecated
    public static ResponseEntity<byte[]> generatePdf(Member member) {
        Map<String, Object> empParams = new HashMap<>();
        String fileName = member.getName();

        empParams.put("title", "APPLICATION FORM TO JOIN THE MEDICAL WELFARE SCHEME FOR " +
                "PERMANENT EMPLOYEES IN THE UNIVERSITY");
        empParams.put("empNo", member.getEmpNo());
        empParams.put("name", member.getName());
        empParams.put("address", member.getAddress());
        empParams.put("email", member.getEmail());
        empParams.put("contactNo", member.getContactNo());
        empParams.put("civilStatus", member.getCivilStatus());
        empParams.put("nic", member.getNic());
        empParams.put("dob", member.getDob());
        empParams.put("sex", member.getSex());
        empParams.put("designation", member.getDesignation());
        empParams.put("department", member.getDepartment());
        MemberDTO memberDTO = ObjectMapper.mapToMemberDTO(member);
        Set<DependantDTO> dependants = memberDTO.getDependants();
        empParams.put("dependants", new JRBeanCollectionDataSource(dependants));

        Set<BeneficiaryDTO> beneficiaries = memberDTO.getBeneficiaries();
        empParams.put("beneficiary", new JRBeanCollectionDataSource(beneficiaries));

        try {
            InputStream reportStream = IcasUtil.class.getResourceAsStream("/memberApplication.jrxml");
            if (reportStream == null) {
                throw new FileNotFoundException("JRXML file not found in classpath");
            }

            JasperPrint empReport = JasperFillManager.fillReport(
                    JasperCompileManager.compileReport(reportStream), // compile from InputStream
                    empParams, // dynamic parameters passed
                    new JREmptyDataSource()
            );

            HttpHeaders headers = new HttpHeaders();
            //set the PDF format
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", fileName + ".pdf");
            //create the report in PDF format
            log.info("pdf generated");
            return new ResponseEntity<>
                    (JasperExportManager.exportReportToPdf(empReport), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

}
