# Repository Guidelines

## Project Structure & Modules
- Java Spring Boot app using Maven and JTE template.
- Source: `src/main/java/hr/ja/st/**` (e.g., `web`, `security`, `config`, `user`).
- Templates (JTE): `src/main/jte/{layout,components,pages}`; reference views like `pages/home/index.jte`.
- Static assets: `src/main/resources/static`.
- Config: `src/main/resources/application*.properties` with `dev`, `test`, `prod` profiles.
- Tests: `src/test/java/hr/ja/st/**` (naming: `*Tests.java`).

## Build, Test, Run
- Build jar + precompile JTE: `./mvnw clean package`
- Run (default profile): `./mvnw spring-boot:run`
- Run with profile: `./mvnw spring-boot:run -Pdev` (or `-Pprod`, `-Ptest`)
- Tests only: `./mvnw test`
- Verify (tests + checks): `./mvnw clean verify`

Notes:
- Java version is `24` (see `pom.xml`).
- login page at `/login`.
- Dev profile seeds demo users: `admin/admin` (ADMIN, USER) and `marko/tajna` (USER).

## Coding Style & Naming
- Indentation: 4 spaces; UTF‑8 encoding.
- Packages under `hr.ja.st`. Group by feature (e.g., `web`, `user`, `security`).
- Controllers return JTE view names (e.g., `"pages/about/index.jte"`).
- Prefer constructor injection; Lombok is available (`@Slf4j`, `@RequiredArgsConstructor`, etc.).
- Constants in `UPPER_SNAKE_CASE`; routes centralized in `hr.ja.st.web.Routes`.

## Testing Guidelines
- Frameworks: JUnit 5, Spring Boot Test, Spring Security Test.
- Name tests `*Tests.java` and mirror package of code under test.
- Use `@WebMvcTest` for MVC slices, `@SpringBootTest` for integration.
- Run locally with `./mvnw test`; no enforced coverage, but cover critical paths and security rules.

## Commit & PR Guidelines
- Commits: short, imperative summaries (e.g., `add user`, `refactor packages`).
- Scope optional in summary; keep related changes in one commit.
- PRs must include: clear description, rationale, linked issues, and test notes; add screenshots for UI/template changes.
- Ensure: builds green (`./mvnw clean verify`), tests updated, and dev profile works (`/` and `/about`).

## Security & Config Tips
- Public endpoints: `/login`, `/about`, `/static/**`, `/__dev/**`; others require auth.
- Store secrets via environment/`application-*.properties`; never commit credentials.
- Adjust date/time via `app.datetime.*` properties; see `DateTimeFormatConfig`.

## UI & Template Changes
- For JTE updates, include before/after screenshots for `/about` and authenticated home (`/`, login via `admin/admin`).
- Verify returned view names match templates (e.g., `pages/home/index.jte`).
- Keep components in `src/main/jte/components`; prefer reusable layouts under `layout/`.

## Testing Examples
- MVC slice: `@WebMvcTest(HomeController.class)` for controller routes; mock collaborators.
- Security: use `@WithMockUser(roles = "USER")` or `roles = "ADMIN"` for protected endpoints.
- Data layer: `@DataJpaTest` for repositories (H2 runtime).
- Example commands: run unit tests `./mvnw -q -Dtest=*Tests test`; single test `./mvnw -Dtest=UserControllerTests test`.

Odgovaraj mi na hrvatskom jeziku. Kod i komentare u kodu piši na engleskom jeziku.
