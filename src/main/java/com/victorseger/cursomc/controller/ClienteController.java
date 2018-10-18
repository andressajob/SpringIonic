package com.victorseger.cursomc.controller;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.domain.Endereco;
import com.victorseger.cursomc.domain.enums.Perfil;
import com.victorseger.cursomc.domain.enums.TipoCliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.dto.ClienteNewDTO;
import com.victorseger.cursomc.services.CidadeService;
import com.victorseger.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private CidadeService cidadeService;

    private boolean error = false;

    @GetMapping("/lista")
    public ModelAndView listClients(Model model) {
        model.addAttribute("clients", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("/client/list");
    }

    @GetMapping("/novo")
    public ModelAndView newClient(Model model) {
        model.addAttribute("client", new Cliente());
        model.addAttribute("action", "new");
        model.addAttribute("types", TipoCliente.values());
        model.addAttribute("profiles", Perfil.values());
        return new ModelAndView("/client/form");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editClient(Model model, @PathVariable int id) {
        model.addAttribute("client", service.find(id));
        model.addAttribute("action", "edit");
        model.addAttribute("types", TipoCliente.values());
        model.addAttribute("profiles", Perfil.values());
        return new ModelAndView("/client/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteClient(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/clientes/lista");
    }

    @PostMapping("/salvar")
    public ModelAndView saveClient(Cliente cliente) {
        if (cliente.getId() != null) service.update(cliente);
        else service.insert(cliente);
        return new ModelAndView("redirect:/clientes/lista");
    }

    @GetMapping("/enderecos/{id}")
    public ModelAndView addAddress(@PathVariable int id, Model model) {
        Endereco endereco = new Endereco();
        model.addAttribute("client", service.find(id));
        model.addAttribute("addresses", service.find(id).getEnderecos());
        model.addAttribute("cities", cidadeService.findAll());
        endereco.setCliente(service.find(id));
        model.addAttribute("newAddress", endereco);
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("/client/address/form");
    }

    @GetMapping("/enderecos/{id}/editar/{idEndereco}")
    public ModelAndView editAddress(@PathVariable int id, @PathVariable int idEndereco, Model model) {
        model.addAttribute("client", service.find(id));
        model.addAttribute("addresses", service.find(id).getEnderecos());
        model.addAttribute("cities", cidadeService.findAll());
        model.addAttribute("newAddress", service.addressById(idEndereco));
        return new ModelAndView("/client/address/form");
    }

    @GetMapping("/enderecos/{id}/excluir/{idEndereco}")
    public ModelAndView deleteAddress(@PathVariable int id, @PathVariable int idEndereco, Model model) {
        if (!service.deleteAddress(idEndereco)) error = true;
        return new ModelAndView("redirect:/clientes/enderecos/" + id);
    }

    @PostMapping("/salvarEndereco")
    public ModelAndView saveAddress(@ModelAttribute("newAddress") @Valid Endereco endereco) {
        if (endereco.getId() != null) {
            service.updateAddress(endereco);
        } else {
            service.insertAddress(endereco);
        }
        return new ModelAndView("redirect:/clientes/editar/" + endereco.getCliente().getId());
    }

}
