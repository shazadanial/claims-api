package com.insurance.claimsapi.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaim {
    private String claimId;
    private String policyNumber;
    private String customerName;
    private String claimType; // AUTO, HOME, HEALTH, LIFE
    private Double claimAmount;
    private String status; // SUBMITTED, PROCESSING, APPROVED, REJECTED
    private String description;
}