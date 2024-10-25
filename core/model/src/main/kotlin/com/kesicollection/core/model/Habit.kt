package com.kesicollection.core.model

data class Habit(
    val id: String,
    val name: String,
    val classification: Classification,
    val status: Status,
)

enum class HabitType {
    CORE, TRIGGER
}