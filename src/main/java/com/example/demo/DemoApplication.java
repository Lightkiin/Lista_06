package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;

import java.util.*;

// =========================
// MAIN
// =========================
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// =========================
// EXERCÍCIO 1 - Recomendador de Viagem
// =========================
@RestController
class ViagemController {
    @GetMapping("/recomendar")
    public String recomendarDestino(@RequestParam String clima, @RequestParam String estilo) {
        if (clima.equalsIgnoreCase("calor") && estilo.equalsIgnoreCase("natureza")) {
            return "Rio de Janeiro";
        } else if (clima.equalsIgnoreCase("frio") && estilo.equalsIgnoreCase("cidade")) {
            return "Gramado";
        } else if (clima.equalsIgnoreCase("calor") && estilo.equalsIgnoreCase("praia")) {
            return "Florianópolis";
        } else if (clima.equalsIgnoreCase("frio") && estilo.equalsIgnoreCase("aventura")) {
            return "Bariloche";
        }
        return "Destino não encontrado";
    }
}

// =========================
// EXERCÍCIO 2 - Gerador de Jogador
// =========================
class Jogador {
    private String nome;
    private String sobrenome;
    private int idade;
    private String posicao;
    private String time;

    public Jogador(String nome, String sobrenome, int idade, String posicao, String time) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.posicao = posicao;
        this.time = time;
    }
    public String getNome() { return nome; }
    public String getSobrenome() { return sobrenome; }
    public int getIdade() { return idade; }
    public String getPosicao() { return posicao; }
    public String getTime() { return time; }
}

@Service
class JogadorService {
    private final List<String> nomes = List.of("Ronaldo", "Pelé", "Neymar", "Messi", "Cristiano");
    private final List<String> sobrenomes = List.of("Fenômeno", "Gaúcho", "Silva", "Júnior", "dos Santos");

    public Jogador gerarJogador(String time, String posicao) {
        Random random = new Random();
        String nome = nomes.get(random.nextInt(nomes.size()));
        String sobrenome = sobrenomes.get(random.nextInt(sobrenomes.size()));
        int idade = 18 + random.nextInt(20);
        return new Jogador(nome, sobrenome, idade, posicao, time);
    }
}

@RestController
@RequestMapping("/jogador")
class JogadorController {
    private final JogadorService jogadorService;
    public JogadorController(JogadorService jogadorService) { this.jogadorService = jogadorService; }

    @GetMapping("/{time}/{posicao}")
    public Jogador gerarJogador(@PathVariable String time, @PathVariable String posicao) {
        return jogadorService.gerarJogador(time, posicao);
    }
}

// =========================
// EXERCÍCIO 3 - Estudantes
// =========================
class Estudante {
    private String nome;
    private int codigo;
    private String curso;
    private String email;
    private String telefone;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}

@RestController
@RequestMapping("/estudantes")
class EstudanteController {
    private final List<Estudante> estudantes = new ArrayList<>();

    @PostMapping
    public Estudante adicionarEstudante(@RequestBody Estudante estudante) {
        estudantes.add(estudante);
        return estudante;
    }

    @GetMapping
    public List<Estudante> listarEstudantes() {
        return estudantes;
    }

    @GetMapping("/{codigo}")
    public Estudante buscarPorCodigo(@PathVariable int codigo) {
        return estudantes.stream()
                .filter(e -> e.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }
}

// =========================
// EXERCÍCIO 4 - Playlist
// =========================
class Musica {
    private int id;
    private String nome;
    private String artista;
    private String album;
    private int duracao;
    private String genero;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public int getDuracao() { return duracao; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}

class Playlist {
    private int id;
    private String nome;
    private List<Musica> musicas = new ArrayList<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<Musica> getMusicas() { return musicas; }
}

@RestController
@RequestMapping("/playlists")
class PlaylistController {
    private final Map<Integer, Playlist> playlists = new HashMap<>();
    private int contador = 1;

    @PostMapping
    public int criarPlaylist(@RequestBody Playlist playlist) {
        playlist.setId(contador++);
        playlists.put(playlist.getId(), playlist);
        return playlist.getId();
    }

    @PostMapping("/{id}/musicas")
    public String adicionarMusica(@PathVariable int id, @RequestBody Musica musica) {
        Playlist playlist = playlists.get(id);
        if (playlist == null) return "Playlist não encontrada!";
        playlist.getMusicas().add(musica);
        return "Música adicionada!";
    }

    @GetMapping
    public Collection<Playlist> listarPlaylists() {
        return playlists.values();
    }

    @GetMapping("/{id}/musicas")
    public List<Musica> listarMusicas(@PathVariable int id) {
        Playlist playlist = playlists.get(id);
        return playlist != null ? playlist.getMusicas() : List.of();
    }
}
