package com.insurance.claimsapi.integration;

import com.insurance.claimsapi.model.InsuranceClaim;
import com.insurance.claimsapi.service.ClaimsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class ClaimsIntegrationTest {

    @Autowired
    private ClaimsService claimsService;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void shouldSendClaimToKafkaTopic() {
        // Given
        InsuranceClaim claim = InsuranceClaim.builder()
                .policyNumber("POL-INT-001")
                .customerName("Integration Test Customer")
                .claimType("AUTO")
                .claimAmount(7500.0)
                .description("Integration test claim")
                .build();

        // When
        InsuranceClaim result = claimsService.submitClaim(claim);

        // Then
        assertNotNull(result.getClaimId());
        assertEquals("SUBMITTED", result.getStatus());

        // Verify message was sent to Kafka
        Message<byte[]> message = outputDestination.receive(1000, "insurance-claims");
        assertNotNull(message);

        String payload = new String(message.getPayload());
        assertTrue(payload.contains(result.getClaimId()));
        assertTrue(payload.contains("POL-INT-001"));
    }
}