package market.secondhandmarket.controladores;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import market.secondhandmarket.modelo.Producto;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.servicios.ProductoServicio;
import market.secondhandmarket.servicios.UsuarioServicio;


@Controller
public class FilesController {
    
    private final ProductoServicio productoServicio;
    private final UsuarioServicio usuarioServicio;

    public FilesController(ProductoServicio productoServicio, UsuarioServicio usuarioServicio) {
        this.productoServicio = productoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/app/producto/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> servirImagenProducto(@PathVariable long id) {
        Producto producto = productoServicio.findById(id);
        if (producto == null || producto.getImagen() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(producto.getImagenTipo()))
                .body(producto.getImagenDatos());
    }

    @GetMapping("/app/usuario/avatar/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> servirAvatarUsuario(@PathVariable long id) {
        Usuario usuario = usuarioServicio.findById(id); 
        if (usuario == null || usuario.getAvatarDatos() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(usuario.getAvatarTipo()))
                .body(usuario.getAvatarDatos());
    }
    

}
