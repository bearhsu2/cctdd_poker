# ARCHITECTURE PATTERNS

For detailed architecture patterns (Controller, Service, Aggregate/Entity, Repository, Domain Service), see
`.claude/docs/clean-architecture.md`.

# ARCHITECTURE DECISION RECORDS

## Purpose

- Document the architecture decisions made in this project.
- Provide context and rationale for future reference.

## Directory

- Store ADRs in the `.ADR` directory at the root of the project.

## Rules

- When making significant architectural decisions, create a new ADR document
- Use a numbered format: `XXX-title-of-decision.md` (e.g., `001-use-event-sourcing.md`)
- Each ADR should include:
    - Title
    - Status (Proposed, Accepted, Deprecated, Superseded)
    - Context (what is the issue we're addressing)
    - Decision (what we decided to do)
    - Consequences (what becomes easier or harder as a result)
- ADRs are immutable once accepted - if a decision changes, create a new ADR that supersedes the old one

