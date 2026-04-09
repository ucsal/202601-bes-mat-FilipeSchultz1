package br.com.ucsal.olimpiadas.repository;

import java.util.ArrayList;
import java.util.List;
import br.com.ucsal.olimpiadas.Participante;

public class ParticipanteRepository {

    private final List<Participante> participantes = new ArrayList<>();

    public void salvar(Participante p) {
        participantes.add(p);
    }

    public List<Participante> listar() {
        return participantes;
    }

    public Participante buscarPorId(long id) {
        return participantes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}