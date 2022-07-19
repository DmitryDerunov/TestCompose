package com.example.testcompose.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    val description: String,
    val value: Double,
    val isFavourite: Boolean = false,
    @PrimaryKey val id: Int? = null
)
