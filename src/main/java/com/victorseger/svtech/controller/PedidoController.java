package com.victorseger.svtech.controller;

import com.victorseger.svtech.domain.*;
import com.victorseger.svtech.services.ClienteService;
import com.victorseger.svtech.services.PedidoService;
import com.victorseger.svtech.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;


@Controller
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @Autowired
    private ClienteService clientService;

    @Autowired
    private ProdutoService productService;

    private boolean error;
    private boolean saved;

    @GetMapping("/lista")
    public ModelAndView listOrders(Model model) {
        for (Pedido order : service.findAll()) {
            if (order.getItens().isEmpty()) {
                service.delete(order.getId());
            }
        }
        model.addAttribute("saved", saved);
        saved = false;
        model.addAttribute("orders", service.findAll());
        return new ModelAndView("order/list");
    }

    @PostMapping("/salvar")
    public ModelAndView saveOrder(Pedido pedido) {
        if (pedido.getId() != null) service.update(pedido);
        else service.insert(pedido);
        return new ModelAndView("redirect:/pedidos/itens" + pedido.getId());
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/editar/{id}")
    public ModelAndView updateOrder(@PathVariable int id, Model model) {
        return new ModelAndView("redirect:/pedidos/itens/" + id);
    }

    @GetMapping("/cancelar/{id}")
    public ModelAndView cancelOrder(@PathVariable int id, Model model) {
        service.delete(id);
        return new ModelAndView("redirect:/pedidos/lista/");
    }

    @GetMapping("/enderecos")
    public @ResponseBody
    List<Endereco> findAllAddress(@RequestParam(value = "clientId", required = true) Integer clientId) {
        return clientService.findAllAddressByClientId(clientId);
    }

    @GetMapping("/novo")
    public ModelAndView newOrder(Model model) {
        Pedido pedido = new Pedido();
        ItemPedido itemPedido = new ItemPedido();
        pedido.setCliente(clientService.find(1));
        pedido.setEnderecoEntrega(clientService.find(1).getEnderecos().get(0));
        pedido = service.insert(pedido);
        itemPedido.setPedido(pedido);
        model.addAttribute("error", error);
        error = false;
        model.addAttribute("order", pedido);
        model.addAttribute("action", "new");
        model.addAttribute("items", new HashSet<>());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newItem", itemPedido);
        return new ModelAndView("order/items/form");
    }

    @GetMapping("/itens/{id}")
    public ModelAndView addItem(@PathVariable int id, Model model) {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(service.find(id));
        model.addAttribute("order", service.find(id));
        model.addAttribute("action", "new");
        model.addAttribute("error", error);
        error = false;
        model.addAttribute("items", service.find(id).getItens());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newItem", itemPedido);
        return new ModelAndView("order/items/form");
    }

    @GetMapping("/itens/{id}/editar/{idItem}")
    public ModelAndView editItem(@PathVariable int id, @PathVariable int idItem, Model model) {
        model.addAttribute("order", service.find(id));
        model.addAttribute("action", "edit");
        model.addAttribute("items", service.find(id).getItens());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("newItem", service.findItemById(service.find(id), productService.find(idItem)));
        return new ModelAndView("order/items/form");
    }

    @GetMapping("/itens/{id}/excluir/{idItem}")
    public ModelAndView deleteItem(@PathVariable int id, @PathVariable int idItem, Model model) {
        service.deleteItem(service.findItemById(service.find(id), productService.find(idItem)));
        return new ModelAndView("redirect:/pedidos/itens/" + id);
    }

    @PostMapping("/salvarItem")
    public ModelAndView saveItem(@ModelAttribute("newItem") ItemPedido itemPedido) {
        if (service.existsItemPedido(itemPedido.getPedido(), itemPedido.getProduto())) {
            service.updateItem(itemPedido);
        } else {
            service.insertItem(itemPedido);
        }
        return new ModelAndView("redirect:/pedidos/itens/" + itemPedido.getPedido().getId());
    }

    @GetMapping("/valida/{id}")
    public ModelAndView validateOrder(@PathVariable int id) {
        if (service.find(id).getItens().isEmpty()) {
            error = true;
            service.delete(id);
            return new ModelAndView("redirect:/pedidos/novo/");
        } else {
            saved = true;
            return new ModelAndView("redirect:/pedidos/lista/");
        }
    }

    @RequestMapping(value = "preco/{product}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> responseEntity(@PathVariable("product") Integer product) {
        try {
            return new ResponseEntity<String>(String.valueOf(productService.find(product).getPreco()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "total/{sell}/{total}", method = RequestMethod.GET, produces = {MimeTypeUtils.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> total(@PathVariable("total") Double total, @PathVariable("sell") Integer sell) {
        try {
            service.getOne(sell).setValorTotal(total);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

}