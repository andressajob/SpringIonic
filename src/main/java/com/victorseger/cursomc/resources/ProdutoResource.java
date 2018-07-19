package com.victorseger.cursomc.resources;

import com.victorseger.cursomc.domain.Produto;
import com.victorseger.cursomc.dto.ProdutoDTO;
import com.victorseger.cursomc.resources.utils.URL;
import com.victorseger.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<Produto> find(@PathVariable Integer id) {
        Produto obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "0") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome")String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction) {

        String nomeDec = URL.decodeParam(nome);
        List<Integer> ids = URL.decodeIntList(categorias);
        Page<Produto> produtoPage = service.search(nomeDec,ids,page,linesPerPage,orderBy,direction);
        Page<ProdutoDTO> produtoDTO = produtoPage.map(ProdutoDTO::new);
        return ResponseEntity.ok().body(produtoDTO);
    }

    @GetMapping("/lista")
    public ModelAndView listProducts(Model model) {
        model.addAttribute("products", service.findAll());
        return new ModelAndView("/product/list");
    }

    @GetMapping("/novo")
    public ModelAndView newProduct(Model model) {
        model.addAttribute("product", new ProdutoDTO());
        model.addAttribute("action", "new");
        return new ModelAndView("/product/new");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editProduct(Model model, @PathVariable int id) {
        model.addAttribute("product", service.find(id));
        model.addAttribute("action", "edit");
        return new ModelAndView("/product/new");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteProduct(@PathVariable int id) {
        service.delete(id);
        return new ModelAndView("redirect:/produtos/lista");
    }

    @PostMapping("/salvar")
    public ModelAndView saveProduct(@Valid Produto produto) {
        service.save(produto);
        return new ModelAndView("redirect:/produtos/lista");
    }

}
