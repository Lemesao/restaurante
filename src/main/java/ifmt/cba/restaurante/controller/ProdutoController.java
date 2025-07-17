package ifmt.cba.restaurante.controller;

import ifmt.cba.restaurante.dto.ProdutoDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.ProdutoNegocio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoNegocio produtoNegocio;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO inserir(@RequestBody ProdutoDTO produtoDTO) throws NotValidDataException {
        ProdutoDTO produto = produtoNegocio.inserir(produtoDTO);
        addLinks(produto);
        return produto;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO alterar(@RequestBody ProdutoDTO produtoDTO) throws NotValidDataException, NotFoundException {
        ProdutoDTO produto = produtoNegocio.alterar(produtoDTO);
        addLinks(produto);
        return produto;
    }

    @DeleteMapping(value = "/{codigo}")
    public void excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
        produtoNegocio.excluir(codigo);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProdutoDTO> buscarTodos() throws NotFoundException {
        List<ProdutoDTO> listaprodutodto = produtoNegocio.pesquisaTodos();
        for (ProdutoDTO produto : listaprodutodto) {
            addLinks(produto);
        }
        return listaprodutodto;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO buscarPorCodigo(@PathVariable int codigo) throws NotFoundException {
        ProdutoDTO produto = produtoNegocio.pesquisaCodigo(codigo);
        addLinks(produto);
        return produto;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO buscarPorNome(@PathVariable String nome) throws NotFoundException {
        ProdutoDTO produto = produtoNegocio.pesquisaPorNome(nome);
        addLinks(produto);
        return produto;
    }

    @GetMapping(value = "/estoque-minimo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProdutoDTO> produtosAbaixoEstoqueMinimo() throws NotFoundException {
        List<ProdutoDTO> produtos = produtoNegocio.pesquisaProdutoAbaixoEstoqueMinimo();
        for (ProdutoDTO produto : produtos) {
            addLinks(produto);
        }
        return produtos;
    }

    private void addLinks(ProdutoDTO produtoDTO) {
        try {
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).buscarPorCodigo(produtoDTO.getCodigo()))
                    .withSelfRel().withType("GET"));
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).alterar(produtoDTO))
                    .withRel("alterar").withType("PUT"));
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).excluir(produtoDTO.getCodigo()))
                    .withRel("excluir").withType("DELETE"));
        } catch (Exception ignored) {
        }
    }
}
