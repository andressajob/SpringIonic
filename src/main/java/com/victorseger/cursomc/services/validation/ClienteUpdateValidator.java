package com.victorseger.cursomc.services.validation;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.repositories.ClienteRepository;
import com.victorseger.cursomc.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ClienteRepository repository;

    @Override
    public void initialize(ClienteUpdate ann) {
    }


    @Override
    public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

        //obtendo o ID do objeto que vem da URI de requisição
        @SuppressWarnings("unchecked")//removendo o warning de problema da linha abaixo
        Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Integer uriId = Integer.parseInt(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        Cliente cliente = repository.findByEmail(objDto.getEmail());

        if(cliente!= null && !cliente.getId().equals(uriId)) {
            list.add(new FieldMessage("email", "Email já existente no sistema!"));
        }

        // inclua os testes aqui, inserindo erros na lista

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
