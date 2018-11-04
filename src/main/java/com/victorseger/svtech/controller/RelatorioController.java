package com.victorseger.svtech.controller;

import com.victorseger.svtech.domain.Filter;
import com.victorseger.svtech.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping(value = "/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    private Filter filter = new Filter();

    @GetMapping("/lista")
    public ModelAndView listReports(Model model) {
        model.addAttribute("products", relatorioService.transformProductMatrix(relatorioService.topSellingProducts()));
        model.addAttribute("orders", relatorioService.topOrders());
        model.addAttribute("categories", relatorioService.transformCategoryMatrix(relatorioService.topSellingCategories()));
        return new ModelAndView("report/list");
    }

    @GetMapping("/filtros")
    public ModelAndView formFilterReports(Model model) {
        String[] objects = {"Produto", "Pedido", "Categoria"};
        model.addAttribute("objects", objects);
        model.addAttribute("filter", filter);
        return new ModelAndView("report/filter");
    }

    @PostMapping("/filtrar")
    public ModelAndView filterReports(Filter filter, Model model){
        if(filter == null)
            filter = new Filter();
        String[] objects = {"Produto", "Pedido", "Categoria"};
        model.addAttribute("objects", objects);
        model.addAttribute("filter", filter);
        if ("Pedido".equals(filter.getObject())){
            model.addAttribute("orders", relatorioService.filterOrders(filter.getInitialDate(), filter.getFinalDate()));
        } else if ("Categoria".equals(filter.getObject())){
            model.addAttribute("categories", relatorioService.transformCategoryMatrix(relatorioService.filterCategories(filter.getInitialDate(), filter.getFinalDate())));
        } else if ("Produto".equals(filter.getObject())){
            model.addAttribute("products", relatorioService.transformProductMatrix(relatorioService.filterProducts(filter.getInitialDate(), filter.getFinalDate())));
        }
        return new ModelAndView("report/filter");
    }
}
