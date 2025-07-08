// claims-api/src/main/java/com/insurance/claimsapi/producer/ClaimsProducerTest.java
package com.insurance.claimsapi.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaimsProducerTest {

    private final StreamBridge streamBridge;

    public boolean sendClaim(Object claim, String claimId, String claimType) {
        try {
            log.info("Sending claim {} of type {} to partition", claimId, claimType);

            // Create message with claimType as partition key
            Message<Object> message = MessageBuilder
                    .withPayload(claim)
                    .setHeader(KafkaHeaders.KEY, claimType)
                    .build();

            boolean sent = streamBridge.send("claims-out", message);

            if (sent) {
                log.info("Claim {} (type: {}) sent successfully", claimId, claimType);
                return true;
            } else {
                log.error("Failed to send claim {} (type: {})", claimId, claimType);
                return false;
            }

        } catch (Exception e) {
            log.error("Exception sending claim {} (type: {})", claimId, claimType, e);
            return false;
        }
    }
}