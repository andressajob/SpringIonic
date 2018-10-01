package com.victorseger.cursomc.repositories;

import com.victorseger.cursomc.domain.ItemPedido;
import com.victorseger.cursomc.domain.Pedido;
import com.victorseger.cursomc.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    ItemPedido findById_PedidoAndId_Produto(Pedido pedido, Produto produto);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ITEM_PEDIDO I INNER JOIN PRODUTO P ON P.id = I.produto_id GROUP BY I.produto_id ORDER BY I.quantidade DESC ",
            nativeQuery = true)
    List<ItemPedido> topSellingProducts();
}
