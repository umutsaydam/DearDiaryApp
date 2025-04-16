package com.umutsaydam.deardiary.domain.entity

sealed class FontSizeSealed(val id: Int, val label: String) {
    data object HeadlineMedium : FontSizeSealed(0, "HeadlineMedium")
    data object HeadlineSmall : FontSizeSealed(1, "HeadlineSmall")
    data object BodyLarge : FontSizeSealed(2, "BodyLarge")
    data object BodyMedium : FontSizeSealed(3, "BodyMedium")

    companion object {
        val values: List<FontSizeSealed> by lazy {
            listOf(HeadlineMedium, HeadlineSmall, BodyLarge, BodyMedium)
        }

        fun fromLabel(sizeId: Int): FontSizeSealed {
            return values.find { it.id == sizeId } ?: BodyMedium
        }
    }
}