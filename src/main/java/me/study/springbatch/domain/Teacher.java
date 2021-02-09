package me.study.springbatch.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "id")
    private List<Student> students;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public List<Student> getStudents() {
        return students;
    }
}
