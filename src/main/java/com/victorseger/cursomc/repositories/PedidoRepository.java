package com.victorseger.cursomc.repositories;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.domain.ItemPedido;
import com.victorseger.cursomc.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Transactional(readOnly = true)
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM PEDIDO ORDER BY valor_total DESC",
            nativeQuery = true)
    List<Pedido> ordersByTotalValor();

}
