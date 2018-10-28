package com.victorseger.svtech.repositories;

import com.victorseger.svtech.domain.ItemPedido;
import com.victorseger.svtech.domain.Pedido;
import com.victorseger.svtech.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
    ItemPedido findById_PedidoAndId_Produto(Pedido pedido, Produto produto);

    @Transactional(readOnly = true)
    @Query(value = "SELECT P.id, sum(I.quantidade) as quantidade FROM ITEM_PEDIDO I INNER JOIN PRODUTO P ON P.id = I.produto_id GROUP BY P.id ORDER BY quantidade DESC ",
            nativeQuery = true)
    Integer[][] topSellingProducts();

    @Transactional(readOnly = true)
    @Query(value = "SELECT P.id, sum(I.quantidade) as quantidade FROM ITEM_PEDIDO I INNER JOIN PRODUTO P ON P.id = I.produto_id INNER JOIN PEDIDO PE ON PE.id_pedido = I.pedido_id " +
            "AND CAST(PE.instante AS DATE) >= :inicio AND CAST(PE.instante AS DATE) <= :fim GROUP BY P.id ORDER BY quantidade DESC",
            nativeQuery = true)
    Integer[][] filterProducts(@Param("inicio")LocalDate initialDate, @Param("fim")LocalDate finalDate);

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