package ifmt.cba.restaurante.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import ifmt.cba.restaurante.dto.ColaboradorDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.ColaboradorNegocio;

@RestController
@RequestMapping("/colaborador")
public class ColaboradorController {

    @Autowired
    private ColaboradorNegocio colaboradorNegocio;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColaboradorDTO> buscarTodos() throws NotFoundException, NotValidDataException {
        List<ColaboradorDTO> listaColaboradorTempDTO = colaboradorNegocio.pesquisaTodos();

        for (ColaboradorDTO colaboradordto : listaColaboradorTempDTO) {
            addHateoasLinksCRUD(colaboradordto);
        }

        return listaColaboradorTempDTO;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ColaboradorDTO buscarPorCodigo(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        ColaboradorDTO colaboradorTempDTO = colaboradorNegocio.pesquisaCodigo(codigo);
        addHateoasLinksCRUD(colaboradorTempDTO);
        return colaboradorTempDTO;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ColaboradorDTO buscarPorNome(@PathVariable("nome") String nome) throws NotFoundException, NotValidDataException {
        ColaboradorDTO colaboradorTempDTO = colaboradorNegocio.pesquisaParteNome(nome);
        addHateoasLinksCRUD(colaboradorTempDTO);
        return colaboradorTempDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ColaboradorDTO inserirColaborador(@RequestBody ColaboradorDTO colaboradorDTO) throws NotFoundException, NotValidDataException {
        ColaboradorDTO colaboradorTempDTO = colaboradorNegocio.inserir(colaboradorDTO);

        addHateoasLinksCRUD(colaboradorTempDTO);
        return colaboradorTempDTO;

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ColaboradorDTO alterarColaborador(@RequestBody ColaboradorDTO colaboradorDTO) throws NotFoundException, NotValidDataException {
        ColaboradorDTO colaboradorTempDTO = colaboradorNegocio.alterar(colaboradorDTO);

        addHateoasLinksCRUD(colaboradorTempDTO);
        return colaboradorTempDTO;
        
    }

    @DeleteMapping(value = "/{codigo}")
    public ResponseEntity<?> excluir(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        colaboradorNegocio.excluir(codigo);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinksCRUD(ColaboradorDTO colaboradorDTO) throws NotFoundException, NotValidDataException {
        colaboradorDTO.add(linkTo(methodOn(ColaboradorController.class).buscarPorCodigo(colaboradorDTO.getCodigo())).withSelfRel().withType("GET"));
        colaboradorDTO.add(linkTo(methodOn(ColaboradorController.class).buscarPorNome(colaboradorDTO.getNome())).withRel("buscarPorNome").withType("GET"));
        colaboradorDTO.add(linkTo(methodOn(ColaboradorController.class).inserirColaborador(colaboradorDTO)).withRel("inserir").withType("POST"));
        colaboradorDTO.add(linkTo(methodOn(ColaboradorController.class).alterarColaborador(colaboradorDTO)).withRel("alterar").withType("PUT"));
        colaboradorDTO.add(linkTo(methodOn(ColaboradorController.class).excluir(colaboradorDTO.getCodigo())).withRel("excluir").withType("DELETE"));
    }
}
