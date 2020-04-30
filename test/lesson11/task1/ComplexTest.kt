package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ComplexTest {

    private fun assertApproxEquals(expected: Complex, actual: Complex, eps: Double) {
        assertEquals(expected.re, actual.re, eps)
        assertEquals(expected.im, actual.im, eps)
    }

    @Test
    fun plus() {
        assertApproxEquals(Complex("4-2i"), Complex("1+2i") + Complex("3-4i"), 1e-10)
        assertApproxEquals(Complex("-1+3i"), Complex("5+7i") + Complex("-6-4i"), 1e-10)
        assertApproxEquals(Complex("-10+12i"), Complex("-1+4i") + Complex("-9+8i"), 1e-10)
    }

    @Test
    operator fun unaryMinus() {
        assertApproxEquals(Complex(1.0, -2.0), -Complex(-1.0, 2.0), 1e-10)
        assertApproxEquals(Complex(-4.0, -6.0), -Complex(4.0, 6.0), 1e-10)
        assertApproxEquals(Complex(9.0, 3.0), -Complex(-9.0, -3.0), 1e-10)
    }

    @Test
    fun minus() {
        assertApproxEquals(Complex("-2+6i"), Complex("1+2i") - Complex("3-4i"), 1e-10)
        assertApproxEquals(Complex("-6+1i"), Complex("-9-6i") - Complex("-3-7i"), 1e-10)
        assertApproxEquals(Complex("0+0i"), Complex("5+5i") - Complex("5+5i"), 1e-10)
    }

    @Test
    fun times() {
        assertApproxEquals(Complex("11+2i"), Complex("1+2i") * Complex("3-4i"), 1e-10)
        assertApproxEquals(Complex("11+10i"), Complex("3-2i") * Complex("1+4i"), 1e-10)
        assertApproxEquals(Complex("32+9i"), Complex("11+10i") * Complex("2-1i"), 1e-10)
    }

    @Test
    fun div() {
        assertApproxEquals(Complex("1+1i"), Complex("13+1i") / Complex("7-6i"), 1e-10)
        assertApproxEquals(Complex("2+1i"), Complex("3+4i") / Complex("2+1i"), 1e-10)
        assertApproxEquals(Complex("1+1i"), Complex("13+1i") / Complex("7-6i"), 1e-10)
    }

    @Test
    fun equals() {
        assertApproxEquals(Complex(1.0, 2.0), Complex("1+2i"), 1e-12)
        assertApproxEquals(Complex(1.0, 0.0), Complex(1.0), 1e-12)
        assertApproxEquals(Complex(4.0, -3.0), Complex("4-3i"), 1e-12)
        assertApproxEquals(Complex(5.0, -6.0), Complex(5.0, -6.0), 1e-12)
    }

    @Test
    fun testToString() {
        assertEquals("1+2i", Complex(1.0, 2.0).toString())
        assertEquals("4+0i", Complex(4.0, 0.0).toString())
        assertEquals("6-2i", Complex(6.0, -2.0).toString())
        assertEquals("0+9i", Complex(0.0, 9.0).toString())
        assertEquals("-5-3i", Complex(-5.0, -3.0).toString())
    }
}