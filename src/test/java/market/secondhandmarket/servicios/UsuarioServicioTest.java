package market.secondhandmarket.servicios;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.repositorios.UsuarioRepository;

@SpringBootTest
class UsuarioServicioTest {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void registrarPersisteUsuarioEnLaBaseDeDatos() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setApellidos("Pérez");
        usuario.setEmail("ana@example.com");
        usuario.setPassword("123456");

        Usuario guardado = usuarioServicio.registrar(usuario);
        Usuario encontrado = usuarioRepository.findFirstByEmail("ana@example.com");

        assertThat(guardado.getId()).isGreaterThan(0);
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getEmail()).isEqualTo("ana@example.com");
        assertThat(encontrado.getPassword()).isNotEqualTo("123456");
    }
}
