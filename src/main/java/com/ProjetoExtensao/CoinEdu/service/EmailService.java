package com.ProjetoExtensao.CoinEdu.service;


import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public String gerarCodigo() {
        return String.format("%06d" , new SecureRandom().nextInt(999999));
    }

    public void enviarCodigo(String destinatario , String codigo , String tipo) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(destinatario);

        if (tipo.equals("cadastro")) {
            msg.setSubject("CoinEdu - Confirme seu cadastro");
            msg.setText("""
                    Bem vindo ao CoinEdu $
                    
                    seu código de verificação é:
                    
                    %s
                    
                    O código expira em 15 minutos.
                    se não foi você, apena ignore este e-mail.

                    """.formatted(codigo));
        }
        mailSender.send(msg);
    }



    public void enviarEmail(String destinatario, String assunto, String corpo) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(destinatario);
        msg.setSubject(assunto);
        msg.setText(corpo);

        mailSender.send(msg);
    }

    public void enviarAlertaQueda(String email, String nome, String moeda, Double preco, double variacao) {
        String assunto = "📉 " + moeda + " caiu " + String.format("%.1f", Math.abs(variacao)) + "%!";
        String corpo = "Olá, " + nome + "!\n\n"
                + moeda + " caiu " + String.format("%.1f", Math.abs(variacao)) + "% "
                + "e está em R$ " + String.format("%.2f", preco) + ".\n\n"
                + "Pode ser um bom momento para comprar!\n\n"
                + "Acesse o CoinEdu para acompanhar."
                + "http://localhost:5173/";

        enviarEmail(email, assunto, corpo);
    }

    public void enviarAlertaSubida(String email, String nome, String moeda, Double preco, double variacao) {
        String assunto = "📈 " + moeda + " subiu " + String.format("%.1f", variacao) + "%!";
        String corpo = "Olá, " + nome + "!\n\n"
                + moeda + " subiu " + String.format("%.1f", variacao) + "% "
                + "e está em R$ " + String.format("%.2f", preco) + ".\n\n"
                + "Pode ser um bom momento para vender!\n\n"
                + "Acesse o CoinEdu para acompanhar."
                + "http://localhost:5173/";

        enviarEmail(email, assunto, corpo);
    }
    }

