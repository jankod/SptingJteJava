package hr.ja.st.project.repo;

import hr.ja.st.project.domain.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
}

