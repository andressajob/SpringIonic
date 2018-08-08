package com.victorseger.cursomc.resources;


import com.victorseger.cursomc.domain.Categoria;
import com.victorseger.cursomc.domain.Cidade;
import com.victorseger.cursomc.domain.Estado;
import com.victorseger.cursomc.dto.CidadeDTO;
import com.victorseger.cursomc.dto.EstadoDTO;
import com.victorseger.cursomc.services.CidadeService;
import com.victorseger.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

    @Autowired
    private EstadoService service;

    @Autowired
    private CidadeService cidadeService;

    private boolean error = false;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EstadoDTO>> findAll() {
        List<Estado> list = service.findAll();
        List<EstadoDTO> dtoList = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
        List<Cidade> list = cidadeService.findByEstado(estadoId);
        List<CidadeDTO> dtoList = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }


    @GetMapping("/lista")
    public ModelAndView listStates(Model model) {
        model.addAttribute("states", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("/client/address/state/list");
    }

    @GetMapping("/novo")
    public ModelAndView newState(Model model) {
        model.addAttribute("state", new Estado());
        model.addAttribute("action", "new");
        return new ModelAndView("/client/address/state/form");
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
        return new ModelAndView("/client/address/state/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteState(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/estados/lista");
    }

}
