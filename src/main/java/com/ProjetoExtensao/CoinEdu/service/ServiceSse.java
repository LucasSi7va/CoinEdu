package com.ProjetoExtensao.CoinEdu.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ServiceSse {
private final List<SseEmitter> emitters =
        new CopyOnWriteArrayList<>();

    public SseEmitter conectar() {
        SseEmitter emitter = new SseEmitter(30_000L);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event()
                    .name("conectado")
                    .data("Conexão SSE estabelecida"));
        } catch (IOException e) {
            emitter.complete();
        }

        return emitter;
    }

public void enviar(Object dados) {
    List<SseEmitter> removidos = new CopyOnWriteArrayList<>();

    for (SseEmitter emitter : emitters) {
        try {
            emitter.send(SseEmitter.event()
                    .name("alerta")
                    .data(dados, MediaType.APPLICATION_JSON));
        }
        catch (IOException e) {
            emitter.complete();
            removidos.add(emitter);
        }
    }
    emitters.removeAll(removidos);
}



}
