package ifmt.cba.restaurante.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ifmt.cba.restaurante.dto.MovimentoEstoqueDTO;
import ifmt.cba.restaurante.dto.RegistroEstoqueDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.RegistroEstoqueNegocio;

@RestController
@RequestMapping("/registroEstoque")
public class RegistroEstoqueController {

    @Autowired
    private RegistroEstoqueNegocio registroEstoqueNegocio;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegistroEstoqueDTO> buscarTodosPorMovimento(@RequestParam("movimento") MovimentoEstoqueDTO movimento) throws NotFoundException, NotValidDataException {
        List<RegistroEstoqueDTO> listaregistroestoqueDTO = registroEstoqueNegocio.buscarPorMovimento(movimento);

        for (RegistroEstoqueDTO registroEstoqueDTO : listaregistroestoqueDTO) {
            addHateoasLinksCRUD(registroEstoqueDTO);
        }

        return listaregistroestoqueDTO;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RegistroEstoqueDTO buscarPorCodigo(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        RegistroEstoqueDTO registroEstoqueDTO = registroEstoqueNegocio.pesquisaCodigo(codigo);
        addHateoasLinksCRUD(registroEstoqueDTO);
        return registroEstoqueDTO;
    }

    @GetMapping(value = "/movimento/{movimento}/data/{data}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RegistroEstoqueDTO> buscarPorMovimentoEData(
            @PathVariable("movimento") MovimentoEstoqueDTO movimento,
            @PathVariable("data") String dataStr) throws NotFoundException, NotValidDataException {

        LocalDate data = LocalDate.parse(dataStr);
        List<RegistroEstoqueDTO> listaregistroestoqueDTO = registroEstoqueNegocio.buscarPorMovimentoEData(movimento, data);

        for (RegistroEstoqueDTO registroEstoquedto : listaregistroestoqueDTO) {
            addHateoasLinksCRUD(registroEstoquedto);
        }

        return listaregistroestoqueDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RegistroEstoqueDTO inserir(@RequestBody RegistroEstoqueDTO registroEstoqueDTO) throws NotValidDataException, NotFoundException {
        RegistroEstoqueDTO registroEstoquedto = registroEstoqueNegocio.inserir(registroEstoqueDTO);
        addHateoasLinksCRUD(registroEstoquedto);
        return registroEstoquedto;
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RegistroEstoqueDTO excluir(@RequestBody RegistroEstoqueDTO registroEstoqueDTO) throws NotValidDataException, NotFoundException {
        RegistroEstoqueDTO registroEstoquedto = registroEstoqueNegocio.excluir(registroEstoqueDTO);
        addHateoasLinksCRUD(registroEstoquedto);
        return registroEstoquedto;
    }

    private void addHateoasLinksCRUD(RegistroEstoqueDTO registroEstoqueDTO) throws NotFoundException, NotValidDataException {
        registroEstoqueDTO.add(linkTo(methodOn(RegistroEstoqueController.class).buscarPorCodigo(registroEstoqueDTO.getCodigo()))
                .withSelfRel().withType("GET"));

        registroEstoqueDTO.add(linkTo(methodOn(RegistroEstoqueController.class).inserir(registroEstoqueDTO))
                .withRel("inserir").withType("POST"));

        registroEstoqueDTO.add(linkTo(methodOn(RegistroEstoqueController.class).excluir(registroEstoqueDTO))
                .withRel("excluir").withType("DELETE"));
    }
}
