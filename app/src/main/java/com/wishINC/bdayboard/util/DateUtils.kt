package com.wishINC.bdayboard.util

import com.wishINC.bdayboard.data.Birthday
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Data class holding computed display info for a birthday.
 */
data class BirthdayDisplayInfo(
    val birthday: Birthday,
    val formattedDate: String,       // e.g. "14 March"
    val daysUntil: Long,             // 0 = today
    val daysUntilLabel: String,      // "Today \uD83C\uDF82", "Tomorrow", "in 23 days"
    val turningAge: Int?,            // age they will turn on their next birthday, null if no year
    val isToday: Boolean,
    val isWithinWeek: Boolean
)

object DateUtils {

    /**
     * Returns the next occurrence of the given day/month on or after [today].
     * Handles Feb 29 on non-leap years by falling back to Feb 28.
     */
    fun nextOccurrence(day: Int, month: Int, today: LocalDate = java.time.LocalDate.now()): LocalDate {
        val effectiveDay = effectiveDay(day, month, today.year)
        val thisYear = LocalDate.of(today.year, month, effectiveDay)

        return if (!thisYear.isBefore(today)) {
            thisYear
        } else {
            val nextYear = today.year + 1
            val nextEffectiveDay = effectiveDay(day, month, nextYear)
            LocalDate.of(nextYear, month, nextEffectiveDay)
        }
    }

    /**
     * Returns the effective day for a given day/month/year, handling Feb 29 on non-leap years.
     */
    private fun effectiveDay(day: Int, month: Int, year: Int): Int {
        return if (month == 2 && day == 29 && !LocalDate.of(year, 1, 1).isLeapYear) {
            28
        } else {
            day
        }
    }

    /**
     * Calculate number of days from [today] to the next occurrence of this birthday.
     */
    fun daysUntil(day: Int, month: Int, today: LocalDate = java.time.LocalDate.now()): Long {
        val next = nextOccurrence(day, month, today)
        return ChronoUnit.DAYS.between(today, next)
    }

    /**
     * The age the person will turn on their next birthday.
     * Returns null if birthYear is null.
     */
    fun turningAge(day: Int, month: Int, birthYear: Int?, today: LocalDate = java.time.LocalDate.now()): Int? {
        if (birthYear == null) return null
        val next = nextOccurrence(day, month, today)
        return next.year - birthYear
    }

    /**
     * Format a day/month as a readable string like "14 March".
     */
    fun formatDate(day: Int, month: Int): String {
        // Use a fixed date just for formatting the month name
        val date = LocalDate.of(2000, month, day.coerceAtMost(28)) // safe date for formatting
        val monthName = date.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        return "$day $monthName"
    }

    /**
     * Generate a human-readable label for the days until count.
     */
    fun daysUntilLabel(daysUntil: Long): String {
        return when (daysUntil) {
            0L -> "Today \uD83C\uDF82"  // 🎂
            1L -> "Tomorrow"
            else -> "in $daysUntil days"
        }
    }

    /**
     * Compute full display info for a birthday entry.
     */
    fun computeDisplayInfo(birthday: Birthday, today: LocalDate = java.time.LocalDate.now()): BirthdayDisplayInfo {
        val days = daysUntil(birthday.day, birthday.month, today)
        return BirthdayDisplayInfo(
            birthday = birthday,
            formattedDate = formatDate(birthday.day, birthday.month),
            daysUntil = days,
            daysUntilLabel = daysUntilLabel(days),
            turningAge = turningAge(birthday.day, birthday.month, birthday.year, today),
            isToday = days == 0L,
            isWithinWeek = days in 1..7
        )
    }

    /**
     * Sort birthdays by nearest upcoming first (today = 0 days = first).
     */
    fun sortByUpcoming(birthdays: List<Birthday>, today: LocalDate = java.time.LocalDate.now()): List<BirthdayDisplayInfo> {
        return birthdays
            .map { computeDisplayInfo(it, today) }
            .sortedBy { it.daysUntil }
    }
}
