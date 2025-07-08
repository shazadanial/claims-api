package com.insurance.claimsapi.service;
import com.insurance.claimsapi.model.InsuranceClaim;
import com.insurance.claimsapi.producer.ClaimsProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimsService {
    private final ClaimsProducer claimsProducer;
    public InsuranceClaim submitClaim(InsuranceClaim claim) {
        // Set system fields
        claim.setClaimId(UUID.randomUUID().toString());
        claim.setStatus("SUBMITTED");

        log.info("Submitting {} claim: {} for customer: {}",
                claim.getClaimType(), claim.getClaimId(), claim.getCustomerName());

        // Send to Kafka with claimType as partition key
        boolean sent = claimsProducer.sendClaim(claim, claim.getClaimId(), claim.getClaimType());

        if (!sent) {
            log.warn("Claim {} may not have been sent successfully", claim.getClaimId());
        }

        return claim;
    }
}