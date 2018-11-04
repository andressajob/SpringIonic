package com.victorseger.svtech.controller;


import com.victorseger.svtech.domain.Estado;
import com.victorseger.svtech.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/estados")
public class EstadoController {

    @Autowired
    private EstadoService service;

    private boolean error = false;

    @GetMapping("/lista")
    public ModelAndView listStates(Model model) {
        model.addAttribute("states", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("client/address/state/list");
    }

    @GetMapping("/novo")
    public ModelAndView newState(Model model) {
        model.addAttribute("state", new Estado());
        model.addAttribute("action", "new");
        return new ModelAndView("client/address/state/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveState(Estado estado) {
        if (estado.getId() != null) service.update(estado);
        else service.insert(estado);
        return new ModelAndView("redirect:/estados/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editState(Model model, @PathVariable int id) {
        model.addAttribute("state", service.getOne(id));
        model.addAttribute("action", "edit");
        return new ModelAndView("client/address/state/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteState(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/estados/lista");
    }

}
