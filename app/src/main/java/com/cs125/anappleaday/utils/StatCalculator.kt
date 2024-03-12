package com.cs125.anappleaday.utils

import com.cs125.anappleaday.data.enumTypes.ActivityLevel
import com.cs125.anappleaday.data.enumTypes.Gender
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

class StatCalculator {
    companion object {
        fun computeBMI(height: Double, weight: Double): Double {
            return BigDecimal(weight * 703 / height.pow(2))
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
        }

        fun computeRMR(age: Int, gender: Gender, height: Double, weight: Double): Double {
            return if (gender == Gender.FEMALE) {
                BigDecimal(655 + 4.35 * weight + 4.7 * height - 4.7 * age)
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
            } else {
                BigDecimal(66 + 4.35 * weight + 12.7 * height - 6.8 * age)
                    .setScale(1, RoundingMode.HALF_UP)
                    .toDouble()
            }
        }

        fun computeCaloriesBudget(rmr: Double, activityLevel: ActivityLevel): Double {
            return BigDecimal(rmr * activityLevel.value)
                .setScale(2, RoundingMode.HALF_UP).toDouble()
        }
    }
}