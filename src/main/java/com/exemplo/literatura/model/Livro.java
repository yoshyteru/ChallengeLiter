package com.exemplo.literatura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String titulo;
    
    private String idioma;
    private Integer numeroDownloads;
    
    // REMOVIDO cascade = CascadeType.ALL - autor já é salvo separadamente
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Livro() {}
    
    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        // Pega apenas o primeiro idioma
        this.idioma = (dadosLivro.idiomas() != null && !dadosLivro.idiomas().isEmpty()) 
                ? dadosLivro.idiomas().get(0) 
                : "Desconhecido";
        this.numeroDownloads = dadosLivro.numeroDownloads();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    
    public Integer getNumeroDownloads() { return numeroDownloads; }
    public void setNumeroDownloads(Integer numeroDownloads) { this.numeroDownloads = numeroDownloads; }
    
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    @Override
    public String toString() {
        return "\n========== LIVRO ==========" +
               "\nTítulo: " + titulo +
               "\nAutor: " + (autor != null ? autor.getNome() : "Desconhecido") +
               "\nIdioma: " + idioma +
               "\nDownloads: " + numeroDownloads +
               "\n==========================\n";
    }
}
