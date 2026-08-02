package market.secondhandmarket.controladores;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import market.secondhandmarket.modelo.Producto;
import market.secondhandmarket.servicios.ProductoServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/public")
public class ZonaPublicaController {

    final ProductoServicio productoServicio;

    ZonaPublicaController(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @ModelAttribute("productos")
    public List<Producto> productosNoVendidos(){
        return productoServicio.productosSinVender();
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, @RequestParam(name = "q", required = false) String query) {
        if(query != null)
            model.addAttribute("productos", productoServicio.buscar(query));
        return "index";
    }

    @GetMapping("/producto/{id}")
    public String showProduct(Model model, @PathVariable Long id) {
        Producto result = productoServicio.findById(id);
        if (result != null){
            model.addAttribute("producto", result);
            return "producto";
        }
        return "redirect:/public/";
    }



}
