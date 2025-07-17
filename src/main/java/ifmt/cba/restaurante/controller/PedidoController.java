package ifmt.cba.restaurante.controller;

import ifmt.cba.restaurante.dto.*;
import ifmt.cba.restaurante.exception.NotFoundException;
import ifmt.cba.restaurante.exception.NotValidDataException;
import ifmt.cba.restaurante.negocio.PedidoNegocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoNegocio pedidoNegocio;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PedidoDTO inserir(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException {
        return pedidoNegocio.inserir(pedidoDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PedidoDTO alterar(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException, NotFoundException {
        return pedidoNegocio.alterar(pedidoDTO);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void excluir(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException, NotFoundException {
        pedidoNegocio.excluir(pedidoDTO);
    }

    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PedidoDTO buscarPorCodigo(@PathVariable int codigo) throws NotFoundException {
        return pedidoNegocio.pesquisaCodigo(codigo);
    }

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PedidoDTO> buscarPorData(
        @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
        @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) throws NotFoundException {
        return pedidoNegocio.pesquisaPorData(inicio, fim);
    }

    @GetMapping(value = "/estado/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PedidoDTO> buscarPorEstado(@PathVariable EstadoPedidoDTO estado) throws NotFoundException {
        return pedidoNegocio.pesquisaPorEstado(estado);
    }

    @GetMapping(value = "/estado/{estado}/data/{data}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PedidoDTO> buscarPorEstadoEData(
        @PathVariable EstadoPedidoDTO estado,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) throws NotFoundException {
        return pedidoNegocio.pesquisaPorEstadoEData(estado, data);
    }

    @PostMapping(value = "/cliente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PedidoDTO> buscarPorCliente(@RequestBody ClienteDTO clienteDTO) throws NotFoundException {
        return pedidoNegocio.pesquisaPorCliente(clienteDTO);
    }

    // ------------------------
    // ItemPedido endpoints
    // ------------------------

    @PutMapping(value = "/item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemPedidoDTO alterarItemPedido(@RequestBody ItemPedidoDTO itemPedidoDTO) throws NotValidDataException, NotFoundException {
        return pedidoNegocio.alterarItemPedido(itemPedidoDTO);
    }

    @DeleteMapping(value = "/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void excluirItemPedido(@RequestBody ItemPedidoDTO itemPedidoDTO) throws NotValidDataException, NotFoundException {
        pedidoNegocio.excluirItemPedido(itemPedidoDTO);
    }

    // ------------------------
    // Mudança de estado
    // ------------------------

    @PutMapping(value = "/estado/producao", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mudarParaProducao(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException {
        pedidoNegocio.mudarPedidoParaProducao(pedidoDTO);
    }

    @PutMapping(value = "/estado/pronto", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mudarParaPronto(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException, NotFoundException {
        pedidoNegocio.mudarPedidoParaPronto(pedidoDTO);
    }

    @PutMapping(value = "/estado/entrega", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mudarParaEntrega(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException, NotFoundException {
        pedidoNegocio.mudarPedidoParaEntrega(pedidoDTO);
    }

    @PutMapping(value = "/estado/concluido", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void mudarParaConcluido(@RequestBody PedidoDTO pedidoDTO) throws NotValidDataException, NotFoundException {
        pedidoNegocio.mudarPedidoParaConcluido(pedidoDTO);
    }
}
