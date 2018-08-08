package com.victorseger.cursomc.resources;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.domain.Endereco;
import com.victorseger.cursomc.domain.Produto;
import com.victorseger.cursomc.domain.enums.Perfil;
import com.victorseger.cursomc.domain.enums.TipoCliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.dto.ClienteNewDTO;
import com.victorseger.cursomc.services.CidadeService;
import com.victorseger.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @Autowired
    private CidadeService cidadeService;

    private boolean error = false;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    //endpoint para busca de cliente por email
    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email) {
        Cliente cliente = service.findByEmail(email);
        return ResponseEntity.ok().body(cliente);
    }


    /*@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
        Cliente cliente = service.fromDTO(clienteDTO);
        cliente.setId(id);
        cliente = service.update(cliente);
        return ResponseEntity.noContent().build();
    }*/

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clienteList = service.findAll();
        //convertendo lista de clientes para lista de DTO (com os dados selecionados para exibir)
        List<ClienteDTO> clienteDTO = clienteList.stream().map(cat -> new ClienteDTO(cat)).collect(Collectors.toList());
        return ResponseEntity.ok().body(clienteDTO);
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(
            //anotação que torna os valores opcionais e não requisitos para a função
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<Cliente> clientePage = service.findPage(page, linesPerPage, orderBy, direction);
        Page<ClienteDTO> clienteDTO = clientePage.map(ClienteDTO::new);
        return ResponseEntity.ok().body(clienteDTO);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteDTO) {
        Cliente cliente = service.fromDTO(clienteDTO);
        cliente = service.insert(cliente);

        //pega a URI do novo recurso inserido e adiciona ao final do "current request"
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).build();

    }

/*    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile file) {

        URI uri = service.uploadProfilePicture(file);
        return ResponseEntity.created(uri).build();

    }*/

    @GetMapping("/lista")
    public ModelAndView listClients(Model model) {
        model.addAttribute("clients", service.findAll());
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
        service.delete(id);
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
