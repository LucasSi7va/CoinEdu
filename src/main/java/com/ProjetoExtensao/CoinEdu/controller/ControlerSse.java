package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.service.ServiceSse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
public class ControlerSse {

@Autowired
private final ServiceSse serviceSse;

@GetMapping("/alertas/stream")
    public SseEmitter stream() {
    return serviceSse.conectar();
}
}
