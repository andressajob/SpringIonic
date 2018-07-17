package com.victorseger.cursomc.services;

import com.victorseger.cursomc.domain.Cidade;
import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.domain.Endereco;
import com.victorseger.cursomc.domain.enums.Perfil;
import com.victorseger.cursomc.domain.enums.TipoCliente;
import com.victorseger.cursomc.dto.ClienteDTO;
import com.victorseger.cursomc.dto.ClienteNewDTO;
import com.victorseger.cursomc.repositories.ClienteRepository;
import com.victorseger.cursomc.repositories.EnderecoRepository;
import com.victorseger.cursomc.security.UserSS;
import com.victorseger.cursomc.services.exceptions.AuthorizationException;
import com.victorseger.cursomc.services.exceptions.DataIntegrityException;
import com.victorseger.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@Service
public class ClienteService{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

/*    @Autowired
    private S3Service s3Service;*/

/*
    @Autowired
    private ImageService imageService;
*/

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private int size;

    public Cliente find(Integer id) {

        UserSS userSS = UserService.authenticated();

        if(userSS == null || !userSS.hasRole(Perfil.ADMIN) && !id.equals(userSS.getId())) {
            throw new AuthorizationException("Acesso Negado!");
        }

        Optional<Cliente> obj = repo.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + ", Tipo: " + Cliente.class.getName()));
    }

    public Cliente insert(Cliente cliente) {
        cliente.setId(null);
        cliente = repo.save(cliente);
        enderecoRepository.saveAll(cliente.getEnderecos());
        return cliente;
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
            //capturando a exceção de integridade de dados violada e adicionando mensagem personalizada
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
        return new Cliente(clienteDTO.getId(),clienteDTO.getNome(),clienteDTO.getEmail(),null,null, null);
    }

    public Cliente fromDTO(ClienteNewDTO clienteDTO) {
        Cliente cliente = new Cliente(null,clienteDTO.getNome(),clienteDTO.getEmail(),clienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteDTO.getTipo()),bCryptPasswordEncoder.encode(clienteDTO.getSenha())); // codificando a senha
        Cidade cidade = new Cidade(clienteDTO.getCidadeId(),null,null);
        Endereco end = new Endereco(null,clienteDTO.getLogradouro(),clienteDTO.getNumero(),clienteDTO.getComplemento(),clienteDTO.getBairro(),clienteDTO.getCep(),cliente, cidade);
        cliente.getEnderecos().add(end);
        cliente.getTelefones().add(clienteDTO.getTelefone1());
        if(clienteDTO.getTelefone2()!=null) {
            cliente.getTelefones().add(clienteDTO.getTelefone2());
        }
        if(clienteDTO.getTelefone3()!=null) {
            cliente.getTelefones().add(clienteDTO.getTelefone3());
        }
        return cliente;
    }

    private void updateData(Cliente newCliente, Cliente cliente) {
        newCliente.setNome(cliente.getNome());
        newCliente.setEmail(cliente.getEmail());
    }

   /* public URI uploadProfilePicture(MultipartFile multipartFile) {
        UserSS userSS = UserService.authenticated();
        if(userSS == null) {
            throw new AuthorizationException("Acesso negado");
        }

        BufferedImage imageJpg = imageService.getJpgImageFromFile(multipartFile);
        imageJpg = imageService.cropImage(imageJpg);
        imageJpg = imageService.resize(imageJpg,size);

        String filename = prefix + userSS.getId() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(imageJpg,"jpg"), filename, "image");

    }*/

    //método de busca de cliente por e-mail
    public Cliente findByEmail(String email) {
        UserSS userss = UserService.authenticated();
        if(userss == null || !userss.hasRole(Perfil.ADMIN) && !email.equals(userss.getUsername())) {
            throw new AuthorizationException("Acesso negado");
        }

        Cliente cliente = repo.findByEmail(email);
        if (cliente == null) {
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + userss.getId() + ", Tipo: " + Cliente.class.getName());
        }
        return cliente;
    }


}