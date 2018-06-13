package com.victorseger.cursomc.resources;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.dto.ClienteNewDTO;
import com.victorseger.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public ResponseEntity<Cliente> find(@PathVariable Integer id) {
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
        Cliente cliente = service.fromDTO(clienteDTO);
        cliente.setId(id);
        cliente = service.update(cliente);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clienteList = service.findAll();
        //convertendo lista de clientes para lista de DTO (com os dados selecionados para exibir)
        List<ClienteDTO> clienteDTO = clienteList.stream().map(cat -> new ClienteDTO(cat)).collect(Collectors.toList());
        return ResponseEntity.ok().body(clienteDTO);
    }

    @RequestMapping(value = "/page",method=RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(
            //anotação que torna os valores opcionais e não requisitos para a função
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24")Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome")String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC")String direction) {

        Page<Cliente> clientePage = service.findPage(page,linesPerPage,orderBy,direction);
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
}
