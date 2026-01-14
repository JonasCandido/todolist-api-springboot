package br.com.jonascandido.todolistapi.internal.todostatus;

import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoStatusRepository extends JpaRepository<TodoStatus, Integer> {
    Optional<TodoStatus> findByName(String name); 
}
