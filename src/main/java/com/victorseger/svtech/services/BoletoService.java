package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService {

    public void preencherPagamentoComBoleto(PagamentoComBoleto pagamentoComBoleto, Date instante) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(instante);
        cal.add(Calendar.DAY_OF_MONTH,7);
        pagamentoComBoleto.setDataVencimento(cal.getTime());
    }


}
