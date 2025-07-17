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

import ifmt.cba.restaurante.dto.TipoPreparoDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.TipoPreparoNegocio;

@RestController
@RequestMapping("/tipopreparo")
public class TipoPreparoController {

    @Autowired
    private TipoPreparoNegocio tipoPreparoNegocio;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipoPreparoDTO>> buscarTodos() throws NotFoundException, NotValidDataException {
        List<TipoPreparoDTO> lista = tipoPreparoNegocio.pesquisaTodos();
        for (TipoPreparoDTO tipopreparoDTO : lista) {
            addHateoasLinksCRUD(tipopreparoDTO);
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoPreparoDTO> buscarPorCodigo(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        TipoPreparoDTO tipopreparoDTO = tipoPreparoNegocio.pesquisaCodigo(codigo);
        addHateoasLinksCRUD(tipopreparoDTO);
        return ResponseEntity.ok(tipopreparoDTO);
    }

    @GetMapping(value = "/descricao/{descricao}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoPreparoDTO> buscarPorDescricao(@PathVariable("descricao") String descricao) throws NotFoundException, NotValidDataException {
        TipoPreparoDTO tipopreparoDTO = tipoPreparoNegocio.pesquisaPorDescricao(descricao);
        addHateoasLinksCRUD(tipopreparoDTO);
        return ResponseEntity.ok(tipopreparoDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoPreparoDTO> inserirTipoPreparo(@RequestBody TipoPreparoDTO tipopreparoDTO)
            throws NotValidDataException, NotFoundException {
        TipoPreparoDTO salvo = tipoPreparoNegocio.inserir(tipopreparoDTO);
        addHateoasLinksCRUD(salvo);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipoPreparoDTO> alterarTipoPreparo(@RequestBody TipoPreparoDTO tipopreparoDTO)
            throws NotFoundException, NotValidDataException {
        TipoPreparoDTO atualizado = tipoPreparoNegocio.alterar(tipopreparoDTO);
        addHateoasLinksCRUD(atualizado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping(value = "/{codigo}")
    public ResponseEntity<Void> excluirTipoPreparo(@PathVariable("codigo") int codigo)
            throws NotFoundException, NotValidDataException {
        tipoPreparoNegocio.excluir(codigo);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinksCRUD(TipoPreparoDTO tipopreparoDTO) throws NotFoundException, NotValidDataException {
        tipopreparoDTO.add(linkTo(methodOn(TipoPreparoController.class).buscarPorCodigo(tipopreparoDTO.getCodigo())).withSelfRel().withType("GET"));
        tipopreparoDTO.add(linkTo(methodOn(TipoPreparoController.class).buscarPorDescricao(tipopreparoDTO.getDescricao())).withRel("buscarPorDescricao").withType("GET"));
        tipopreparoDTO.add(linkTo(methodOn(TipoPreparoController.class).inserirTipoPreparo(tipopreparoDTO)).withRel("inserirTipoPreparo").withType("POST"));
        tipopreparoDTO.add(linkTo(methodOn(TipoPreparoController.class).alterarTipoPreparo(tipopreparoDTO)).withRel("alterarTipoPreparo").withType("PUT"));
        tipopreparoDTO.add(linkTo(methodOn(TipoPreparoController.class).excluirTipoPreparo(tipopreparoDTO.getCodigo())).withRel("excluirTipoPreparo").withType("DELETE"));
    }
}
