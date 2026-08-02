package market.secondhandmarket.seguridad;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.repositorios.UsuarioRepository;

@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repositorio;

    public UserDetailsServiceImpl(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositorio.findFirstByEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return User.withUsername(username)
                .password(usuario.getPassword())
                .disabled(false)
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();
    }
}
