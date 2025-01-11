package com.glucode.about_you

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glucode.about_you.engineers.models.Answer
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.engineers.models.Question
import com.glucode.about_you.engineers.models.QuickStats

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var engineers: MutableList<Engineer>

    @Before
    fun setUp() {
        engineers = mutableListOf(
            Engineer(
                name = "Alice",
                role = "Software Engineer",
                defaultImageName = "image1.png",
                quickStats = QuickStats(years = 2, coffees = 10, bugs = 5),
                questions = listOf(
                    Question.One(Answer("Morning", 0)),
                    Question.Two(Answer("10 to 15 years old", 1)),
                    Question.Three(Answer("Java", 0)),
                    Question.Four(Answer("Rarely", 2)),
                    Question.Five(Answer("Watch or read a tutorial", 3))
                )
            ),
            Engineer(
                name = "Bob",
                role = "Senior Developer",
                defaultImageName = "image2.png",
                quickStats = QuickStats(years = 5, coffees = 20, bugs = 3),
                questions = listOf(
                    Question.One(Answer("Afternoon", 1)),
                    Question.Two(Answer("15 to 20 years old", 2)),
                    Question.Three(Answer("Kotlin", 1)),
                    Question.Four(Answer("Monthly", 1)),
                    Question.Five(Answer("Build projects", 2))
                )
            ),
            Engineer(
                name = "Charlie",
                role = "Intern",
                defaultImageName = "image3.png",
                quickStats = QuickStats(years = 1, coffees = 15, bugs = 10),
                questions = listOf(
                    Question.One(Answer("Evening", 2)),
                    Question.Two(Answer("5 to 10 years old", 0)),
                    Question.Three(Answer("Python", 2)),
                    Question.Four(Answer("Weekly", 0)),
                    Question.Five(Answer("Ask a mentor", 1))
                )
            )
        )
    }

    //TESTS TO MAKE SURE ENGINEERS ARE SORTED IN ASCENDING ORDER
    @Test
    fun testSortByCoffeesAscending() {
        val sortedEngineers = engineers.sortedBy { it.quickStats.coffees }

        assertEquals("Alice", sortedEngineers[0].name)  // Lowest coffees
        assertEquals("Charlie", sortedEngineers[1].name)
        assertEquals("Bob", sortedEngineers[2].name)   // Highest coffees
    }
    @Test
    fun testSortEmptyList() {
        val emptyEngineers: MutableList<Engineer> = mutableListOf()
        val sortedEngineers = emptyEngineers.sortedBy { it.quickStats.coffees }

        assertEquals(0, sortedEngineers.size)  // Should remain empty
    }

    @Test
    fun testSortByBugsAscending() {
        val sortedEngineers = engineers.sortedBy { it.quickStats.bugs }

        assertEquals("Bob", sortedEngineers[0].name)    // Lowest bugs fixed
        assertEquals("Alice", sortedEngineers[1].name)
        assertEquals("Charlie", sortedEngineers[2].name)  // Highest bugs fixed
    }

    @Test
    fun testSortByYearsAscending() {
        val sortedEngineers = engineers.sortedBy { it.quickStats.years }

        assertEquals("Charlie", sortedEngineers[0].name)  // Least years
        assertEquals("Alice", sortedEngineers[1].name)
        assertEquals("Bob", sortedEngineers[2].name)      // Most years
    }


}
