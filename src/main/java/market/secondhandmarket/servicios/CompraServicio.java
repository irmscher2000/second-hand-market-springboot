package market.secondhandmarket.servicios;

import java.util.List;
import org.springframework.stereotype.Service;

import market.secondhandmarket.modelo.Compra;
import market.secondhandmarket.modelo.Producto;
import market.secondhandmarket.modelo.Usuario;
import market.secondhandmarket.repositorios.CompraRepository;

@Service
public class CompraServicio {
    
    final CompraRepository repositorio;

    final ProductoServicio productoServicio;

    CompraServicio(CompraRepository repositorio, ProductoServicio productoServicio) {
        this.repositorio = repositorio;
        this.productoServicio = productoServicio;
    }

    public Compra insertar(Compra c, Usuario u){
        c.setPropietario(u);
        return repositorio.save(c);
    }

    public Compra insertar(Compra c){
        return repositorio.save(c);
    }

    public Producto addProductoCompra(Producto p, Compra c){
        p.setCompra(c);
        return productoServicio.editar(p);
    }

    public Compra buscarPorId(long id){
        return repositorio.findById(id).orElse(null);
    }

    public List<Compra> todas(){
        return repositorio.findAll();
    }

    public List<Compra> porPropietario(Usuario u){
        return repositorio.findByPropietario(u);
    }
    
    

}
