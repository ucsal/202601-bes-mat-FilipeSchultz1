package br.com.ucsal.olimpiadas.repository;

import java.util.ArrayList;
import java.util.List;
import br.com.ucsal.olimpiadas.Prova;

public class ProvaRepository {

    private final List<Prova> provas = new ArrayList<>();

    public void salvar(Prova p) {
        provas.add(p);
    }

    public List<Prova> listar() {
        return provas;
    }

    public Prova buscarPorId(long id) {
        return provas.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}