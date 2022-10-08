package com.example.MyBookShopApp.data;

import javax.persistence.*;

@Entity
@Table(name = "book_files")
public class BookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String hash;
    @Column(nullable = false)
    private int typeId;
    @Column(nullable = false)
    private String path;

    @Override
    public String toString() {
        return "BookFile{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", typeId=" + typeId +
                ", path='" + path + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
