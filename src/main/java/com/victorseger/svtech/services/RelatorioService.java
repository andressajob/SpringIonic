package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Categoria;
import com.victorseger.svtech.domain.Pedido;
import com.victorseger.svtech.domain.Produto;
import com.victorseger.svtech.repositories.ItemPedidoRepository;
import com.victorseger.svtech.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoService produtoService;

    public Integer[][] topSellingProducts() {
        return itemPedidoRepository.topSellingProducts();
    }

    public List<Pedido> topOrders() {
        return repo.ordersByTotalValor();
    }

    public List<Pedido> filterOrders(LocalDate initDate, LocalDate finalDate) {
        return repo.filterOrders(initDate, finalDate);
    }

    public Integer[][] filterProducts(LocalDate initialDate, LocalDate finalDate) {
        return itemPedidoRepository.filterProducts(initialDate, finalDate);
    }

    public Integer[][] topSellingCategories() {
        return itemPedidoRepository.topSellingCategories();
    }

    public Map<Categoria, Integer> transformCategoryMatrix(Integer[][] matrix) {
        Map<Categoria, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            map.put(categoriaService.find(matrix[i][0]), matrix[i][1]);
        }
        return map;
    }

    public Map<Produto, Integer> transformProductMatrix(Integer[][] matrix) {
        Map<Produto, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < matrix.length; i++) {
            map.put(produtoService.find(matrix[i][0]), matrix[i][1]);
        }
        return map;
    }

    public Integer[][] filterCategories(LocalDate initDate, LocalDate finalDate) {
        return itemPedidoRepository.filterCategories(initDate, finalDate);
    }
}
