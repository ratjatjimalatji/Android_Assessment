package com.glucode.about_you.engineers.models

import android.os.Parcelable


abstract class Question(
    val questionText: String,
    val answerOptions: List<String>,
    val questionType: String,//TODO - Is this still necessary?
    val answer: Answer
){
    class One(answer: Answer): Question(
        questionText = "When do you have the most energy?",
        answerOptions = listOf(
            "6am",
            "12pm",
            "6pm",
            "Midnight"
        ),
        questionType = "SELECTION",
        answer = answer
    )
    class Two(answer: Answer): Question(
        questionText = "How old were you when you wrote your first line of code?",
        answerOptions = listOf(
            "Younger than 10 years",
            "10 to 15 years old",
            "16 to 20 years old",
            "21 to 25 years old",
            "26 years old or older"
        ),
        questionType = "SELECTION",
        answer = answer
    )
    class Three(answer: Answer): Question(
        questionText = "Which new programming language would you want to learn?",
        answerOptions = listOf(
            "Python",
            "Kotlin",
            "Swift",
            "Ruby",
            "C#",
            "C++",
            "Rust",
            "None"
        ),
        questionType = "SELECTION",
        answer = answer
    )
    class Four(answer: Answer): Question(
        questionText = "How often do you learn a new framework or language?",
        answerOptions = listOf(
            "Every few months",
            "Once a year",
            "Once every few years"
        ),
        questionType = "SELECTION",
        answer = answer
    )
    class Five(answer: Answer): Question(
        questionText = "What do you do when you get stuck on a problem?",
        answerOptions = listOf(
            "Visit Stack Overflow",
            "Do other work and come back later",
            "Call a coworker or friend",
            "Watch or read a tutorial",
            "Go down a google rabbit hole",
            "Panic"
        ),
        questionType = "SELECTION",
        answer = answer
    )
}