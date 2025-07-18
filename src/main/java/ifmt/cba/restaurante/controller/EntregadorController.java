package ifmt.cba.restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifmt.cba.restaurante.dto.EntregadorDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.EntregadorNegocio;

@RestController
@RequestMapping("/entregador")
public class EntregadorController {

    @Autowired
    private EntregadorNegocio entregadorNegocio;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EntregadorDTO> buscarTodos() throws NotFoundException, NotValidDataException {
        List<EntregadorDTO> listaEntregadorTempDTO = entregadorNegocio.pesquisaTodos();

        for (EntregadorDTO entregadorDTO : listaEntregadorTempDTO) {
            addHateoasLinksCRUD(entregadorDTO);
        }
        return listaEntregadorTempDTO;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntregadorDTO buscarPorCodigo(@PathVariable("codigo") int codigo) throws NotFoundException {
        EntregadorDTO entregadorTempDTO = entregadorNegocio.pesquisaCodigo(codigo);

        addHateoasLinksCRUD(entregadorTempDTO);
        return entregadorTempDTO;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntregadorDTO buscarPorNome(@PathVariable("nome") String nome) throws NotFoundException {
        EntregadorDTO entregadorDTO = entregadorNegocio.pesquisaPorNome(nome);
        addHateoasLinksCRUD(entregadorDTO);
        return entregadorDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntregadorDTO inserir(@RequestBody EntregadorDTO entregadorDTO) throws NotFoundException, NotValidDataException {
        EntregadorDTO entregadorDTO2 = entregadorNegocio.inserir(entregadorDTO);
        addHateoasLinksCRUD(entregadorDTO2);
        return entregadorDTO2;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EntregadorDTO alterar(@RequestBody EntregadorDTO entregadorDTO) throws NotFoundException, NotValidDataException {
        EntregadorDTO entregadorDTO3 = entregadorNegocio.alterar(entregadorDTO);
        addHateoasLinksCRUD(entregadorDTO3);
        return entregadorDTO3;
    }

    @DeleteMapping(value = "/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        entregadorNegocio.excluir(codigo);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinksCRUD(EntregadorDTO dto) {
        try {
            dto.add(linkTo(methodOn(EntregadorController.class).buscarPorCodigo(dto.getCodigo())).withSelfRel().withType("GET"));
            dto.add(linkTo(methodOn(EntregadorController.class).buscarPorNome(dto.getNome())).withRel("buscarPorNome").withType("GET"));
            dto.add(linkTo(methodOn(EntregadorController.class).inserir(dto)).withRel("inserir").withType("POST"));
            dto.add(linkTo(methodOn(EntregadorController.class).alterar(dto)).withRel("alterar").withType("PUT"));
            dto.add(linkTo(methodOn(EntregadorController.class).excluir(dto.getCodigo())).withRel("excluir").withType("DELETE"));
        } catch (NotFoundException | NotValidDataException e) {
        }
    }
}
