package com.wishINC.bdayboard.util

import com.wishINC.bdayboard.data.Birthday
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class DateUtilsTest {

    // ── nextOccurrence ───────────────────────────────────────────

    @Test
    fun `birthday later this year`() {
        val today = LocalDate.of(2026, 3, 1)
        val next = DateUtils.nextOccurrence(day = 14, month = 6, today = today)
        assertEquals(LocalDate.of(2026, 6, 14), next)
    }

    @Test
    fun `birthday today returns today`() {
        val today = LocalDate.of(2026, 6, 14)
        val next = DateUtils.nextOccurrence(day = 14, month = 6, today = today)
        assertEquals(LocalDate.of(2026, 6, 14), next)
    }

    @Test
    fun `birthday already passed this year rolls to next year`() {
        val today = LocalDate.of(2026, 7, 1)
        val next = DateUtils.nextOccurrence(day = 14, month = 6, today = today)
        assertEquals(LocalDate.of(2027, 6, 14), next)
    }

    @Test
    fun `feb 29 birthday on leap year stays feb 29`() {
        // 2028 is a leap year
        val today = LocalDate.of(2028, 1, 1)
        val next = DateUtils.nextOccurrence(day = 29, month = 2, today = today)
        assertEquals(LocalDate.of(2028, 2, 29), next)
    }

    @Test
    fun `feb 29 birthday on non-leap year falls back to feb 28`() {
        // 2026 is not a leap year
        val today = LocalDate.of(2026, 1, 1)
        val next = DateUtils.nextOccurrence(day = 29, month = 2, today = today)
        assertEquals(LocalDate.of(2026, 2, 28), next)
    }

    @Test
    fun `feb 29 birthday passed this non-leap year rolls to next year`() {
        // 2026 is not a leap year, and we're past Feb 28
        val today = LocalDate.of(2026, 3, 1)
        val next = DateUtils.nextOccurrence(day = 29, month = 2, today = today)
        // 2027 is also not a leap year
        assertEquals(LocalDate.of(2027, 2, 28), next)
    }

    @Test
    fun `year rollover dec 31 to jan 1`() {
        val today = LocalDate.of(2026, 12, 31)
        val next = DateUtils.nextOccurrence(day = 1, month = 1, today = today)
        assertEquals(LocalDate.of(2027, 1, 1), next)
    }

    // ── daysUntil ─────────────────────────────────────────────────

    @Test
    fun `days until today is zero`() {
        val today = LocalDate.of(2026, 9, 29)
        assertEquals(0L, DateUtils.daysUntil(day = 29, month = 9, today = today))
    }

    @Test
    fun `days until tomorrow is one`() {
        val today = LocalDate.of(2026, 9, 29)
        assertEquals(1L, DateUtils.daysUntil(day = 30, month = 9, today = today))
    }

    @Test
    fun `days until wraps around year`() {
        val today = LocalDate.of(2026, 12, 30)
        assertEquals(2L, DateUtils.daysUntil(day = 1, month = 1, today = today))
    }

    // ── turningAge ───────────────────────────────────────────────

    @Test
    fun `turning age calculated correctly`() {
        val today = LocalDate.of(2026, 1, 1)
        // Birthday March 14, 1990 → next occurrence March 14 2026 → turning 36
        assertEquals(36, DateUtils.turningAge(day = 14, month = 3, birthYear = 1990, today = today))
    }

    @Test
    fun `turning age null when no birth year`() {
        val today = LocalDate.of(2026, 1, 1)
        assertNull(DateUtils.turningAge(day = 14, month = 3, birthYear = null, today = today))
    }

    @Test
    fun `turning age after birthday passed this year`() {
        val today = LocalDate.of(2026, 4, 1)
        // Birthday March 14, 1990 → already passed → next is March 14, 2027 → turning 37
        assertEquals(37, DateUtils.turningAge(day = 14, month = 3, birthYear = 1990, today = today))
    }

    // ── daysUntilLabel ────────────────────────────────────────────

    @Test
    fun `label for today`() {
        assertEquals("Today \uD83C\uDF82", DateUtils.daysUntilLabel(0L))
    }

    @Test
    fun `label for tomorrow`() {
        assertEquals("Tomorrow", DateUtils.daysUntilLabel(1L))
    }

    @Test
    fun `label for multiple days`() {
        assertEquals("in 23 days", DateUtils.daysUntilLabel(23L))
    }

    // ── sortByUpcoming ────────────────────────────────────────────

    @Test
    fun `sort by upcoming puts nearest first`() {
        val today = LocalDate.of(2026, 6, 1)
        val birthdays = listOf(
            Birthday(id = 1, name = "Alice", day = 25, month = 12),   // far away
            Birthday(id = 2, name = "Bob", day = 5, month = 6),       // 4 days
            Birthday(id = 3, name = "Carol", day = 1, month = 6),     // today!
            Birthday(id = 4, name = "Dave", day = 15, month = 3),     // passed, next year
        )
        val sorted = DateUtils.sortByUpcoming(birthdays, today)
        assertEquals(listOf("Carol", "Bob", "Alice", "Dave"), sorted.map { it.birthday.name })
    }

    // ── formatDate ────────────────────────────────────────────────

    @Test
    fun `format date produces readable string`() {
        val result = DateUtils.formatDate(14, 3)
        assertEquals("14 March", result)
    }
}
