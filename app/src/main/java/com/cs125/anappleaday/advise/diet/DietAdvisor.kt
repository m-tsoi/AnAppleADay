package com.cs125.anappleaday.advise.diet

import com.cs125.anappleaday.api.EdamamApiServices
import com.cs125.anappleaday.data.enumTypes.MealType
import com.cs125.anappleaday.data.enumTypes.NutritionData
import com.cs125.anappleaday.data.record.models.healthPlans.DietPlan
import com.cs125.anappleaday.data.record.models.live.RecommendedMeal
import com.cs125.anappleaday.utils.isInRange

class DietAdvisor(
    private var dietPlan: DietPlan,
) {
    fun computeDailyScore(dailyInputNutritionList: MutableList<NutritionData>): Double {
        var initialScore = DAYLY_SCORE

        val stats = getMacroNuStats(dailyInputNutritionList)

        if (!isInRange(stats["protein%"]!!,dietPlan.dayProteinIntake - MACRONU_TOLERANCE_RATE,
                dietPlan.dayProteinIntake + MACRONU_TOLERANCE_RATE ))
            initialScore -= 25.0

        if (!isInRange(stats["protein%"]!!,dietPlan.dayCarbIntake - MACRONU_TOLERANCE_RATE,
                dietPlan.dayCarbIntake + MACRONU_TOLERANCE_RATE ))
            initialScore -= 25.0

        if (!isInRange(stats["protein%"]!!,dietPlan.dayFatIntake - MACRONU_TOLERANCE_RATE,
                dietPlan.dayFatIntake + MACRONU_TOLERANCE_RATE ))
            initialScore -= 25.0

        if (!isInRange(stats["protein%"]!!,dietPlan.dayCaloriesIntake - CALORIES_TOLERANCE_RATE,
                dietPlan.dayCaloriesIntake + CALORIES_TOLERANCE_RATE ))
            initialScore -= 25.0


        return initialScore
    }

    // TODO: may add mealType field to Nutrition
    fun checkMealAndRecommend(nutritionData: NutritionData): MutableList<RecommendedMeal> {
        val mealCalories = nutritionData.calories

        if (mealCalories < BREAKFAST_CALORIES_RATE * dietPlan.dayCaloriesIntake) {
            return recommendMeal(nutritionData.name,MealType.BREAKFAST)
        } else if (mealCalories < LUNCH_CALORIES_RATE * dietPlan.dayCaloriesIntake) {
            return recommendMeal(nutritionData.name,MealType.LUNCH)
        } else if (mealCalories < DINNER_CALORIES_RATE * dietPlan.dayCaloriesIntake) {
            return recommendMeal(nutritionData.name,MealType.DINNER)
        }

        return mutableListOf()
    }

    fun getMacroNuStats(dailyInputNutritionList: MutableList<NutritionData>): HashMap<String, Double> {
        var protein = 0.0
        var carb = 0.0
        var fat = 0.0
        var calories = 0.0

        for (nutritonData in dailyInputNutritionList)  {
            protein += nutritonData.proteinG
            carb += nutritonData.carbohydratesTotalG
            fat += nutritonData.fatTotalG
            calories += nutritonData.calories
        }

        val totalMacronytrients = protein + carb + fat

        return hashMapOf(
            "protein%" to protein / totalMacronytrients,
            "carb%" to carb / totalMacronytrients,
            "fat%" to fat / totalMacronytrients,
            "calories" to calories,
        )
    }

    fun recommendMeal(q: String, mealType: MealType): MutableList<RecommendedMeal> {
        var mealRate = when(mealType) {
            MealType.BREAKFAST -> BREAKFAST_CALORIES_RATE
            MealType.LUNCH -> LUNCH_CALORIES_RATE
            MealType.DINNER -> DINNER_CALORIES_RATE
        }

        val mealCalories = mealRate * dietPlan.dayCaloriesIntake
        val meals = EdamamApiServices.recommendRecipes(q, calories = mealCalories, mealType)
        return meals
    }

    companion object {
        private val DAYLY_SCORE: Double = 100.0
        private val MACRONU_TOLERANCE_RATE: Double = 0.025
        private val CALORIES_TOLERANCE_RATE: Double = 0.05
        
        // Average calories for each meal
        // breakfast: 300-400
        // lunch: 500-700
        // dinner: 500-700
        // The following stats are estimation based on
        // src "https://globalnews.ca/news/3615212/this-is-what-your-breakfast-lunch-and-dinner-calories-actually-look-like/"
        private val BREAKFAST_CALORIES_RATE: Double = 0.2
        private val LUNCH_CALORIES_RATE: Double = 0.4
        private val DINNER_CALORIES_RATE: Double = 0.4
    }
}