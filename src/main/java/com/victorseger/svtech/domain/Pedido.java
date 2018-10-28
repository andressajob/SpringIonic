package com.victorseger.svtech.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idPedido;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date instante;

    private Double valorTotal;
    //@JsonManagedReference - foi recomendada a remoção pois apresentou problemas durante a execução
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "pedido")
    private Pagamento pagamento;

    //@JsonManagedReference - foi recomendada a remoção pois apresentou problemas durante a execução
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "endereco_de_entrega_id")
    private Endereco enderecoEntrega;


    @OneToMany(mappedBy = "id.pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    //lista de itens associados ao pedido
    private Set<ItemPedido> itens = new HashSet<>();

    public Pedido() {
    }

    public Pedido(Integer id, Date instante, Cliente cliente, Endereco enderecoEntrega) {
        this.idPedido = id;
        this.instante = instante;
        this.cliente = cliente;
        this.enderecoEntrega = enderecoEntrega;
    }

    public double getValorTotal() {
        double soma = 0;
        for (ItemPedido itemPedido : itens) {
            soma += itemPedido.getPreco();
        }
        return soma;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return idPedido;
    }

    public void setId(Integer id) {
        this.idPedido = id;
    }

    public Date getInstante() {
        return instante;
    }

    public void setInstante(Date instante) {
        this.instante = instante;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Set<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(idPedido, pedido.idPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + idPedido +
                '}';
    }

    /*    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        StringBuilder builder = new StringBuilder();
        builder.append("Pedido número: ");
        builder.append(getId());
        builder.append(", Instante: ");
        builder.append(simpleDateFormat.format(getInstante()));
        builder.append(", Cliente: ");
        builder.append(getCliente().getNome());
        builder.append(", Situação do Pagamento: ");
        builder.append(getPagamento().getEstado().getDescricao());
        builder.append("\nDetalhes: \n");
        for(ItemPedido itemPedido : getItens()) {
            builder.append(itemPedido.toString());
        };
        builder.append(", Valor Total: ");
        builder.append(numberFormat.format(getValorTotal()));
        return builder.toString();
    }*/
}
