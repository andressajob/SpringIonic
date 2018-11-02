package com.victorseger.svtech.services;

import com.victorseger.svtech.domain.Cliente;
import com.victorseger.svtech.domain.Endereco;
import com.victorseger.svtech.repositories.ClienteRepository;
import com.victorseger.svtech.repositories.EnderecoRepository;
import com.victorseger.svtech.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ClienteService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private int size;

    public Cliente find(Integer id) {
        if (id != null) {
            Optional<Cliente> obj = repo.findById(id);

            return obj.orElseThrow(() -> new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + ", Tipo: " + Cliente.class.getName()));
        }
        return null;
    }

    public Cliente insert(Cliente cliente) {
        if (cliente != null) {
            cliente.setId(null);
            cliente = repo.save(cliente);
            enderecoRepository.saveAll(cliente.getEnderecos());
        }
        return cliente;
    }

    public Cliente update(Cliente cliente) {
        if (cliente != null) {
            Cliente newCliente = find(cliente.getId());
            //chama o método auxiliar para apenas atualizar os campos desejados do cliente e não remover nenhum valor de outro campo
            updateData(newCliente, cliente);
            return repo.save(newCliente);
        }
        return null;
    }

    public boolean delete(Integer id) {
        if (id != null) {
            boolean flag = repo.existsById(id);
            try {
                if (flag)
                    repo.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                flag = false;
            }
            return flag;
        }
        return false;
    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    private void updateData(Cliente newCliente, Cliente cliente) {
        newCliente.setNome(cliente.getNome());
        newCliente.setEmail(cliente.getEmail());
        newCliente.setCpfOuCnpj(cliente.getCpfOuCnpj());
        newCliente.setTelefones(cliente.getTelefones());
        newCliente.setTipo(cliente.getTipo());
    }

    public void insertAddress(Endereco endereco) {
        if (endereco != null) {
            endereco.setId(null);
            Cliente newCliente = repo.getOne(endereco.getCliente().getId());
            endereco.setCliente(newCliente);
            endereco = enderecoRepository.save(endereco);
            newCliente.getEnderecos().add(endereco);
            repo.save(newCliente);
        }
    }

    public void updateAddress(Endereco endereco) {
        enderecoRepository.save(endereco);
    }

    public Endereco addressById(Integer id) {
        if (id != null && enderecoRepository.existsById(id))
            return enderecoRepository.getOne(id);
        return null;
    }

    public List<Endereco> findAllAddressByClientId(Integer id) {
        if (id != null) {
            return enderecoRepository.findAllByClienteId(id);
        }
        return null;
    }

    public boolean deleteAddress(Integer id) {
        if(id!=null){
            boolean flag = enderecoRepository.existsById(id);
            try {
                if (flag)
                    enderecoRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                flag = false;
            }
            return flag;
        }
        return false;
    }


}