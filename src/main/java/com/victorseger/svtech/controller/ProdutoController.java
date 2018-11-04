package com.victorseger.svtech.controller;

import com.victorseger.svtech.domain.Produto;
import com.victorseger.svtech.services.CategoriaService;
import com.victorseger.svtech.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping(value = "/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private CategoriaService categoriaService;

    private boolean error;
    private boolean errorCategories;

    @GetMapping("/lista")
    public ModelAndView listProducts(Model model) {
        model.addAttribute("products", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("product/list");
    }

    @GetMapping("/novo")
    public ModelAndView newProduct(Model model) {
        model.addAttribute("product", new Produto());
        model.addAttribute("categories", categoriaService.findAll());
        model.addAttribute("action", "new");
        model.addAttribute("error", error);
        model.addAttribute("errorCategories", errorCategories);
        error = false;
        errorCategories = false;
        return new ModelAndView("product/form");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editProduct(Model model, @PathVariable int id) {
        model.addAttribute("product", service.find(id));
        model.addAttribute("categories", categoriaService.findAll());
        model.addAttribute("action", "edit");
        return new ModelAndView("product/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteProduct(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/produtos/lista");
    }

    @PostMapping("/salvar")
    public ModelAndView saveProduct(@Valid Produto produto, @RequestParam(value = "categories", required = false) int[] categories) {
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (categoriaService.existsById(categories[i])) {
                    produto.getCategorias().add(categoriaService.find(categories[i]));
                }
            }
        }
        Produto savedProduto = service.save(produto);
        if (savedProduto != null && !savedProduto.getCategorias().isEmpty()){
            return new ModelAndView("redirect:/produtos/lista");
        } else {
            if (savedProduto == null){
                error = true;
            } else if (savedProduto.getCategorias().isEmpty()){
                errorCategories = true;
            }
            return new ModelAndView("redirect:/produtos/novo");
        }
    }

}
