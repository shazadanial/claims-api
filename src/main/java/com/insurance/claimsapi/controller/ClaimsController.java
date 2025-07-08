package com.insurance.claimsapi.controller;
import com.insurance.claimsapi.service.ClaimsService;
import com.insurance.claimsapi.model.InsuranceClaim;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimsController {
    private final ClaimsService claimsService;
    @PostMapping
    public ResponseEntity<InsuranceClaim> submitClaim(@RequestBody InsuranceClaim
                                                              claim) {
        log.info("Received claim submission for customer: {}",
                claim.getCustomerName());
        InsuranceClaim submittedClaim = claimsService.submitClaim(claim);
        return ResponseEntity.ok(submittedClaim);
    }
}