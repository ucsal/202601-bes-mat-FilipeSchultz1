package br.com.ucsal.olimpiadas.service;

import br.com.ucsal.olimpiadas.*;
import br.com.ucsal.olimpiadas.repository.*;

import java.util.List;

public class ProvaAplicacaoService {

    private final QuestaoRepository questaoRepo;
    private final TentativaRepository tentativaRepo;

    private long proximoId = 1;

    public ProvaAplicacaoService(QuestaoRepository qRepo, TentativaRepository tRepo) {
        this.questaoRepo = qRepo;
        this.tentativaRepo = tRepo;
    }

    public Tentativa aplicar(long participanteId, long provaId, List<Character> respostas) {

        var questoes = questaoRepo.buscarPorProva(provaId);

        Tentativa tentativa = new Tentativa();
        tentativa.setId(proximoId++);
        tentativa.setParticipanteId(participanteId);
        tentativa.setProvaId(provaId);

        for (int i = 0; i < questoes.size(); i++) {
            var q = questoes.get(i);
            char marcada = respostas.get(i);

            Resposta r = new Resposta();
            r.setQuestaoId(q.getId());
            r.setAlternativaMarcada(marcada);
            r.setCorreta(q.isRespostaCorreta(marcada));

            tentativa.getRespostas().add(r);
        }

        tentativaRepo.salvar(tentativa);
        return tentativa;
    }

    public int calcularNota(Tentativa t) {
        return (int) t.getRespostas().stream().filter(Resposta::isCorreta).count();
    }
}