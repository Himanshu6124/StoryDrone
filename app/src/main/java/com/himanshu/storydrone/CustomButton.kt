package com.himanshu.storydrone

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton(context : Context, attrs: AttributeSet) : AppCompatButton(context,attrs)
{
    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface : Typeface = Typeface.createFromAsset(context.assets,"baloo2-bold.ttf")
        setTypeface(typeface)
    }
}