# Change Case Companion

IntelliJ-family plugin. Select an identifier (or just place the caret
on one — no selection needed) and instantly convert it between
`camelCase`, `PascalCase`, `snake_case`, `kebab-case`, and
`CONSTANT_CASE`.

## Why it exists

Ports a pattern that's genuinely popular elsewhere — VS Code has
multiple "change case" extensions with millions of combined installs —
with no real equivalent anywhere in JetBrains Marketplace (confirmed
by search before building this, not assumed). A deliberate "port a
proven concept" bet, not a competitor-complaint-driven build — the
same documented-exception discipline this follows (same treatment as
Refactor Simulator/Bean Copy Companion/
Turbo Log Companion).

## Why built this way

- **Works starting from ANY of the 5 styles, not just camelCase.** The
  converter never assumes the input's current style: it splits on
  separators first, then on camelCase/PascalCase word boundaries
  (including acronym runs — `XMLHttpRequest` splits into `XML`/`Http`/
  `Request`, not letter-by-letter), and only then re-assembles the
  words into the target style.
- **Operates on Editor text directly, never PSI.** Case conversion is
  a plain-text operation — restricting it to PSI-resolvable
  identifiers would make it useless on JSON/YAML keys, CSS classes, or
  anywhere else an "identifier" isn't a real language symbol in the
  platform's eyes. Works identically in any file type.
- **Deliberately synchronous, no background-thread dispatch.**
  Splitting and rejoining a short identifier string is microseconds of
  work — not the kind of computation that needs moving off the EDT.
  Forcing a pooled-thread hop here would add real
  complexity for zero benefit.
- **100% local** — no network call, no account, no telemetry.

## Usage

Select text (or just place the caret inside/next to a word) →
right-click → **Change Case Companion** → pick the target style.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us
at **gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
