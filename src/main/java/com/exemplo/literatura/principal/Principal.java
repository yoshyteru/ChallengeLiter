package com.exemplo.literatura.principal;

import com.exemplo.literatura.model.*;
import com.exemplo.literatura.repository.AutorRepository;
import com.exemplo.literatura.repository.LivroRepository;
import com.exemplo.literatura.service.ConsumoApi;
import com.exemplo.literatura.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private final LivroRepository livroRepository;

    private final AutorRepository autorRepository;

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    \n=== CATÁLOGO DE LIVROS ===
                    1 - Buscar livro por título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros em determinado idioma
                    6 - Estatísticas por idioma
                    0 - Sair
                    """;
            System.out.println(menu);
            System.out.print("Escolha uma opção: ");

            try {
                opcao = leitura.nextInt();
                leitura.nextLine(); // limpa o buffer do enter
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida! Digite um número.");
                leitura.nextLine(); // limpa entrada errada
                continue;
            }

            switch (opcao) {
                case 1 -> buscarLivroPorTitulo();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLivrosPorIdioma();
                case 6 -> exibirEstatisticasPorIdioma();
                case 0 -> System.out.println("Encerrando aplicação...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.print("\nDigite o título do livro: ");
        var titulo = leitura.nextLine();

        // Verifica se já existe no banco (DERIVED QUERY)
        Optional<Livro> livroExistente = livroRepository.findByTituloContainingIgnoreCase(titulo);
        if (livroExistente.isPresent()) {
            System.out.println("\nLivro já existe no banco de dados!");
            System.out.println(livroExistente.get());
            return;
        }

        // Busca na API Gutendex
        String ENDERECO = "https://gutendex.com/books/";
        var url = ENDERECO + "?search=" + titulo.replace(" ", "+");
        System.out.println("Buscando na API: " + url);

        var json = consumo.obterDados(url);
        var resposta = conversor.obterDados(json, RespostaApi.class);

        if (resposta.results() == null || resposta.results().isEmpty()) {
            System.out.println("Livro não encontrado na API!");
            return;
        }

        // Pega o primeiro resultado da API
        DadosLivro dadosLivro = resposta.results().get(0);
        Livro livro = new Livro(dadosLivro);

        // Trata o autor (pega só o primeiro)
        if (dadosLivro.autores() != null && !dadosLivro.autores().isEmpty()) {
            DadoAutor dadosAutor = dadosLivro.autores().get(0);

            // Verifica se autor já existe (DERIVED QUERY)
            Autor autor = autorRepository.findByNome(dadosAutor.nome())
                    .orElseGet(() -> {
                        // Se não existe, cria e salva
                        Autor novoAutor = new Autor(dadosAutor);
                        return autorRepository.save(novoAutor);
                    });

            livro.setAutor(autor);
        }

        // Salva no banco
        livroRepository.save(livro);
        System.out.println("\nLivro salvo com sucesso!");
        System.out.println(livro);
    }

    private void listarLivrosRegistrados() {
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado.");
            return;
        }
        System.out.println("\n=== LIVROS REGISTRADOS ===");
        livros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNenhum autor registrado.");
            return;
        }
        System.out.println("\n=== AUTORES REGISTRADOS ===");
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.print("\nDigite o ano: ");
        try {
            int ano = leitura.nextInt();
            leitura.nextLine();

            // Usa DERIVED QUERY para buscar autores vivos no ano
            List<Autor> autores = autorRepository
                    .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);

            if (autores.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado nesse ano.");
                return;
            }

            System.out.println("\n=== AUTORES VIVOS EM " + ano + " ===");
            autores.forEach(System.out::println);

        } catch (InputMismatchException e) {
            System.out.println("Ano inválido! Digite um número.");
            leitura.nextLine();
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("\nDigite o idioma (ex: pt, en, es, fr): ");
        var idioma = leitura.nextLine();

        // Usa DERIVED QUERY
        List<Livro> livros = livroRepository.findByIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + idioma);
            return;
        }

        System.out.println("\n=== LIVROS EM '" + idioma + "' ===");
        livros.forEach(System.out::println);
    }

    private void exibirEstatisticasPorIdioma() {
        System.out.println("\n=== ESTATÍSTICAS POR IDIOMA ===");

        List<Livro> todosLivros = livroRepository.findAll();

        if (todosLivros.isEmpty()) {
            System.out.println("Nenhum livro registrado para estatísticas.");
            return;
        }

        // Usa Java Streams para agrupar e contar
        Map<String, Long> estatisticas = todosLivros.stream()
                .collect(Collectors.groupingBy(Livro::getIdioma, Collectors.counting()));

        estatisticas.forEach((idioma, quantidade) ->
                System.out.println("Idioma: " + idioma + " - Quantidade: " + quantidade));

        // Usa DERIVED QUERIES para contagens específicas
        System.out.println("\n--- Contagem específica (Derived Queries) ---");
        System.out.println("Livros em Português (pt): " + livroRepository.countByIdioma("pt"));
        System.out.println("Livros em Inglês (en): " + livroRepository.countByIdioma("en"));
        System.out.println("Livros em Espanhol (es): " + livroRepository.countByIdioma("es"));
        System.out.println("Livros em Francês (fr): " + livroRepository.countByIdioma("fr"));
    }
}