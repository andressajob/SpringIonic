package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.repositories.ClienteRepository;
import com.victorseger.cursomc.services.exceptions.DataIntegrityException;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    public Cliente find(Integer id) {
        Optional<Cliente> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Cliente.class.getName()));
    }

    public Cliente update(Cliente cliente) {
        Cliente newCliente = find(cliente.getId());
        //chama o método auxiliar para apenas atualizar os campos desejados do cliente e não remover nenhum valor de outro campo
        updateData(newCliente,cliente);
        return repo.save(newCliente);
    }



    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir cliente que possui pedidos");
        }

    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        //cria as páginas de requisição com parâmetros (num de páginas, quantidade por página, direção - convertido para Direction e campos para ordenação)
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        //findAll com paginação direto do spring data
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.getId(),clienteDTO.getNome(),clienteDTO.getEmail(),null,null);
    }

    private void updateData(Cliente newCliente, Cliente cliente) {
        newCliente.setNome(cliente.getNome());
        newCliente.setEmail(cliente.getEmail());
    }
}