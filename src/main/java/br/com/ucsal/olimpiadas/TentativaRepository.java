package br.com.ucsal.olimpiadas.repository;

import java.util.ArrayList;
import java.util.List;
import br.com.ucsal.olimpiadas.Tentativa;

public class TentativaRepository {

    private final List<Tentativa> tentativas = new ArrayList<>();

    public void salvar(Tentativa t) {
        tentativas.add(t);
    }

    public List<Tentativa> listar() {
        return tentativas;
    }
}