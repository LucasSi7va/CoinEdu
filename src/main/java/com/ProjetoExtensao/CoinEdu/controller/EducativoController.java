package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.EducativoDto;
import com.ProjetoExtensao.CoinEdu.service.EducativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/educativo")
public class EducativoController {

@Autowired
    private EducativoService educativoService;

@GetMapping
    public ResponseEntity<List<EducativoDto>> getTodos() {
    return ResponseEntity.ok(educativoService.listarTudo());
}


@GetMapping("/{id}")
    public ResponseEntity<EducativoDto> getPorId(@PathVariable String id) {
    return ResponseEntity.ok(educativoService.buscarPorId(id));
}

}
