# EcoleDZ — Design spec (2026 standard, Penpot-ready)

No Penpot tool is available in this environment, so this file is the
source of truth a designer can reproduce 1:1 in Penpot.

## Tokens (shared DZ direction)
- Color: Material 3 dynamic color (system seed) + explicit light/dark
  schemes; contrast ≥ 4.5:1 for body text. No hard-coded brand hex in
  components — use `colorScheme.primary / onPrimary / surface / onSurface`.
- Type: M3 type scale (`titleLarge/Medium`, `bodyMedium`, `labelLarge`).
- Shape: M3 default (cards 12dp, FAB 16dp).
- Spacing: 4/8/16/24dp grid. Touch targets ≥ 48dp.
- Dark mode: `isSystemInDarkTheme()` + dynamic dark scheme (no separate palette).
- RTL: `supportsRtl=true`, FR default + `values-ar` strings; no
  left/right hard-coding (use start/end, `Arrangement.SpaceBetween`).

## Screens (3 tabs)
1. Classes (list→detail, adaptive):
   - Compact (<840dp): list, tap → detail replaces list, "← Classes" back.
   - Wide (≥840dp): two-pane list | detail with divider.
   - Cards: class name + delete icon (content-desc "Supprimer la classe X").
   - Detail: Nom/Prénom fields + 48dp "+" button; pupil cards with
     average "/20 + mention", grade rows, "+ Note" expander.
2. Présences: date title, class FilterChips, pupil card with 4 status
   chips (Présent/Absent/Retard/Excusé), persisted per day via Room.
3. À propos: version, offline statement, 0–20 scale rules, FR/AR note.

## Implemented in `MainActivity.kt`
- `EcoleDzTheme` (dynamic light/dark), `enableEdgeToEdge()`,
  safe-drawing insets, nav icons + content descriptions,
  `ClassListPane` + `PupilListPane` adaptive panes, 48dp touch targets.
