package com.ilyasov.sci_king.util

interface OnTaskCompleted {
    fun onTaskCompleted()
    fun onTaskCompleted(result: Boolean)
}