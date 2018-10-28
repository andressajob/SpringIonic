package com.victorseger.svtech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.victorseger.svtech.domain.enums.Perfil;
import com.victorseger.svtech.domain.enums.TipoCliente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nome;


    @Column(unique = true)//anotação que torna impossivel ter mais de um cliente com o mesmo email sem mensagem personalizada (a exceção é lançada por exceção do banco)
    private String email;
    private String cpfOuCnpj;
    private Integer tipo;

    @JsonIgnore
    private String senha;

    //liberando a serialização dos endereços
    //@JsonManagedReference - foi recomendada a remoção pois apresentou problemas durante a execução
    //Cascade = ALL - toda operação que modificar um cliente, vai refletir nos endereços (ou seja, remoção de cliente, remove endereços associados tbm)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();


    //dependência de enums
    @ElementCollection
    @CollectionTable(name = "TELEFONE")
    //telefones estão somente aqui por serem classe fraca
    private Set<String> telefones = new HashSet<>();


    @ElementCollection(fetch = FetchType.EAGER) // exige que quando forem buscados os clientes, os perfis sejam chamados junto
    @CollectionTable(name = "PERFIS")
    private Set<Integer> perfis = new HashSet<>();



    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente() {
        addPerfil(Perfil.CLIENTE); //adiciona o perfil de cliente a qualquer usuário do sistema
    }

    public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpfOuCnpj = cpfOuCnpj;
        this.tipo = (tipo == null)? null : tipo.getCod();
        this.senha=senha;
        addPerfil(Perfil.CLIENTE); //adiciona o perfil de cliente a qualquer usuário do sistema
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public TipoCliente getTipo() {
        return TipoCliente.toEnum(tipo);
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo.getCod();
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis(){
        return perfis.stream().map(x->Perfil.toEnum(x)).collect(Collectors.toSet()); // lambda para percorrer o array dos perfis e retorna todos
    }

    public void addPerfil(Perfil perfil) {
        perfis.add(perfil.getCod());
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
