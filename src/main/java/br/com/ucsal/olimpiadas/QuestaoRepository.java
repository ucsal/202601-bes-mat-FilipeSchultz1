package br.com.ucsal.olimpiadas.repository;

import java.util.ArrayList;
import java.util.List;
import br.com.ucsal.olimpiadas.Questao;

public class QuestaoRepository {

    private final List<Questao> questoes = new ArrayList<>();

    public void salvar(Questao q) {
        questoes.add(q);
    }

    public List<Questao> listar() {
        return questoes;
    }

    public List<Questao> buscarPorProva(long provaId) {
        return questoes.stream()
                .filter(q -> q.getProvaId() == provaId)
                .toList();
    }
}