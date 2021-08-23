package com.ilyasov.sci_king.custom

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ilyasov.sci_king.R

class CustomEditTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var hint: String = ""
        set(value) {
            field = value
            findViewById<EditText>(R.id.edtCustomView).hint = value
        }

    private var errorAction = {}

    var text: String = ""
        get() = findViewById<EditText>(R.id.edtCustomView).text.toString()
        set(value) {
            field = value
            findViewById<EditText>(R.id.edtCustomView).text =
                Editable.Factory.getInstance().newEditable(value)
        }

    private var searchImg: Int = 0
        set(value) {
            field = value
            findViewById<AppCompatImageView>(R.id.imgCustomView).setImageResource(value)
        }

    fun setActionOnClick(v: () -> Unit) = v.invoke()

    fun showError() = errorAction.invoke()

    init {
        View.inflate(context, R.layout.custom_edit_text_view, this)

        with(context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditTextView, 0, 0)) {
            hint = getString(R.styleable.CustomEditTextView_editHint) ?: ""
            text = getString(R.styleable.CustomEditTextView_getText) ?: ""
            searchImg = getResourceId(R.styleable.CustomEditTextView_img, R.drawable.ic_search_24)
        }
    }
}