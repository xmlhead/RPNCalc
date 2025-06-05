import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.PI

class RPNCalculatorTest {
    private fun eval(expr: String): String {
        val calc = RPNCalculator()
        return calc.evaluateInput(expr)
    }

    private fun evalDouble(expr: String): Double? = eval(expr).toDoubleOrNull()

    @Test
    fun testAdd() {
        assertEquals(5.0, evalDouble("2 3 +"))
    }

    @Test
    fun testSub() {
        assertEquals(-1.0, evalDouble("2 3 -"))
    }

    @Test
    fun testMul() {
        assertEquals(6.0, evalDouble("2 3 *"))
    }

    @Test
    fun testDiv() {
        assertEquals(2.0, evalDouble("6 3 /"))
    }

    @Test
    fun testDivByZero() {
        assertNull(evalDouble("1 0 /"))
    }

    @Test
    fun testSqr() {
        assertEquals(9.0, evalDouble("3 sqr"))
    }

    @Test
    fun testExp() {
        val result = evalDouble("0 exp")
        assertNotNull(result)
        result?.let { assertEquals(1.0, it, 1e-9) }
    }

    @Test
    fun testSin() {
        val result = evalDouble("${PI/2} sin")
        assertNotNull(result)
        result?.let { assertEquals(1.0, it, 1e-9) }
    }

    @Test
    fun testCos() {
        val result = evalDouble("0 cos")
        assertNotNull(result)
        result?.let { assertEquals(1.0, it, 1e-9) }
    }

    @Test
    fun testPow() {
        assertEquals(8.0, evalDouble("2 3 pow"))
    }

    @Test
    fun testPowZero() {
        assertEquals(1.0, evalDouble("5 0 pow"))
    }
}
