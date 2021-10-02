package necoapi.security;

import necoapi.domain.CustomUserDetails;
import necoapi.models.User;
import necoapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username != null) {
            return Optional.ofNullable(username).filter(s -> !s.isEmpty());
        }
        return Optional.ofNullable("admin").filter(s -> !s.isEmpty());
    }
}
