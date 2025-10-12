# TECHNICAL DESIGN DOCUMENT

## Purpose

- Document the technical design and architecture of the project.

## Directory

- Stored in the `.claude/docs` directory at the root of the project.

## Sections

- Architecture: `.claude/docs/architecture`
- Event Bus: `.claude/docs/event-bus`

# ARCHITECTURE DECISION RECORDS (ADR)

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

