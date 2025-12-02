package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing the WhatsApp consent request body.
 * Lombok annotations remove boilerplate code like getters/setters.
 */
@Data                   // generates getters, setters, toString, equals, hashCode
@Builder                // enables builder pattern
@NoArgsConstructor      // no-args constructor (needed by Jackson)
@AllArgsConstructor
public class WhatsAppConsentRequest {
    private boolean whatsAppNotification;
}
