package com.jamessaboia.budgetflow.data.local

import androidx.room.TypeConverter
import com.jamessaboia.budgetflow.domain.model.BudgetGroup
import com.jamessaboia.budgetflow.domain.model.TransactionType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromBudgetGroup(value: BudgetGroup): String {
        return value.name
    }

    @TypeConverter
    fun toBudgetGroup(value: String): BudgetGroup {
        return BudgetGroup.valueOf(value)
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}
