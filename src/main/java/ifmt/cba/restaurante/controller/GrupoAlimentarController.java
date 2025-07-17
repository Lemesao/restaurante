package ifmt.cba.restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ifmt.cba.restaurante.dto.GrupoAlimentarDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.GrupoAlimentarNegocio;

@RestController
@RequestMapping("/api/grupo-alimentar")
public class GrupoAlimentarController {

    @Autowired
    private GrupoAlimentarNegocio grupoAlimentarNegocio;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GrupoAlimentarDTO> inserir(@RequestBody GrupoAlimentarDTO grupoAlimentarDTO) throws NotValidDataException {
        GrupoAlimentarDTO grupoInserido = grupoAlimentarNegocio.inserir(grupoAlimentarDTO);
        return adicionarLinks(grupoInserido);
    }

    @PutMapping
    public EntityModel<GrupoAlimentarDTO> alterar(@RequestBody GrupoAlimentarDTO grupoAlimentarDTO) throws NotValidDataException, NotFoundException {
        GrupoAlimentarDTO grupoAlterado = grupoAlimentarNegocio.alterar(grupoAlimentarDTO);
        return adicionarLinks(grupoAlterado);
    }

    @DeleteMapping("/{codigo}")
    public void excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
        grupoAlimentarNegocio.excluir(codigo);
    }

    @GetMapping
    public CollectionModel<EntityModel<GrupoAlimentarDTO>> listarTodos() throws NotFoundException {
        List<GrupoAlimentarDTO> lista = grupoAlimentarNegocio.pesquisaTodos();
        List<EntityModel<GrupoAlimentarDTO>> listaComLinks = lista.stream()
            .map(this::adicionarLinks)
            .toList();
        return CollectionModel.of(listaComLinks);
    }

    @GetMapping("/nome/{parteNome}")
    public EntityModel<GrupoAlimentarDTO> pesquisarPorNome(@PathVariable String parteNome) throws NotFoundException {
        GrupoAlimentarDTO grupo = grupoAlimentarNegocio.pesquisaPorNome(parteNome);
        return adicionarLinks(grupo);
    }

    @GetMapping("/{codigo}")
    public EntityModel<GrupoAlimentarDTO> pesquisarPorCodigo(@PathVariable int codigo) throws NotFoundException {
        GrupoAlimentarDTO grupo = grupoAlimentarNegocio.pesquisaCodigo(codigo);
        return adicionarLinks(grupo);
    }

    private EntityModel<GrupoAlimentarDTO> adicionarLinks(GrupoAlimentarDTO grupo) {
        return EntityModel.of(grupo,
            Link.of("/api/grupo-alimentar/" + grupo.getCodigo()).withSelfRel(),
            Link.of("/api/grupo-alimentar").withRel("todos"),
            Link.of("/api/grupo-alimentar/nome/" + grupo.getNome()).withRel("buscar-por-nome")
        );
    }
}
