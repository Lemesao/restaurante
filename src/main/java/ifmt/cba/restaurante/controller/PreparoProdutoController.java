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

import ifmt.cba.restaurante.dto.PreparoProdutoDTO;
import ifmt.cba.restaurante.dto.ProdutoDTO;
import ifmt.cba.restaurante.dto.TipoPreparoDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.PreparoProdutoNegocio;

@RestController
@RequestMapping("/preparoProduto")
public class PreparoProdutoController {

    @Autowired
    private PreparoProdutoNegocio preparoProdutoNegocio;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO inserir(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotValidDataException {
        preparoProdutoDTO = preparoProdutoNegocio.inserir(preparoProdutoDTO);
        addLinks(preparoProdutoDTO);
        return preparoProdutoDTO;
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO alterar(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotValidDataException, NotFoundException {
        addLinks(preparoProdutoDTO);
        return preparoProdutoDTO;
    }

    @DeleteMapping("/{codigo}")
public ResponseEntity<Void> excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
    preparoProdutoNegocio.excluir(codigo);
    return ResponseEntity.noContent().build(); // HTTP 204
}


    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorCodigo(@PathVariable int codigo) throws NotFoundException {
        PreparoProdutoDTO preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorCodigo(codigo);
        addLinks(preparoProdutoDTO);
        return preparoProdutoDTO;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorNome(@PathVariable String nome) throws NotFoundException {
        PreparoProdutoDTO preparoProdutoDTO = preparoProdutoNegocio.pesquisaPorNome(nome);
        addLinks(preparoProdutoDTO);
        return preparoProdutoDTO;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarTodos() throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaTodos();
        for (PreparoProdutoDTO preparoProdutoDTO : lista) {
            addLinks(preparoProdutoDTO);
        }
        return lista;
    }

    @PostMapping(value = "/produto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarPorProduto(@RequestBody ProdutoDTO produtoDTO) throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaPorProduto(produtoDTO);
        for (PreparoProdutoDTO preparoProdutoDTO : lista) {
            addLinks(preparoProdutoDTO);
        }
        return lista;
    }

    @PostMapping(value = "/tipoPreparo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarPorTipoPreparo(@RequestBody TipoPreparoDTO tipoPreparoDTO) throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaPorTipoPreparo(tipoPreparoDTO);
        for (PreparoProdutoDTO preparoProdutoDTO : lista) {
            addLinks(preparoProdutoDTO);
        }
        return lista;
    }

    @PostMapping(value = "/produto-tipoPreparo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorProdutoETipoPreparo(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotFoundException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.pesquisaPorProdutoETipoPreparo(
                preparoProdutoDTO.getProduto(),
                preparoProdutoDTO.getTipoPreparo());
        addLinks(dto);
        return dto;
    }

    private void addLinks(PreparoProdutoDTO preparoProdutoDTO) {
        try {
            preparoProdutoDTO.add(linkTo(methodOn(PreparoProdutoController.class).buscarPorCodigo(preparoProdutoDTO.getCodigo()))
                    .withSelfRel().withType("GET"));

            preparoProdutoDTO.add(linkTo(methodOn(PreparoProdutoController.class).alterar(preparoProdutoDTO))
                    .withRel("alterar").withType("PUT"));

            preparoProdutoDTO.add(linkTo(methodOn(PreparoProdutoController.class).excluir(preparoProdutoDTO.getCodigo()))
                    .withRel("excluir").withType("DELETE"));
        } catch (NotFoundException | NotValidDataException ignored) {}
    }
}
