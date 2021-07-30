package com.ilyasov.sci_king.util

import android.view.View

fun View.isVisible(visibility: Boolean) =
    if (visibility) this.visibility = View.VISIBLE else this.visibility = View.GONE