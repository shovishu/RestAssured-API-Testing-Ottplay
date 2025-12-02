package models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing the login request body.
 * Lombok annotations remove boilerplate code like getters/setters.
 */
@Data                   // generates getters, setters, toString, equals, hashCode
@Builder                // enables builder pattern
@NoArgsConstructor      // no-args constructor (needed by Jackson)
@AllArgsConstructor     // all-args constructor
public class CreateProfileRequest {
    private String avatar_id;
    private String name;
    private int profile_type;
    private String profile_type_key;
}
