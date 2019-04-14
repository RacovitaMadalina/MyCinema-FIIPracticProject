package ro.fiipractic.mycinema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fiipractic.mycinema.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
