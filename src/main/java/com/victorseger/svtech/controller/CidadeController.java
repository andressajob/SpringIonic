package com.victorseger.svtech.controller;

import com.victorseger.svtech.domain.Cidade;
import com.victorseger.svtech.services.CidadeService;
import com.victorseger.svtech.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/cidades")
public class CidadeController {

    @Autowired
    private CidadeService service;

    @Autowired
    private  EstadoService estadoService;

    private boolean error = false;

    @GetMapping("/lista")
    public ModelAndView listCities(Model model) {
        model.addAttribute("cities", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("/client/address/city/list");
    }

    @GetMapping("/novo")
    public ModelAndView newCity(Model model) {
        model.addAttribute("city", new Cidade());
        model.addAttribute("states", estadoService.findAll());
        model.addAttribute("action", "new");
        return new ModelAndView("/client/address/city/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveCity(Cidade cidade) {
        if (cidade.getId() != null) service.update(cidade);
        else service.insert(cidade);
        return new ModelAndView("redirect:/cidades/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editCity(Model model, @PathVariable int id) {
        model.addAttribute("city", service.getOne(id));
        model.addAttribute("states", estadoService.findAll());
        model.addAttribute("action", "edit");
        return new ModelAndView("/client/address/city/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteCity(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/cidades/lista");
    }
}
