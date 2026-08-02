package market.secondhandmarket.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import market.secondhandmarket.modelo.Compra;
import market.secondhandmarket.modelo.Usuario;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    List<Compra> findByPropietario(Usuario propietario);
}
