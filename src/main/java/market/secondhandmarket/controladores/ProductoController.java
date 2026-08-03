package market.secondhandmarket.controladores;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import market.secondhandmarket.modelo.Producto;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.servicios.ProductoServicio;
import market.secondhandmarket.servicios.UsuarioServicio;
import market.secondhandmarket.upload.StorageService;

@Controller
@RequestMapping("/app")
public class ProductoController {

    private final ProductoServicio productoServicio;
    private final UsuarioServicio usuarioServicio;
    final StorageService storageService;

    public ProductoController(ProductoServicio productoServicio, UsuarioServicio usuarioServicio, StorageService storageService) {
        this.productoServicio = productoServicio;
        this.usuarioServicio = usuarioServicio;
        this.storageService = storageService;
    }

    @GetMapping("/misproductos")
    public String misProductos(Model model, Authentication authentication,
            @RequestParam(name = "q", required = false) String query) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        List<Producto> misproductos;
        if (query != null && !query.trim().isEmpty()) {
            misproductos = productoServicio.buscarMisProductos(query, usuario);
        } else {
            misproductos = productoServicio.productosDeUnPropietario(usuario);
        }
        model.addAttribute("misproductos", misproductos);
        return "app/producto/lista";
    }

    @GetMapping("/misproductos/{id}/eliminar")
    public String eliminarProducto(@PathVariable long id, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        Producto p = productoServicio.findById(id);
        if (p != null && p.getPropietario() != null && p.getPropietario().getId() == usuario.getId()
                && p.getCompra() == null) {
            productoServicio.borrar(p);
        }
        return "redirect:/app/misproductos";
    }

    @GetMapping("/producto/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "app/producto/form";
    }

    @PostMapping("/producto/nuevo/submit")
    public String nuevoProductoSubmit(@ModelAttribute Producto producto, Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            producto.setImagenDatos(file.getBytes());
            producto.setImagenTipo(file.getContentType());
        }

        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        producto.setPropietario(usuario);
        productoServicio.insertar(producto);
        return "redirect:/app/misproductos";
    }

    @GetMapping("/producto/editar/{id}")
    public String editarProducto(@PathVariable long id, Model model, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        Producto p = productoServicio.findById(id);
        if (p != null && p.getPropietario() != null && p.getPropietario().getId() == usuario.getId()) {
            model.addAttribute("producto", p);
            return "app/producto/form";
        }
        return "redirect:/app/misproductos";
    }

    @PostMapping("/producto/editar/submit")
    public String editarProductoSubmit(@ModelAttribute Producto producto, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        producto.setPropietario(usuario);
        productoServicio.editar(producto);
        return "redirect:/app/misproductos";
    }
}
