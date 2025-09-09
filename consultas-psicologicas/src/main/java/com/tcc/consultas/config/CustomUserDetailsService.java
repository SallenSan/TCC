package com.tcc.consultas.config;

import com.tcc.consultas.model.UsuarioAdmin;
import com.tcc.consultas.repository.UsuarioAdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioAdminRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioAdminRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioAdmin u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));


        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new org.springframework.security.core.userdetails.User(
                u.getEmail(),
                u.getSenhaHash(),   // se não usa login Spring Security, pode ser qualquer hash
                authorities
        );
    }
}
