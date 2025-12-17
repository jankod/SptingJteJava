package hr.ja.st.project.domain;

import hr.ja.st.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "projects", uniqueConstraints = @UniqueConstraint(name = "uk_project_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 2048)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ProjectMember> members = new HashSet<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }

    public Optional<User> owner() {
        return members.stream()
                .filter(pm -> pm.getRole() == ProjectRole.OWNER)
                .map(ProjectMember::getUser)
                .findFirst();
    }

    public List<User> participants() {
        return members.stream()
                .filter(pm -> pm.getRole() == ProjectRole.PARTICIPANT)
                .map(ProjectMember::getUser)
                .toList();
    }

    public void setOwner(User user) {
        // remove existing owner
        members.removeIf(pm -> pm.getRole() == ProjectRole.OWNER);
        addMember(user, ProjectRole.OWNER);
    }

    public void addParticipant(User user) {
        if (user == null) {
            return;
        }
        boolean alreadyOwner = owner()
                .map(User::getId)
                .filter(id -> Objects.equals(id, user.getId()))
                .isPresent();
        if (alreadyOwner) {
            return;
        }
        addMember(user, ProjectRole.PARTICIPANT);
    }

    public void removeParticipant(User user) {
        members.removeIf(pm -> pm.getUser().getId().equals(user.getId()) && pm.getRole() == ProjectRole.PARTICIPANT);
    }

    public boolean isOwner(Long userId) {
        return owner().map(u -> Objects.equals(u.getId(), userId)).orElse(false);
    }

    private void addMember(User user, ProjectRole role) {
        // Users must appear only once per project; update existing membership if necessary
        ProjectMember existing = members.stream()
                .filter(pm -> Objects.equals(pm.getUser().getId(), user.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setRole(role);
            return;
        }

        ProjectMember pm = new ProjectMember();
        pm.setProject(this);
        pm.setUser(user);
        pm.setRole(role);
        members.add(pm);
    }
}
