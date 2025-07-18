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

import ifmt.cba.restaurante.dto.ProdutoDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.ProdutoNegocio;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoNegocio produtoNegocio;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO inserir(@RequestBody ProdutoDTO produtoDTO) throws NotValidDataException, NotFoundException {
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

 @DeleteMapping("/{codigo}")
public ResponseEntity<Void> excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
    produtoNegocio.excluir(codigo);
    return ResponseEntity.noContent().build(); // HTTP 204 No Content
}

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProdutoDTO> buscarTodos() throws NotFoundException, NotValidDataException {
        List<ProdutoDTO> lista = produtoNegocio.pesquisaTodos();
        for (ProdutoDTO dto : lista) {
            addLinks(dto);
        }
        return lista;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO buscarPorCodigo(@PathVariable int codigo) throws NotFoundException, NotValidDataException {
        ProdutoDTO produto = produtoNegocio.pesquisaCodigo(codigo);
        addLinks(produto);
        return produto;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProdutoDTO buscarPorNome(@PathVariable String nome) throws NotFoundException, NotValidDataException {
        ProdutoDTO produto = produtoNegocio.pesquisaPorNome(nome);
        addLinks(produto);
        return produto;
    }

    @GetMapping(value = "/estoque-minimo", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProdutoDTO> produtosAbaixoEstoqueMinimo() throws NotFoundException, NotValidDataException {
        List<ProdutoDTO> lista = produtoNegocio.pesquisaProdutoAbaixoEstoqueMinimo();
        for (ProdutoDTO dto : lista) {
            addLinks(dto);
        }
        return lista;
    }

    private void addLinks(ProdutoDTO produtoDTO) throws NotValidDataException, NotFoundException {
        
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).buscarPorCodigo(produtoDTO.getCodigo()))
                    .withSelfRel().withType("GET"));
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).alterar(produtoDTO))
                    .withRel("alterar").withType("PUT"));
            produtoDTO.add(linkTo(methodOn(ProdutoController.class).excluir(produtoDTO.getCodigo()))
                    .withRel("excluir").withType("DELETE"));
        }
}

