# DELIVERY — EcoleDZ v1.0.0 (2026-10-10)

## Test table (verified locally)
| # | Test | Result |
|---|------|--------|
| 1 | Logic: empty grades → null | PASS (python replica of GradeCalculator) |
| 2 | Logic: primary simple mean 12+16+14 → 14.0 | PASS |
| 3 | Logic: weighted (10×2+20)/3 | PASS |
| 4 | Logic: multi grades per subject (10+14)/2=12 | PASS |
| 5 | Logic: grade bounds 0–20 | PASS |
| 6 | Logic: mentions 9.9→Insuffisant … 18.5→Excellent | PASS |
| 7 | Static: 7 JUnit tests in GradeCalculatorTest.kt mirror the 6 logic tests + AR mentions | WRITTEN, runs in CI (no JDK locally) |
| 8 | Static: no INTERNET permission in manifest | PASS (verified False) |
| 9 | Static: supportsRtl + values-ar present | PASS |
| 10 | Cloud build (test+lint+assembleDebug+APK artifact) | PENDING repo creation (workflow ready at .github/workflows/android.yml) |

Local commit: 1388087, 18 files, branch master.

## Publish status
- Local git repo: READY at `ecoledz/` (commit 1388087).
- Remote `github.com/DAYONEPROMISE/ecoledz`: NOT FOUND (`ls-remote` → "Repository not found").
- Blocked: no `gh`, no GitHub token/MCP, no browser tool in this environment → cannot create repo or push.
- Unblock (one action): connect GitHub in Server settings → Connectors tab (gives git/gh auth), or provide a PAT; then: `gh repo create DAYONEPROMISE/ecoledz --public --source=. --push` and CI builds the APK automatically.

## Costs: $0 (GitHub free tier, no backend/infra; app is fully offline).
## Limitations
- APK not yet built (needs CI after push; no JDK/Android SDK on this machine).
- No cloud sync, no PDF export, no multi-term bulletins yet — Room schema v1 supports adding them.
- minSdk 26; Arabic strings minimal (app name + mentions); full AR translation pending teacher feedback.

## First-10-customers plan
1. 2 primary teachers (Algiers, Oran) — install debug APK, saisie of 1 real class.
2. 2 moyen teachers — validate coefficients + mentions wording.
3. 1 director — validate attendance workflow.
4. Ask each for 1 referral → reach 10 within 2 weeks.
5. Collect: saisie time, missing subjects, AR wording fixes → v1.1.
6. Channel: Facebook teacher groups (DZ), school WhatsApp groups; demo video <2 min, APK via GitHub release.
