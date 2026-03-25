package com.jamessaboia.budgetflow.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jamessaboia.budgetflow.domain.model.BudgetGroup

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val groupType: BudgetGroup,
    val budgetLimit: Double?,
    val isDefault: Boolean
)
