package market.secondhandmarket.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import market.secondhandmarket.modelo.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findFirstByEmail(String email);
    
}
