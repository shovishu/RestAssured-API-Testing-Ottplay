package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing a profile request payload.
 * Currently optional, since getUserProfiles is a GET request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String userId;      // optional, can be used if API requires
    private String profileType; // optional
    private boolean includeInactive; // optional
}
