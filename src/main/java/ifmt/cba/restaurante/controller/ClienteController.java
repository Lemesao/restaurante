package ifmt.cba.restaurante.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ifmt.cba.restaurante.dto.ClienteDTO;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.ClienteNegocio;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteNegocio clienteNegocio;

    @PostMapping
    public ResponseEntity<ClienteDTO> inserir(@RequestBody ClienteDTO clienteDTO) throws NotValidDataException {
        ClienteDTO cliente = clienteNegocio.inserir(clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping
    public ResponseEntity<ClienteDTO> alterar(@RequestBody ClienteDTO clienteDTO) throws NotValidDataException, NotFoundException {
        ClienteDTO cliente = clienteNegocio.alterar(clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable int codigo) throws NotValidDataException, NotFoundException {
        clienteNegocio.excluir(codigo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> pesquisaTodos() throws NotFoundException {
        List<ClienteDTO> lista = clienteNegocio.pesquisaTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/nome/{parteNome}")
    public ResponseEntity<ClienteDTO> pesquisaPorNome(@PathVariable String parteNome) throws NotFoundException {
        ClienteDTO cliente = clienteNegocio.pesquisaPorNome(parteNome);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<ClienteDTO> pesquisaCodigo(@PathVariable int codigo) throws NotFoundException {
        ClienteDTO cliente = clienteNegocio.pesquisaCodigo(codigo);
        return ResponseEntity.ok(cliente);
    }
}

