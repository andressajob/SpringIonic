package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.*;
import com.victorseger.svtech.repositories.ItemPedidoRepository;
import com.victorseger.svtech.repositories.PedidoRepository;
import com.victorseger.svtech.services.exceptions.DataIntegrityException;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;


@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;


    public Pedido find(Integer id) {
        if (id != null) {
            Optional<Pedido> obj = repo.findById(id);

            return obj.orElseThrow(() -> new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + ", Tipo: " + Pedido.class.getName()));
        }
        return null;
    }

    public Pedido getOne(Integer id) {
        return repo.getOne(id);
    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        if (pedido != null) {
            pedido.setId(null);
            pedido.setInstante(new Date());
            pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
            if (!pedido.getItens().isEmpty()) {
                double soma = 0;
                for (ItemPedido itemPedido : pedido.getItens()) {
                    soma += itemPedido.getPreco();
                }
                pedido.setValorTotal(soma);
            }
            pedido = repo.save(pedido);
        }
        return pedido;

    }

    public boolean existsById(Integer id) {
        if (id != null && id > 0)
            return repo.existsById(id);
        return false;
    }

    public List<Pedido> findAll() {
        return repo.findAll();
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir pedido");
        }

    }

    public Pedido update(Pedido pedido) {
        if (pedido != null) {
            Pedido newPedido = find(pedido.getId());
            updateData(newPedido, pedido);
            return repo.save(newPedido);
        }
        return null;
    }

    private void updateData(Pedido newPedido, Pedido pedido) {
        newPedido.setCliente(pedido.getCliente());
        newPedido.setEnderecoEntrega(pedido.getEnderecoEntrega());
    }

    public ItemPedido findItemById(Pedido pedido, Produto produto) {
        if (pedido != null && produto != null) {
            return itemPedidoRepository.findById_PedidoAndId_Produto(pedido, produto);
        }
        return null;
    }

    public void insertItem(ItemPedido itemPedido) {
        ItemPedidoPK pk = new ItemPedidoPK();
        Pedido pedido = repo.getOne(itemPedido.getPedido().getId());
        pk.setPedido(itemPedido.getPedido());
        pk.setProduto(itemPedido.getProduto());
        itemPedido.setId(pk);
        if (itemPedido.getDesconto() == null)
            itemPedido.setDesconto(0.0);
        itemPedido = itemPedidoRepository.save(itemPedido);
        double soma = 0;
        for (ItemPedido itemPedidos : pedido.getItens()) {
            soma += itemPedidos.getPreco();
        }
        pedido.setValorTotal(soma);
        pedido.getItens().add(itemPedido);
        repo.save(pedido);
    }

    public void updateItem(ItemPedido itemPedido) {
        ItemPedido newItem = itemPedidoRepository.findById_PedidoAndId_Produto(itemPedido.getPedido(), itemPedido.getProduto());
        updateDataItem(newItem, itemPedido);
        itemPedidoRepository.save(newItem);
    }

    private void updateDataItem(ItemPedido newItem, ItemPedido itemPedido) {
        ItemPedidoPK pk = new ItemPedidoPK();
        pk.setPedido(itemPedido.getPedido());
        pk.setProduto(itemPedido.getProduto());
        newItem.setId(pk);
        if (itemPedido.getDesconto() != null) newItem.setDesconto(itemPedido.getDesconto());
        else newItem.setDesconto(0.0);
        newItem.setProduto(itemPedido.getProduto());
        newItem.setPedido(itemPedido.getPedido());
        newItem.setPreco(itemPedido.getPreco());
        newItem.setQuantidade(itemPedido.getQuantidade());
    }

    public void deleteItem(ItemPedido itemPedido) {
        itemPedidoRepository.delete(itemPedidoRepository.findById_PedidoAndId_Produto(itemPedido.getPedido(), itemPedido.getProduto()));
    }

    public boolean existsItemPedido(Pedido pedido, Produto produto) {
        return itemPedidoRepository.findById_PedidoAndId_Produto(pedido, produto) != null;
    }

}