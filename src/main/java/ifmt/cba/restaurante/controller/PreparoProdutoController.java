package ifmt.cba.restaurante.controller;

import ifmt.cba.restaurante.dto.PreparoProdutoDTO;
import ifmt.cba.restaurante.dto.ProdutoDTO;
import ifmt.cba.restaurante.dto.TipoPreparoDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.PreparoProdutoNegocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/preparoProduto")
public class PreparoProdutoController {

    @Autowired
    private PreparoProdutoNegocio preparoProdutoNegocio;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO inserir(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotValidDataException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.inserir(preparoProdutoDTO);
        addLinks(dto);
        return dto;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO alterar(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotValidDataException, NotFoundException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.alterar(preparoProdutoDTO);
        addLinks(dto);
        return dto;
    }

    @DeleteMapping(value = "/{codigo}")
    public void excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
        preparoProdutoNegocio.excluir(codigo);
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorCodigo(@PathVariable int codigo) throws NotFoundException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.pesquisaPorCodigo(codigo);
        addLinks(dto);
        return dto;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorNome(@PathVariable String nome) throws NotFoundException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.pesquisaPorNome(nome);
        addLinks(dto);
        return dto;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarTodos() throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaTodos();
        for (PreparoProdutoDTO dto : lista) {
            addLinks(dto);
        }
        return lista;
    }

    @PostMapping(value = "/produto", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarPorProduto(@RequestBody ProdutoDTO produtoDTO) throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaPorProduto(produtoDTO);
        for (PreparoProdutoDTO dto : lista) {
            addLinks(dto);
        }
        return lista;
    }

    @PostMapping(value = "/tipoPreparo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PreparoProdutoDTO> buscarPorTipoPreparo(@RequestBody TipoPreparoDTO tipoPreparoDTO) throws NotFoundException {
        List<PreparoProdutoDTO> lista = preparoProdutoNegocio.pesquisaPorTipoPreparo(tipoPreparoDTO);
        for (PreparoProdutoDTO dto : lista) {
            addLinks(dto);
        }
        return lista;
    }

    @PostMapping(value = "/produto-tipoPreparo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PreparoProdutoDTO buscarPorProdutoETipoPreparo(@RequestBody PreparoProdutoDTO preparoProdutoDTO) throws NotFoundException {
        PreparoProdutoDTO dto = preparoProdutoNegocio.pesquisaPorProdutoETipoPreparo(
                preparoProdutoDTO.getProdutoDTO(),
                preparoProdutoDTO.getTipoPreparoDTO());
        addLinks(dto);
        return dto;
    }

    private void addLinks(PreparoProdutoDTO dto) {
        try {
            dto.add(linkTo(methodOn(PreparoProdutoController.class).buscarPorCodigo(dto.getCodigo()))
                    .withSelfRel().withType("GET"));

            dto.add(linkTo(methodOn(PreparoProdutoController.class).alterar(dto))
                    .withRel("alterar").withType("PUT"));

            dto.add(linkTo(methodOn(PreparoProdutoController.class).excluir(dto.getCodigo()))
                    .withRel("excluir").withType("DELETE"));
        } catch (Exception ignored) {}
    }
}
