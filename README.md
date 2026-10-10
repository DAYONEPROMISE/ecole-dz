# EcoleDZ — Carnet de notes offline pour enseignants (Algérie)

MIT, clean-room. Kotlin + Jetpack Compose (Material 3) + Room. 100% hors-ligne, aucun compte, données élèves sur l'appareil uniquement.

## Fonctionnalités
- Classes + élèves (CRUD)
- Notes /20 par matière + trimestre, coefficients, moyenne générale + mention
- Présences par jour (Présent/Absent/Retard/Excusé)
- FR/AR avec RTL (`supportsRtl=true`, `values-ar/strings.xml`)
- Échelle 0–20, seuil 10. Primaire: moyenne simple; Moyen/Lycée: pondérée par coefficients.

## Build (cloud)
CI `.github/workflows/android.yml` exécute: tests unitaires + lint + `assembleDebug`, publie l'APK en artifact `ecoledz-debug-apk`.

Local: Android Studio Hedgehog+, JDK 17, `gradle assembleDebug`.

## Structure
`app/src/main/java/dz/ecole/ecoledz/`: `GradeCalculator.kt` (logique pure testée), `Data.kt` (Room), `EcoleDzApp.kt`, `MainActivity.kt` (Compose).
