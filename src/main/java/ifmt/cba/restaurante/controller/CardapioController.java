package ifmt.cba.restaurante.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

import ifmt.cba.restaurante.dto.CardapioDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.CardapioNegocio;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {

    @Autowired
    private CardapioNegocio cardapioNegocio;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CardapioDTO> buscarTodos() throws NotFoundException, NotValidDataException {
        List<CardapioDTO> lista = cardapioNegocio.pesquisaTodos();

        for (CardapioDTO dto : lista) {
            addHateoasLinksCRUD(dto);
        }

        return lista;
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CardapioDTO buscarPorID(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        CardapioDTO dto = cardapioNegocio.pesquisaCodigo(codigo);
        addHateoasLinksCRUD(dto);
        return dto;
    }

    @GetMapping(value = "/nome/{nome}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CardapioDTO buscarPorNome(@PathVariable("nome") String nome) throws NotFoundException, NotValidDataException {
        CardapioDTO dto = cardapioNegocio.pesquisaPorNome(nome);
        addHateoasLinksCRUD(dto);
        return dto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CardapioDTO inserirCardapio(@RequestBody CardapioDTO cardapioDTO) throws NotFoundException, NotValidDataException {
        CardapioDTO dto = cardapioNegocio.inserir(cardapioDTO);
        addHateoasLinksCRUD(dto);
        return dto;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CardapioDTO alterarCardapio(@RequestBody CardapioDTO cardapioDTO) throws NotFoundException, NotValidDataException {
        CardapioDTO dto = cardapioNegocio.alterar(cardapioDTO);
        addHateoasLinksCRUD(dto);
        return dto;
    }

    @DeleteMapping(value = "/{codigo}")
    public ResponseEntity<?> excluirCardapio(@PathVariable("codigo") int codigo) throws NotFoundException, NotValidDataException {
        cardapioNegocio.excluir(codigo);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinksCRUD(CardapioDTO dto) throws NotFoundException, NotValidDataException {
        dto.add(linkTo(methodOn(CardapioController.class).buscarPorID(dto.getCodigo())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(CardapioController.class).buscarPorNome(dto.getNome())).withRel("buscarPorNome").withType("GET"));
        dto.add(linkTo(methodOn(CardapioController.class).inserirCardapio(dto)).withRel("inserirCardapio").withType("POST"));
        dto.add(linkTo(methodOn(CardapioController.class).alterarCardapio(dto)).withRel("alterarCardapio").withType("PUT"));
        dto.add(linkTo(methodOn(CardapioController.class).excluirCardapio(dto.getCodigo())).withRel("excluirCardapio").withType("DELETE"));
    }
}
