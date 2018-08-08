package com.victorseger.cursomc.resources;

import com.victorseger.cursomc.domain.Categoria;
import com.victorseger.cursomc.domain.Cliente;
import com.victorseger.cursomc.dto.CategoriaDTO;
import com.victorseger.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;
    private boolean error = false;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Categoria> find(@PathVariable Integer id) {
        Categoria obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoria = service.fromDTO(categoriaDTO);
        categoria = service.insert(categoria);

        //pega a URI do novo recurso inserido e adiciona ao final do "current request"
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();

        return ResponseEntity.created(uri).build();

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id) {
        Categoria categoria = service.fromDTO(categoriaDTO);
        categoria.setId(id);
        categoria = service.update(categoria);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoriaDTO>> findAll() {
        List<Categoria> categoriaList = service.findAll();
        //convertendo lista de categorias para lista de DTO (com os dados selecionados para exibir)
        List<CategoriaDTO> categoriaDTO = categoriaList.stream().map(cat -> new CategoriaDTO(cat)).collect(Collectors.toList());
        return ResponseEntity.ok().body(categoriaDTO);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Page<CategoriaDTO>> findPage(
            //anotação que torna os valores opcionais e não requisitos para a função
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        Page<Categoria> categoriaPage = service.findPage(page, linesPerPage, orderBy, direction);
        Page<CategoriaDTO> categoriaDTO = categoriaPage.map(CategoriaDTO::new);
        return ResponseEntity.ok().body(categoriaDTO);
    }

    @GetMapping("/lista")
    public ModelAndView listCategories(Model model) {
        model.addAttribute("categories", service.findAll());
        model.addAttribute("error", error);
        error = false;
        return new ModelAndView("/product/category/list");
    }

    @GetMapping("/novo")
    public ModelAndView newCategory(Model model) {
        model.addAttribute("category", new Categoria());
        model.addAttribute("action", "new");
        return new ModelAndView("/product/category/form");
    }

    @PostMapping("/salvar")
    public ModelAndView saveCategory(Categoria categoria) {
        if (categoria.getId() != null) service.update(categoria);
        else service.insert(categoria);
        return new ModelAndView("redirect:/categorias/lista");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editCategory(Model model, @PathVariable int id) {
        model.addAttribute("category", service.find(id));
        model.addAttribute("action", "edit");
        return new ModelAndView("/product/category/form");
    }

    @GetMapping("/excluir/{id}")
    public ModelAndView deleteCategory(@PathVariable int id) {
        if (!service.delete(id)) error = true;
        return new ModelAndView("redirect:/categorias/lista");
    }


}
