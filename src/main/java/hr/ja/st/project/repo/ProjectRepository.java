package hr.ja.st.project.repo;

import hr.ja.st.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByName(String name);
}

