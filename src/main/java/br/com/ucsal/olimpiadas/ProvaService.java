package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.Prova;
import br.com.ucsal.olimpiadas.repository.ProvaRepository;

public class ProvaService {

    private final ProvaRepository repo;
    private long proximoId = 1;

    public ProvaService(ProvaRepository repo) {
        this.repo = repo;
    }

    public Prova cadastrar(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título inválido");
        }

        Prova p = new Prova();
        p.setId(proximoId++);
        p.setTitulo(titulo);

        repo.salvar(p);
        return p;
    }
}