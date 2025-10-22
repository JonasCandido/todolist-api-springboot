package br.com.jonascandido.todolistapi.internal.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
}
