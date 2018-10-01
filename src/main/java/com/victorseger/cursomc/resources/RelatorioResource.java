package com.victorseger.cursomc.resources;

import com.victorseger.cursomc.domain.Filter;
import com.victorseger.cursomc.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/relatorios")
public class RelatorioResource {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/lista")
    public ModelAndView listReports(Model model) {
        model.addAttribute("products", pedidoService.topSellingProducts());
        model.addAttribute("orders", pedidoService.topOrders());
        return new ModelAndView("/report/list");
    }

    @GetMapping("/filtros")
    public ModelAndView formFilterReports(Model model) {
        String[] objects = {"Produto", "Pedido", "Categoria"};
        model.addAttribute("objects", objects);
        model.addAttribute("filter", new Filter());
        return new ModelAndView("/report/filter");
    }

    @PostMapping("/filtrar")
    public void filterReports(Filter filter){
        System.out.println(filter.toString());
        System.out.println(filter.getFinalDate().compareTo(filter.getInitialDate()));
    }

}
