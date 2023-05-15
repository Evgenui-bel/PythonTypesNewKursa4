package com.example.dictionary.server.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Класс представляющий словарный термин.
 */
public class Term implements Entity, Serializable {
    @Serial
    private static final long serialVersionUID = 4807390829636705565L;

    private int id;
    private String name;
    private String information;

    public Term(int id, String name, String information) {
        this.id = id;
        this.name = name;
        this.information = information;
    }

    public Term(String name, String information) {
        this.name = name;
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return id == term.id && Objects.equals(name, term.name) && Objects.equals(information, term.information);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, information);
    }
}
