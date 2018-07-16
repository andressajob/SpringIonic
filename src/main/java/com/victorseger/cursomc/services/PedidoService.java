package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.domain.ItemPedido;
import com.victorseger.cursomc.domain.PagamentoComBoleto;
import com.victorseger.cursomc.domain.Pedido;
import com.victorseger.cursomc.domain.enums.EstadoPagamento;
import com.victorseger.cursomc.repositories.ItemPedidoRepository;
import com.victorseger.cursomc.repositories.PagamentoRepository;
import com.victorseger.cursomc.repositories.PedidoRepository;
import com.victorseger.cursomc.security.UserSS;
import com.victorseger.cursomc.services.exceptions.AuthorizationException;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;


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
    private EmailService emailService;


    public Pedido find(Integer id) {
        Optional<Pedido> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Pedido.class.getName()));
    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        pedido.setId(null);
        pedido.setInstante(new Date());
        pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
        pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        pedido.getPagamento().setPedido(pedido);
        if(pedido.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pgtoBol = (PagamentoComBoleto) pedido.getPagamento();
            boletoService.preencherPagamentoComBoleto(pgtoBol, pedido.getInstante());
        }
        pedido = repo.save(pedido);
        pagamentoRepository.save(pedido.getPagamento());
        for (ItemPedido itemPedido : pedido.getItens()) {
            itemPedido.setDesconto(0.0);
            itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId()));
            itemPedido.setPreco(itemPedido.getProduto().getPreco());
            itemPedido.setPedido(pedido);
        }

        itemPedidoRepository.saveAll(pedido.getItens());
        emailService.sendOrderConfirmationHtmlEmail(pedido);
        return pedido;

    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS userSS = UserService.authenticated();
        if(userSS == null) { // caso cliente não esteja authenticado, não pegará nenhum dado
            throw new AuthorizationException("Acesso negado");
        }
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Cliente cliente = clienteService.find(userSS.getId());
        return repo.findByCliente(cliente,pageRequest);
    }
}