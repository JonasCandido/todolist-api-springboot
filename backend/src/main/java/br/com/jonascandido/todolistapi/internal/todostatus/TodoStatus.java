package br.com.jonascandido.todolistapi.internal.todostatus;

import jakarta.persistence.*;

@Entity
@Table(name = "todo_status")
public class TodoStatus {

    protected TodoStatus() {}

    public TodoStatus(String name) {
        this.name = name;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
