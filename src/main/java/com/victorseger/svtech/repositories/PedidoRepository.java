package com.victorseger.svtech.repositories;

import com.victorseger.svtech.domain.Cliente;
import com.victorseger.svtech.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Transactional(readOnly = true)
    Page<Pedido> findByCliente(Cliente cliente, Pageable pageable);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM PEDIDO ORDER BY valor_total DESC",
            nativeQuery = true)
    List<Pedido> ordersByTotalValor();

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM PEDIDO WHERE CAST(instante AS DATE) >= :inicio AND CAST(instante AS DATE) <= :fim ORDER BY valor_total DESC",
            nativeQuery = true)
    List<Pedido> filterOrders(@Param("inicio")LocalDate initialDate, @Param("fim")LocalDate finalDate);

}
