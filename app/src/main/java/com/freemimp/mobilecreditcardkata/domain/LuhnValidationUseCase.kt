package com.freemimp.mobilecreditcardkata.domain

import javax.inject.Inject

class LuhnValidationUseCase @Inject constructor() {
    fun execute(cardNumber: String): Boolean {
        val reversedDigits = cardNumber.reversed().map { it.toString().toInt() }
        val checksum = reversedDigits.mapIndexed { index, digit ->
            if (index % 2 == 1) {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            } else {
                digit
            }
        }.sum()
        return checksum % 10 == 0
    }
}