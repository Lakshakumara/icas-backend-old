package com.yml.icas.control;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "*")
@RequestMapping(path = "/download")
public interface DownloadRest {

    @GetMapping(path = "/application/opd/{claimId}")
    ResponseEntity<byte[]> downloadOPDApplication(@PathVariable(name = "claimId") Integer claimId);


    @GetMapping(path = "/application/{year}/{empNo}")
    ResponseEntity<byte[]> downloadApplication(@PathVariable(name = "year") Integer year,
                                               @PathVariable(name = "empNo") String empNo);

    @GetMapping(path = "/voucher/{voucherId}")
    ResponseEntity<byte[]> downloadVoucher(@PathVariable(name = "voucherId") long voucherId);
}
