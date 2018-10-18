package com.victorseger.cursomc.controller;

import com.victorseger.cursomc.domain.Categoria;
import com.victorseger.cursomc.dto.CategoriaDTO;
import com.victorseger.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Controller
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
        return new ModelAndView("/product/category/list");
    }

    @GetMapping("/novo")
    public ModelAndView newCategory(Model model) {
        model.addAttribute("category", new Categoria());
        model.addAttribute("action", "new");
        return new ModelAndView("/product/category/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveCategory(Categoria categoria) {
        if (categoria.getId() != null) service.update(categoria);
        else service.insert(categoria);
        return new ModelAndView("redirect:/categorias/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editCategory(Model model, @PathVariable int id) {
        model.addAttribute("category", service.find(id));
        model.addAttribute("action", "edit");
        return new ModelAndView("/product/category/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteCategory(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/categorias/lista");
    }


}
