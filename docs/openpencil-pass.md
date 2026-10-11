# OpenPencil pass — ecole-dz

## Design (Shared/OpenPencil/ecole-dz/)
- `design.html` (class card + grade calculator, DZ tokens) → `design.fig`
- `lint --preset accessibility`: 0 errors, 0 warnings

## Code review (no rewrite needed)
All icons already carry descriptions (`contentDescription = cd`, add/delete labeled).
No code change. Note: default branch is `master` — pushes target it.

## APKs
`android-build.yml` adds `assembleRelease` (unsigned, same retry wrapper) + release artifact +
versioned `v*-b*` GitHub Release with notes + both APKs.
