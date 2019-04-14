package ro.fiipractic.mycinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fiipractic.mycinema.entities.MovieInstance;

public interface MovieInstanceRepository extends JpaRepository<MovieInstance, Long> {
}
