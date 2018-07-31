package com.victorseger.cursomc.repositories;

import com.victorseger.cursomc.domain.ItemPedido;
import com.victorseger.cursomc.domain.Pedido;
import com.victorseger.cursomc.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    ItemPedido findById_PedidoAndId_Produto(Pedido pedido, Produto produto);
}
