# RESEARCH — EcoleDZ

Verified 2026-10-10 via web search:
1. Grading scale 0–20, pass mark 10/20 (Nuffic: "10.00 or higher is a pass"; hsabet.fr: "<10 insuffisant, 10–12 passable, 12–14 assez bien, 14–16 bien, 16–18 très bien, 18+ excellent").
   Sources: https://www.nuffic.nl/en/education-systems/algeria/grades-and-study-results, https://hsabet.com/fr/calculateur-moyenne-primaire
2. Primary (1AP–5AP): all subjects equal coefficient → general average = simple mean (hsabet.fr + hsabet.com/en).
3. Moyen subject average formula (Algerie360, ministry circular 2023): ((continuous + written devoir)/2 + exam×2)/3 — implemented as configurable per-grade mean + coefficients (superset covering primary + moyen).
4. End-of-primary exam abolished 2021/22 (hsabet); progression = annual average by school.
5. Stack (developer.android.com offline-first + 2026 guides): Room as single source of truth observed via Flow/StateFlow in Compose; no network layer needed for 100% offline (our case: zero network code = trivially offline-first).

# VALIDATE
- Need: teachers want fast saisie, averages auto, attendance, FR/AR, no internet, no account. ✅ covered.
- Constraint: pupil data on-device only ✅ (Room local, no INTERNET permission in manifest).
- Constraint: MIT clean-room ✅ (all code authored here, no copied snippets).

# SELECT
- Kotlin 2.0.20, Compose BOM 2024.09.00 + Material3, Room 2.6.1 + KSP, Navigation-Compose, minSdk 26 (covers ~99% DZ devices), target/compile 34, JDK 17.
- No backend, no WorkManager (nothing to sync), no analytics.

# PLAN
- Entities: SchoolClass, Pupil, Grade(subject/term/value/coef), Attendance(date/status).
- UI: Classes→Pupils→Grades; Attendance by class+date; About. Bottom nav, FAB add class.
- Logic: GradeCalculator pure object, 7 unit tests.
- CI: test + lint + assembleDebug + upload APK artifact.
- Costs: $0 (GitHub free, no infra). Limitations: no cloud sync/export PDF yet; see README/docs.
