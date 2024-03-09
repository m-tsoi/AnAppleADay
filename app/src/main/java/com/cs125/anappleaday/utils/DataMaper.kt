package com.cs125.anappleaday.utils

fun Any?.toMap(): Map<String, Any?> {
    return this!!.javaClass.declaredFields.associate {
        it.isAccessible = true
        it.name to it.get(this)
    }
}
