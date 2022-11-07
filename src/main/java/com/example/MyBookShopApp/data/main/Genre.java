package com.example.MyBookShopApp.data.main;

import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "genres")
@Data
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Genre parentGenre;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "parentGenre")
    private List<Genre> embeddedGenreList;
    @OneToMany(mappedBy = "genre")
    private List<Book> bookList;
    @Transient
    private List<Book> bookListIncludedBooksOfEmbeddedGenres;

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", parentGenre=" + (parentGenre == null ? "no parent" : parentGenre.getName()) +
                ", name='" + name + '\'' +
                ", embeddedGenreList=" + Arrays.toString(embeddedGenreList.stream().map(Genre::getName).toArray()) +
                '}';
    }

    public boolean getEmbeddedEmbedded() {
        return embeddedGenreList.stream().anyMatch(g -> !g.getEmbeddedGenreList().isEmpty());
    }
}
