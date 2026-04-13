package edu.eci.dosw.techcup_futbol.service;

import edu.eci.dosw.techcup_futbol.entity.UserEntity;
import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Objetivo de loadUserByUsername:
     * Cargar un usuario para el flujo de autenticacion de Spring Security usando el email
     * como identificador (username) y devolverlo como UserDetails.
     *
     * Para que sirve UserDetails:
     * Es el contrato que Spring Security usa para representar al usuario autenticado
     * (username, password, estado de cuenta y autoridades).
     *
     * Para que sirve SimpleGrantedAuthority:
     * Representa una autoridad/rol en formato que Spring Security entiende para
     * decisiones de autorizacion.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String normalizedEmail = normalizeEmail(email);
        UserEntity user = userRepository.findByEmail(normalizedEmail);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + normalizedEmail);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .map(GrantedAuthority.class::cast)
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
