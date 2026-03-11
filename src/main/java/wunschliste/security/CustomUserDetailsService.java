package wunschliste.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import wunschliste.benutzer.Benutzer;
import wunschliste.benutzer.BenutzerRepository;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final BenutzerRepository benutzerRepository;

    public CustomUserDetailsService(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Benutzer benutzer = benutzerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer nicht gefunden"));

        return new User(
                benutzer.getEmail(),
                benutzer.getPasswort(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

