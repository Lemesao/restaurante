package ifmt.cba.restaurante.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifmt.cba.restaurante.negocio.RegistroEstoqueNegocio;


@RestController
@RequestMapping("/RegistroEstoque")
public class RegistroEstoqueController {

    @Autowired
    private RegistroEstoqueNegocio registroEstoqueNegocio;

}
