package com.umutsaydam.deardiary.domain.entity

import android.util.Log
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.GenericFontFamily

sealed class FontFamilySealed(
    val id: Int,
    val fontName: String,
    val fontFamily: GenericFontFamily
) {
    data object Serif : FontFamilySealed(0, "Serif", FontFamily.Serif)
    data object Cursive : FontFamilySealed(1, "Cursive", FontFamily.Cursive)
    data object MonoSpace : FontFamilySealed(2, "Monospace", FontFamily.Monospace)
    data object SansSerif : FontFamilySealed(3, "SansSerif", FontFamily.SansSerif)

    companion object {
        val fontFamilyList: List<FontFamilySealed> by lazy {
            listOf(Serif, Cursive, MonoSpace, SansSerif)
        }

        fun fromLabel(fontId: Int): FontFamilySealed {
            val result = fontFamilyList.find { it.id == fontId }
            if (result == null) {
                Log.e("FontError", "No FontFamily with id=$fontId found. Returning Serif by default.")
            }
            return result ?: Serif
        }

    }
}

