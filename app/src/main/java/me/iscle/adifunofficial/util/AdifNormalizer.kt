package me.iscle.adifunofficial.util

import java.text.Normalizer

object AdifNormalizer {
    fun normalize(value: String): String {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace("[^\\p{ASCII}]".toRegex(), "")
    }
}