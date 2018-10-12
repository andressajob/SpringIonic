package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.*;
import com.victorseger.cursomc.domain.enums.EstadoPagamento;
import com.victorseger.cursomc.repositories.ItemPedidoRepository;
import com.victorseger.cursomc.repositories.PagamentoRepository;
import com.victorseger.cursomc.repositories.PedidoRepository;
import com.victorseger.cursomc.security.UserSS;
import com.victorseger.cursomc.services.exceptions.AuthorizationException;
import com.victorseger.cursomc.services.exceptions.DataIntegrityException;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CategoriaService categoriaService;

/*
    @Autowired
    private EmailService emailService;
*/


    public Pedido find(Integer id) {
        Optional<Pedido> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Pedido.class.getName()));
    }

    public Pedido getOne(Integer id) {
        return repo.getOne(id);
    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        pedido.setId(null);
        pedido.setInstante(new Date());
        pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
//        pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
//        pedido.getPagamento().setPedido(pedido);
        /*if(pedido.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pgtoBol = (PagamentoComBoleto) pedido.getPagamento();
            boletoService.preencherPagamentoComBoleto(pgtoBol, pedido.getInstante());
        }
        pagamentoRepository.save(pedido.getPagamento());
        for (ItemPedido itemPedido : pedido.getItens()) {
            itemPedido.setDesconto(0.0);
            itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId()));
            itemPedido.setPreco(itemPedido.getProduto().getPreco());
            itemPedido.setPedido(pedido);
        }*/
        if (!pedido.getItens().isEmpty()) {
            double soma = 0;
            for (ItemPedido itemPedido : pedido.getItens()) {
                soma += itemPedido.getPreco();
            }
            pedido.setValorTotal(soma);
        }
        pedido = repo.save(pedido);

        //itemPedidoRepository.saveAll(pedido.getItens());
        //emailService.sendOrderConfirmationHtmlEmail(pedido);
        return pedido;

    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null) { // caso cliente não esteja authenticado, não pegará nenhum dado
            throw new AuthorizationException("Acesso negado");
        }
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Cliente cliente = clienteService.find(userSS.getId());
        return repo.findByCliente(cliente, pageRequest);
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
        Pedido newPedido = find(pedido.getId());
        updateData(newPedido, pedido);
        return repo.save(newPedido);
    }

    private void updateData(Pedido newPedido, Pedido pedido) {
        newPedido.setCliente(pedido.getCliente());
        newPedido.setEnderecoEntrega(pedido.getEnderecoEntrega());
    }

    public ItemPedido findItemById(Pedido pedido, Produto produto) {
        return itemPedidoRepository.findById_PedidoAndId_Produto(pedido, produto);
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

    public List<ItemPedido> topSellingProducts() {
        return itemPedidoRepository.topSellingProducts();
    }

    public List<Pedido> topOrders() {
        return repo.ordersByTotalValor();
    }

    public List<Pedido> filterOrders(LocalDate initDate, LocalDate finalDate) {
        return repo.filterOrders(initDate, finalDate);
    }

    public List<ItemPedido> filterProducts(LocalDate initialDate, LocalDate finalDate) {
        return itemPedidoRepository.filterProducts(initialDate, finalDate);
    }

    public Integer[][] topSellingCategories() {
        return itemPedidoRepository.topSellingCategories();
    }

    public Map<Categoria, Integer> transformMatrix(Integer[][] matrix) {
        Map<Categoria, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            map.put(categoriaService.find(matrix[i][0]), matrix[i][1]);
        }
        return map;
    }

    public Integer[][] filterCategories(LocalDate initDate, LocalDate finalDate) {
        return itemPedidoRepository.filterCategories(initDate, finalDate);
    }
}