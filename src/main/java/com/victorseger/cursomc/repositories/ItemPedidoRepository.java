package com.victorseger.cursomc.repositories;

import com.victorseger.cursomc.domain.ItemPedido;
import com.victorseger.cursomc.domain.Pedido;
import com.victorseger.cursomc.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    ItemPedido findById_PedidoAndId_Produto(Pedido pedido, Produto produto);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ITEM_PEDIDO I INNER JOIN PRODUTO P ON P.id = I.produto_id GROUP BY I.produto_id ORDER BY I.quantidade DESC ",
            nativeQuery = true)
    List<ItemPedido> topSellingProducts();

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM ITEM_PEDIDO I INNER JOIN PRODUTO P ON P.id = I.produto_id" +
            " INNER JOIN PEDIDO PE ON PE.id_pedido = I.pedido_id AND CAST(PE.instante AS DATE) >= :inicio AND CAST(PE.instante AS DATE) <= :fim" +
            " GROUP BY I.produto_id,PE.ID_PEDIDO ORDER BY I.quantidade DESC",
            nativeQuery = true)
    List<ItemPedido> filterProducts(@Param("inicio")LocalDate initialDate, @Param("fim")LocalDate finalDate);

    @Transactional(readOnly = true)
    @Query(value = "SELECT c.id, sum(i.quantidade) as quantidade FROM (((item_pedido i " +
            "INNER JOIN produto p ON p.id = i.produto_id) " +
            "INNER JOIN produto_categoria pc ON pc.produto_id = p.id) " +
            "INNER JOIN categoria c ON pc.categoria_id = c.id) " +
            "GROUP BY c.id ORDER BY quantidade DESC",
            nativeQuery = true)
     Integer[][] topSellingCategories();

    @Transactional(readOnly = true)
    @Query(value = "SELECT c.id, sum(i.quantidade) as quantidade FROM ((((item_pedido i " +
            "INNER JOIN produto p ON p.id = i.produto_id) " +
            "INNER JOIN produto_categoria pc ON pc.produto_id = p.id) " +
            "INNER JOIN categoria c ON pc.categoria_id = c.id) " +
            "INNER JOIN pedido pe ON pe.id_pedido = i.pedido_id AND CAST(pe.instante AS DATE) >= :inicio AND CAST(pe.instante AS DATE) <= :fim) " +
            "GROUP BY c.id ORDER BY quantidade DESC",
            nativeQuery = true)
    Integer[][] filterCategories(@Param("inicio")LocalDate initDate, @Param("fim")LocalDate finalDate);
}