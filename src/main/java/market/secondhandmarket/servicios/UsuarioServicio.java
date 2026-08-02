package market.secondhandmarket.servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.repositorios.UsuarioRepository;

@Service
public class UsuarioServicio {
    
    final UsuarioRepository repositorio;

    final PasswordEncoder passwordEncoder;

    UsuarioServicio(UsuarioRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(Usuario u){
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repositorio.save(u);
    }
    
    @Transactional
    public Usuario findById(long id){
        return repositorio.findById(id).orElse(null);
    }
    
    @Transactional
    public Usuario buscarPorEmail(String email){
        return repositorio.findFirstByEmail(email);
    }
}
