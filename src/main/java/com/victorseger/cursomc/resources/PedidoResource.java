package com.victorseger.cursomc.resources;

import com.sun.rowset.internal.Row;
import com.victorseger.cursomc.domain.*;
import com.victorseger.cursomc.services.ClienteService;
import com.victorseger.cursomc.services.PedidoService;
import com.victorseger.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.FetchProfile;
import javax.validation.Valid;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService service;

    @Autowired
    private ClienteService clientService;

    @Autowired
    private ProdutoService productService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pedido> find(@PathVariable Integer id) {
        Pedido obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody Pedido pedido) {

        pedido = service.insert(pedido);

        //pega a URI do novo recurso inserido e adiciona ao final do "current request"
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();

        return ResponseEntity.created(uri).build();

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<Pedido>> findPage(
            //anotação que torna os valores opcionais e não requisitos para a função
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

        Page<Pedido> pedidoPage = service.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(pedidoPage);
    }

    @GetMapping("/lista")
    public ModelAndView listOrders(Model model) {
        model.addAttribute("orders", service.findAll());
        return new ModelAndView("/order/list");
    }

    @GetMapping("/novo")
    public ModelAndView newOrder(Model model) {
        model.addAttribute("order", new Pedido());
        model.addAttribute("action", "new");
        model.addAttribute("clients", clientService.findAll());
        return new ModelAndView("/order/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveOrder(Pedido pedido) {
        if (pedido.getId() != null) service.update(pedido);
        else service.insert(pedido);
        return new ModelAndView("redirect:/pedidos/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView updateOrder(@PathVariable int id, Model model) {
        model.addAttribute("order", service.find(id));
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("action", "edit");
        return new ModelAndView("/order/form");
    }

    @GetMapping("/itemPedidos")
    public @ResponseBody
    List<Endereco> findAllAddress(@RequestParam(value = "clientId", required = true) Integer clientId) {
        return clientService.findAllAddressByClientId(clientId);
    }

    @GetMapping("/itens/{id}")
    public ModelAndView addItem(@PathVariable int id, Model model) {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(service.find(id));
        model.addAttribute("order", service.find(id));
        model.addAttribute("action", "new");
        model.addAttribute("items", service.find(id).getItens());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newItem", itemPedido);
        return new ModelAndView("/order/items/form");
    }

    @GetMapping("/itens/{id}/editar/{idItem}")
    public ModelAndView editItem(@PathVariable int id, @PathVariable int idItem, Model model) {
        model.addAttribute("order", service.find(id));
        model.addAttribute("action", "edit");
        model.addAttribute("items", service.find(id).getItens());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newItem", service.findItemById(service.find(id), productService.find(idItem)));
        return new ModelAndView("/order/items/form");
    }

    @GetMapping("/itens/{id}/excluir/{idItem}")
    public ModelAndView deleteItem(@PathVariable int id, @PathVariable int idItem, Model model) {
        service.deleteItem(service.findItemById(service.find(id),productService.find(idItem)));
        return new ModelAndView("redirect:/pedidos/itens/" + id);
    }

    @PostMapping("/salvarItem")
    public ModelAndView saveItem(@ModelAttribute("newItem") ItemPedido itemPedido) {
        if (service.existsItemPedido(itemPedido.getPedido(), itemPedido.getProduto())){
            service.updateItem(itemPedido);
        } else{
            service.insertItem(itemPedido);
        }
        return new ModelAndView("redirect:/pedidos/itens/" + itemPedido.getPedido().getId());
    }

}
