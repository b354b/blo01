package b354b.blog01.springbootdeveloper.repository;

import b354b.blog01.springbootdeveloper.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
