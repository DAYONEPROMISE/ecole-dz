package dz.ecole.ecoledz

/** Pure grade math for the Algerian 20-point scale. 100% testable, no Android deps. */
object GradeCalculator {

    /** Mean of subject averages weighted by coefficient. Null if no grades. */
    fun generalAverage(subjectGrades: Map<String, List<Double>>, coefficients: Map<String, Double> = emptyMap()): Double? {
        var weightedSum = 0.0
        var totalCoef = 0.0
        for ((subject, grades) in subjectGrades) {
            if (grades.isEmpty()) continue
            val avg = grades.average()
            val coef = coefficients[subject] ?: 1.0
            weightedSum += avg * coef
            totalCoef += coef
        }
        return if (totalCoef == 0.0) null else weightedSum / totalCoef
    }

    fun subjectAverage(grades: List<Double>): Double? =
        if (grades.isEmpty()) null else grades.average()

    fun isValidGrade(v: Double): Boolean = v in 0.0..20.0

    /** Official-style mention scale (pass mark 10/20). */
    fun mention(avg: Double): String = when {
        avg >= 18 -> "Excellent"
        avg >= 16 -> "Très bien"
        avg >= 14 -> "Bien"
        avg >= 12 -> "Assez bien"
        avg >= 10 -> "Passable"
        else -> "Insuffisant"
    }

    fun mentionAr(avg: Double): String = when {
        avg >= 18 -> "ممتاز"
        avg >= 16 -> "جيد جداً"
        avg >= 14 -> "جيد"
        avg >= 12 -> "حسن"
        avg >= 10 -> "مقبول"
        else -> "غير كافٍ"
    }
}
