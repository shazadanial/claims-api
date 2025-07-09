package com.insurance.claimsapi.producer;

import com.insurance.claimsapi.model.InsuranceClaim;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimsProducerTest {

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private ClaimsProducer claimsProducer;

    @Test
    void shouldSendClaimSuccessfully() {
        // Given
        InsuranceClaim claim = InsuranceClaim.builder()
                .claimId("TEST-001")
                .claimType("AUTO")
                .claimAmount(5000.0)
                .customerName("Test Customer")
                .build();

        when(streamBridge.send(eq("claims-out"), any(Message.class)))
                .thenReturn(true);

        // When
        boolean result = claimsProducer.sendClaim(claim, "TEST-001", "AUTO");

        // Then
        assertTrue(result);
        verify(streamBridge).send(eq("claims-out"), any(Message.class));
    }

    @Test
    void shouldHandleSendFailure() {
        // Given
        InsuranceClaim claim = InsuranceClaim.builder()
                .claimId("TEST-002")
                .claimType("HEALTH")
                .claimAmount(1200.0)
                .build();

        when(streamBridge.send(eq("claims-out"), any(Message.class)))
                .thenReturn(false);

        // When
        boolean result = claimsProducer.sendClaim(claim, "TEST-002", "HEALTH");

        // Then
        assertFalse(result);
    }

    @Test
    void shouldHandleException() {
        // Given
        InsuranceClaim claim = InsuranceClaim.builder()
                .claimId("TEST-003")
                .claimType("PROPERTY")
                .build();

        when(streamBridge.send(any(), any()))
                .thenThrow(new RuntimeException("Kafka connection failed"));

        // When
        boolean result = claimsProducer.sendClaim(claim, "TEST-003", "PROPERTY");

        // Then
        assertFalse(result);
    }
}