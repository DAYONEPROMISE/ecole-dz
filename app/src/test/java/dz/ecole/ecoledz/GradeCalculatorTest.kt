package dz.ecole.ecoledz

import org.junit.Assert.*
import org.junit.Test

class GradeCalculatorTest {
    @Test fun `empty grades return null`() {
        assertNull(GradeCalculator.generalAverage(emptyMap()))
        assertNull(GradeCalculator.subjectAverage(emptyList()))
    }

    @Test fun `primary simple mean`() {
        val avg = GradeCalculator.generalAverage(mapOf("Maths" to listOf(12.0), "Arabe" to listOf(16.0), "Français" to listOf(14.0)))
        assertNotNull(avg)
        assertEquals(14.0, avg!!, 0.001)
    }

    @Test fun `weighted coefficients`() {
        val avg = GradeCalculator.generalAverage(
            mapOf("Maths" to listOf(10.0), "Arabe" to listOf(20.0)),
            mapOf("Maths" to 2.0, "Arabe" to 1.0)
        )
        assertEquals((10.0 * 2 + 20.0) / 3.0, avg!!, 0.001)
    }

    @Test fun `grade bounds`() {
        assertTrue(GradeCalculator.isValidGrade(0.0))
        assertTrue(GradeCalculator.isValidGrade(20.0))
        assertFalse(GradeCalculator.isValidGrade(20.5))
        assertFalse(GradeCalculator.isValidGrade(-1.0))
    }

    @Test fun `mentions follow 10 pass mark`() {
        assertEquals("Insuffisant", GradeCalculator.mention(9.99))
        assertEquals("Passable", GradeCalculator.mention(10.0))
        assertEquals("Assez bien", GradeCalculator.mention(12.0))
        assertEquals("Bien", GradeCalculator.mention(14.0))
        assertEquals("Très bien", GradeCalculator.mention(16.0))
        assertEquals("Excellent", GradeCalculator.mention(18.5))
    }

    @Test fun `arabic mentions`() {
        assertEquals("غير كافٍ", GradeCalculator.mentionAr(5.0))
        assertEquals("ممتاز", GradeCalculator.mentionAr(19.0))
    }

    @Test fun `multiple grades per subject averaged first`() {
        val avg = GradeCalculator.generalAverage(mapOf("Maths" to listOf(10.0, 14.0)))
        assertEquals(12.0, avg!!, 0.001)
    }
}
