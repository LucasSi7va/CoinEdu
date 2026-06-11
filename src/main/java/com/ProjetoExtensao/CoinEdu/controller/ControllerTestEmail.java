package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ControllerTestEmail {

    private final EmailService emailService;

    @GetMapping("/teste/email-queda")
    public String testarEmailQueda() {
        emailService.enviarAlertaQueda(
                "colocar email teste",   // coloque seu email aqui
                "Lucas",
                "Bitcoin",
                45000.0,
                -10.5
        );
        return "Email de queda enviado!";
    }

    @GetMapping("/teste/email-subida")
    public String testarEmailSubida() {
        emailService.enviarAlertaSubida(
                "colocar email teste",
                "Lucas",
                "Ethereum",
                12000.0,
                11.3
        );
        return "Email de subida enviado!";
    }
}
