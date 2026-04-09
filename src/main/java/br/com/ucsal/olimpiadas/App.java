package br.com.ucsal.olimpiadas;

import java.util.List;
import java.util.Scanner;

import br.com.ucsal.olimpiadas.repository.*;
import br.com.ucsal.olimpiadas.service.*;

public class App {

	private static final Scanner in = new Scanner(System.in);

	// Repositories
	static ParticipanteRepository participanteRepo = new ParticipanteRepository();
	static ProvaRepository provaRepo = new ProvaRepository();
	static QuestaoRepository questaoRepo = new QuestaoRepository();
	static TentativaRepository tentativaRepo = new TentativaRepository();

	// Services
	static ParticipanteService participanteService = new ParticipanteService(participanteRepo);
	static ProvaService provaService = new ProvaService(provaRepo);
	static ProvaAplicacaoService provaAplicacaoService =
			new ProvaAplicacaoService(questaoRepo, tentativaRepo);

	public static void main(String[] args) {
		seed();

		while (true) {
			System.out.println("\n=== OLIMPÍADA DE QUESTÕES (V1) ===");
			System.out.println("1) Cadastrar participante");
			System.out.println("2) Cadastrar prova");
			System.out.println("3) Cadastrar questão (A–E) em uma prova");
			System.out.println("4) Aplicar prova (selecionar participante + prova)");
			System.out.println("5) Listar tentativas (resumo)");
			System.out.println("0) Sair");
			System.out.print("> ");

			switch (in.nextLine()) {
			case "1" -> cadastrarParticipante();
			case "2" -> cadastrarProva();
			case "3" -> cadastrarQuestao();
			case "4" -> aplicarProva();
			case "5" -> listarTentativas();
			case "0" -> {
				System.out.println("tchau");
				return;
			}
			default -> System.out.println("opção inválida");
			}
		}
	}

	static void cadastrarParticipante() {
		System.out.print("Nome: ");
		var nome = in.nextLine();

		System.out.print("Email: ");
		var email = in.nextLine();

		try {
			var p = participanteService.cadastrar(nome, email);
			System.out.println("Participante cadastrado: " + p.getId());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static void cadastrarProva() {
		System.out.print("Título da prova: ");
		var titulo = in.nextLine();

		try {
			var p = provaService.cadastrar(titulo);
			System.out.println("Prova criada: " + p.getId());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static void cadastrarQuestao() {
		if (provaRepo.listar().isEmpty()) {
			System.out.println("não há provas cadastradas");
			return;
		}

		var provaId = escolherProva();
		if (provaId == null)
			return;

		System.out.println("Enunciado:");
		var enunciado = in.nextLine();

		var alternativas = new String[5];
		for (int i = 0; i < 5; i++) {
			char letra = (char) ('A' + i);
			System.out.print("Alternativa " + letra + ": ");
			alternativas[i] = letra + ") " + in.nextLine();
		}

		System.out.print("Alternativa correta (A–E): ");
		char correta;
		try {
			correta = Questao.normalizar(in.nextLine().trim().charAt(0));
		} catch (Exception e) {
			System.out.println("alternativa inválida");
			return;
		}

		var q = new Questao();
		q.setId(System.currentTimeMillis()); // substitui contador simples
		q.setProvaId(provaId);
		q.setEnunciado(enunciado);
		q.setAlternativas(alternativas);
		q.setAlternativaCorreta(correta);

		questaoRepo.salvar(q);

		System.out.println("Questão cadastrada: " + q.getId());
	}

	static void aplicarProva() {
		if (participanteRepo.listar().isEmpty()) {
			System.out.println("cadastre participantes primeiro");
			return;
		}
		if (provaRepo.listar().isEmpty()) {
			System.out.println("cadastre provas primeiro");
			return;
		}

		var participanteId = escolherParticipante();
		if (participanteId == null)
			return;

		var provaId = escolherProva();
		if (provaId == null)
			return;

		var questoes = questaoRepo.buscarPorProva(provaId);

		if (questoes.isEmpty()) {
			System.out.println("esta prova não possui questões cadastradas");
			return;
		}

		System.out.println("\n--- Início da Prova ---");

		List<Character> respostas = new java.util.ArrayList<>();

		for (var q : questoes) {
			System.out.println("\nQuestão #" + q.getId());
			System.out.println(q.getEnunciado());

			System.out.println("Posição inicial:");
			imprimirTabuleiroFen(q.getFenInicial());

			for (var alt : q.getAlternativas()) {
				System.out.println(alt);
			}

			System.out.print("Sua resposta (A–E): ");
			char marcada;
			try {
				marcada = Questao.normalizar(in.nextLine().trim().charAt(0));
			} catch (Exception e) {
				System.out.println("resposta inválida");
				marcada = 'X';
			}

			respostas.add(marcada);
		}

		var tentativa = provaAplicacaoService.aplicar(participanteId, provaId, respostas);

		int nota = provaAplicacaoService.calcularNota(tentativa);

		System.out.println("\n--- Fim da Prova ---");
		System.out.println("Nota: " + nota + " / " + respostas.size());
	}

	static void listarTentativas() {
		System.out.println("\n--- Tentativas ---");

		for (var t : tentativaRepo.listar()) {
			System.out.printf("#%d | participante=%d | prova=%d | nota=%d/%d%n",
					t.getId(),
					t.getParticipanteId(),
					t.getProvaId(),
					provaAplicacaoService.calcularNota(t),
					t.getRespostas().size());
		}
	}

	static Long escolherParticipante() {
		System.out.println("\nParticipantes:");
		for (var p : participanteRepo.listar()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getNome());
		}

		System.out.print("Escolha o id: ");

		try {
			long id = Long.parseLong(in.nextLine());
			if (participanteRepo.buscarPorId(id) == null) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
		}
	}

	static Long escolherProva() {
		System.out.println("\nProvas:");
		for (var p : provaRepo.listar()) {
			System.out.printf("  %d) %s%n", p.getId(), p.getTitulo());
		}

		System.out.print("Escolha o id: ");

		try {
			long id = Long.parseLong(in.nextLine());
			if (provaRepo.buscarPorId(id) == null) {
				System.out.println("id inválido");
				return null;
			}
			return id;
		} catch (Exception e) {
			System.out.println("entrada inválida");
			return null;
		}
	}

	static void imprimirTabuleiroFen(String fen) {
		String parteTabuleiro = fen.split(" ")[0];
		String[] ranks = parteTabuleiro.split("/");

		System.out.println();
		System.out.println("    a b c d e f g h");
		System.out.println("   -----------------");

		for (int r = 0; r < 8; r++) {
			String rank = ranks[r];
			System.out.print((8 - r) + " | ");

			for (char c : rank.toCharArray()) {
				if (Character.isDigit(c)) {
					int vazios = c - '0';
					for (int i = 0; i < vazios; i++) {
						System.out.print(". ");
					}
				} else {
					System.out.print(c + " ");
				}
			}

			System.out.println("| " + (8 - r));
		}

		System.out.println("   -----------------");
		System.out.println("    a b c d e f g h");
		System.out.println();
	}

	static void seed() {
		var prova = provaService.cadastrar("Olimpíada 2026 • Nível 1 • Prova A");

		var q1 = new Questao();
		q1.setId(System.currentTimeMillis());
		q1.setProvaId(prova.getId());

		q1.setEnunciado("""
				Questão 1 — Mate em 1.
				É a vez das brancas.
				Encontre o lance que dá mate imediatamente.
				""");

		q1.setFenInicial("6k1/5ppp/8/8/8/7Q/6PP/6K1 w - - 0 1");

		q1.setAlternativas(new String[] {
				"A) Qh7#", "B) Qf5#", "C) Qc8#", "D) Qh8#", "E) Qe6#"
		});

		q1.setAlternativaCorreta('C');

		questaoRepo.salvar(q1);
	}
}