package br.com.jonascandido.todolistapi.internal.todo;

import br.com.jonascandido.todolistapi.internal.user.User;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_todos_user_id"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_todo_status_status_id"))
    private TodoStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    protected Todo() {}

    public Todo(String title, String description, User user, TodoStatus status) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
}
