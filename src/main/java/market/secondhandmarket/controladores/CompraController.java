package market.secondhandmarket.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import market.secondhandmarket.modelo.Compra;
import market.secondhandmarket.modelo.Producto;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.servicios.CompraServicio;
import market.secondhandmarket.servicios.ProductoServicio;
import market.secondhandmarket.servicios.UsuarioServicio;

@Controller
@RequestMapping("/app")
public class CompraController {

    private final CompraServicio compraServicio;
    private final ProductoServicio productoServicio;
    private final UsuarioServicio usuarioServicio;

    public CompraController(CompraServicio compraServicio, ProductoServicio productoServicio,
            UsuarioServicio usuarioServicio) {
        this.compraServicio = compraServicio;
        this.productoServicio = productoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/miscompras")
    public String misCompras(Model model, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        List<Compra> misCompras = compraServicio.porPropietario(usuario);
        model.addAttribute("mis_compras", misCompras);
        return "app/compra/listado";
    }

    @GetMapping("/compra/factura/{id}")
    public String factura(@PathVariable long id, Model model, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        Compra compra = compraServicio.buscarPorId(id);
        if (compra == null || compra.getPropietario() == null || compra.getPropietario().getId() != usuario.getId()) {
            return "redirect:/app/miscompras";
        }
        List<Producto> productos = productoServicio.productosDeUnaCompra(compra);
        double totalCompra = productos.stream().mapToDouble(Producto::getPrecio).sum();
        model.addAttribute("compra", compra);
        model.addAttribute("productos", productos);
        model.addAttribute("total_compra", totalCompra);
        return "app/compra/factura";
    }

    @GetMapping("/carrito")
    public String carrito(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        model.addAttribute("carrito", carrito);
        if (carrito != null && !carrito.isEmpty()) {
            double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();
            model.addAttribute("total_carrito", total);
        }
        return "app/compra/carrito";
    }

    @GetMapping("/carrito/add/{id}")
    public String addCarrito(@PathVariable long id, HttpSession session) {
        Producto p = productoServicio.findById(id);
        if (p != null && p.getCompra() == null) {
            @SuppressWarnings("unchecked")
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            boolean exists = carrito.stream().anyMatch(prod -> prod.getId() == p.getId());
            if (!exists) {
                carrito.add(p);
            }
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/app/carrito";
    }

    @GetMapping("/carrito/eliminar/{id}")
    public String eliminarCarrito(@PathVariable long id, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(p -> p.getId() == id);
            if (carrito.isEmpty()) {
                session.setAttribute("carrito", null);
            } else {
                session.setAttribute("carrito", carrito);
            }
        }
        return "redirect:/app/carrito";
    }

    @GetMapping("/carrito/finalizar")
    public String finalizarCarrito(HttpSession session, Authentication authentication) {
        Usuario usuario = usuarioServicio.buscarPorEmail(authentication.getName());
        @SuppressWarnings("unchecked")
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito != null && !carrito.isEmpty()) {
            Compra compra = compraServicio.insertar(new Compra(), usuario);
            for (Producto p : carrito) {
                compraServicio.addProductoCompra(p, compra);
            }
            session.setAttribute("carrito", null);
            return "redirect:/app/compra/factura/" + compra.getId();
        }
        return "redirect:/app/carrito";
    }
}
