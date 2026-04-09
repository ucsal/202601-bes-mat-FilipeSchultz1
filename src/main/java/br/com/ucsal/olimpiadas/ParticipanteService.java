package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.Participante;
import br.com.ucsal.olimpiadas.repository.ParticipanteRepository;

public class ParticipanteService {

    private final ParticipanteRepository repo;
    private long proximoId = 1;

    public ParticipanteService(ParticipanteRepository repo) {
        this.repo = repo;
    }

    public Participante cadastrar(String nome, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        Participante p = new Participante();
        p.setId(proximoId++);
        p.setNome(nome);
        p.setEmail(email);

        repo.salvar(p);
        return p;
    }
}