package com.ProjetoExtensao.CoinEdu.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ProjetoExtensao.CoinEdu.dto.EducativoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EducativoService {

    private List<EducativoDto> conteudos;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<EducativoDto>> typeReference = new TypeReference<>() {};


        InputStream inputStream = getClass().getResourceAsStream("/Json/conteudo.json");

        if (inputStream == null) {
            throw new IOException("Arquivo JSON não encontrado no caminho: /Json/conteudo.json");
        }

        this.conteudos = mapper.readValue(inputStream, typeReference);
    }

    public List<EducativoDto> listarTudo() {
        return conteudos;
    }

    public EducativoDto buscarPorId(String id) {
        return conteudos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}