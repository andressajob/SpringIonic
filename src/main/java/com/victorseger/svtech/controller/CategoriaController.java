package com.victorseger.svtech.controller;

import com.victorseger.svtech.domain.Categoria;
import com.victorseger.svtech.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping(value = "/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;
    private boolean error = false;

    @GetMapping("/lista")
    public ModelAndView listCategories(Model model) {
        model.addAttribute("categories", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("product/category/list");
    }

    @GetMapping("/novo")
    public ModelAndView newCategory(Model model) {
        model.addAttribute("category", new Categoria());
        model.addAttribute("action", "new");
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("product/category/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveCategory(Categoria categoria) {
        if (categoria.getId() != null){
            service.update(categoria);
        } else {
            Categoria savedCategoria = service.insert(categoria);
            if (savedCategoria == null){
                error = true;
                return new ModelAndView("redirect:/categorias/novo");
            }
        }
        return new ModelAndView("redirect:/categorias/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editCategory(Model model, @PathVariable int id) {
        model.addAttribute("category", service.find(id));
        model.addAttribute("action", "edit");
        return new ModelAndView("product/category/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteCategory(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/categorias/lista");
    }


}
