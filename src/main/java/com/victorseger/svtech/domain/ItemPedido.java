package com.victorseger.svtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ItemPedido implements Serializable {
    private static final long serialVersionUID = 1L;

    //torna esse id não serializado (para não ocupar espaço na memória e não causar referencia ciclica)
    @JsonIgnore
    //id embutido em um tipo auxiliar (chave composta entre produto e pedido)
    @EmbeddedId
    @AttributeOverrides
            ({
                    @AttributeOverride(name = "pedido", column = @Column(name = "pedido_id")),
                    @AttributeOverride(name = "produto", column = @Column(name = "produto_id"))
            })
    private ItemPedidoPK id = new ItemPedidoPK();

    private Double desconto;
    private Integer quantidade;
    private Double preco;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Double preco) {
        id.setPedido(pedido);
        id.setProduto(produto);
        this.desconto = desconto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    //para evitar a referencia ciclica
    @JsonIgnore
    public Pedido getPedido() {
        return id.getPedido();
    }

    public void setPedido(Pedido pedido) {
        id.setPedido(pedido);
    }

    //este get pode ser serializado, pois queremos ver os produtos dentro do item pedido
    public Produto getProduto() {
        return id.getProduto();
    }

    public void setProduto(Produto produto) {
        id.setProduto(produto);
    }

    public ItemPedidoPK getId() {
        return id;
    }

    public void setId(ItemPedidoPK id) {
        this.id = id;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        if (this.getId() != null) {
            if (this.getId().getProduto() != null){
                this.preco = this.quantidade * this.getId().getProduto().getPreco();
                this.preco -= this.desconto;
            }
        }
        return preco;
    }

    public void setPreco(Double valor) {
        this.preco = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

/*    @Override
    public String toString() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return getProduto().getNome() + ", Qtd: " + getQuantidade() + ", Preço Unitário: " + numberFormat.format(getPreco()) + ", Subtotal: " + numberFormat.format(getSubTotal()) + "\n";
    }*/
}
