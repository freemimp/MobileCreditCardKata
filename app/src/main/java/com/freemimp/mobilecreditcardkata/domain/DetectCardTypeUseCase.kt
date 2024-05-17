package com.freemimp.mobilecreditcardkata.domain

import javax.inject.Inject

class DetectCardTypeUseCase @Inject constructor(private val luhnValidationUseCase: LuhnValidationUseCase) {

    fun execute(cardNumber: String): CardType {
        return if (cardNumber.length in 13..16) {
            when {
                cardNumber.startsWith("4")
                        && (cardNumber.length == 16 || cardNumber.length == 13)
                        && luhnValidationUseCase.execute(cardNumber) -> CardType.Visa

                (cardNumber.startsWith("51")
                        || cardNumber.startsWith("52")
                        || cardNumber.startsWith("53")
                        || cardNumber.startsWith("54")
                        || cardNumber.startsWith("55"))
                        && (cardNumber.length == 16)
                        && luhnValidationUseCase.execute(cardNumber) -> CardType.Mastercard

                (cardNumber.startsWith("34") || cardNumber.startsWith("37"))
                        && cardNumber.length == 15
                        && luhnValidationUseCase.execute(cardNumber) -> CardType.Amex

                cardNumber.startsWith("6011")
                        && cardNumber.length == 16
                        && luhnValidationUseCase.execute(cardNumber) -> CardType.Discover

                else -> CardType.Unknown
            }
        } else {
            CardType.Unknown
        }
    }
}
