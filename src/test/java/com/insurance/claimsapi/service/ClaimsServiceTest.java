package com.insurance.claimsapi.service;

import com.insurance.claimsapi.model.InsuranceClaim;
import com.insurance.claimsapi.producer.ClaimsProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimsServiceTest {

    @Mock
    private ClaimsProducer claimsProducer;

    @InjectMocks
    private ClaimsService claimsService;

    @Test
    void shouldSubmitClaimSuccessfully() {
        // Given
        InsuranceClaim inputClaim = InsuranceClaim.builder()
                .policyNumber("POL-TEST-001")
                .customerName("Test Customer")
                .claimType("AUTO")
                .claimAmount(3500.0)
                .description("Test claim")
                .build();

        when(claimsProducer.sendClaim(any(), any(), any()))
                .thenReturn(true);

        // When
        InsuranceClaim result = claimsService.submitClaim(inputClaim);

        // Then
        assertNotNull(result.getClaimId());
        assertEquals("SUBMITTED", result.getStatus());
        assertEquals("POL-TEST-001", result.getPolicyNumber());
        assertEquals("Test Customer", result.getCustomerName());

        verify(claimsProducer).sendClaim(eq(result), eq(result.getClaimId()), eq("AUTO"));
    }

    @Test
    void shouldGenerateUniqueClaimIds() {
        // Given
        InsuranceClaim claim1 = InsuranceClaim.builder()
                .policyNumber("POL-001")
                .claimType("AUTO")
                .build();

        InsuranceClaim claim2 = InsuranceClaim.builder()
                .policyNumber("POL-002")
                .claimType("HEALTH")
                .build();

        when(claimsProducer.sendClaim(any(), any(), any()))
                .thenReturn(true);

        // When
        InsuranceClaim result1 = claimsService.submitClaim(claim1);
        InsuranceClaim result2 = claimsService.submitClaim(claim2);

        // Then
        assertNotEquals(result1.getClaimId(), result2.getClaimId());
    }
}