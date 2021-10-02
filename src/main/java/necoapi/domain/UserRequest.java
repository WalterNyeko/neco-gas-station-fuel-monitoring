package necoapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.models.Role;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String username;

    private String password;

    private String email;

    private String address;

    private Set<Role> roles;
}
