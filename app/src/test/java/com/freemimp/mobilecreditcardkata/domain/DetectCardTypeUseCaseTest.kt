package com.freemimp.mobilecreditcardkata.domain

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DetectCardTypeUseCaseTest {
    private val luhnValidationUseCase: LuhnValidationUseCase = mockk()

    private val sut: DetectCardTypeUseCase = DetectCardTypeUseCase(luhnValidationUseCase)

    @ParameterizedTest(name = "given execute is invoked, when card number is {0} and Luhn validation is {1}, then returned card type is {2}")
    @MethodSource("provideTestData")
    fun testCardType(testData: TestData) {
        every { luhnValidationUseCase.execute(any()) } returns testData.isPassingLuhnValidation
        val result = sut.execute(testData.cardNumber)
        assertEquals(testData.expectedCardType, result)
    }

    companion object {
        @JvmStatic
        fun provideTestData() = listOf(
            TestData("411111111", true, CardType.Unknown),            // Invalid less then 13 digits
            TestData("41111111111111111", true, CardType.Unknown),    // Invalid more then 16 digits
            TestData("4111111111111111", true, CardType.Visa),        // Valid Visa 16 digits
            TestData("4111111111111", true, CardType.Visa),           // Valid Visa 13 digits
            TestData("41111111111112", true, CardType.Unknown),       // Invalid Visa digits length
            TestData("41111111111112", false, CardType.Unknown),      // Invalid Visa Luhn validation
            TestData("341111111111111", true, CardType.Amex),         // Valid AMEX 34
            TestData("371111111111111", true, CardType.Amex),         // Valid AMEX 37
            TestData("391111111111111", true, CardType.Unknown),      // Invalid AMEX prefix
            TestData("37111111111112", true, CardType.Unknown),       // Invalid AMEX length TestData("341111111111111", true, CardType.Amex),        // Valid AMEX 34
            TestData("6011111111111111", true, CardType.Discover),    // Valid Discover
            TestData("6012111111111111", true, CardType.Unknown),     // Invalid Discover prefix
            TestData("601111111111121", true, CardType.Unknown),      // Invalid Discover length
            TestData("6011111111111111", false, CardType.Unknown),    // Invalid Discover Luhn validation
            TestData("5111111111111111", true, CardType.Mastercard),  // Valid Mastercard 51
            TestData("5211111111111111", true, CardType.Mastercard),  // Valid Mastercard 52
            TestData("5311111111111111", true, CardType.Mastercard),  // Valid Mastercard 53
            TestData("5411111111111111", true, CardType.Mastercard),  // Valid Mastercard 54
            TestData("5511111111111111", true, CardType.Mastercard),  // Valid Mastercard 55
            TestData("5611111111111111", true, CardType.Unknown),     // Invalid Mastercard prefix
            TestData("5111111111111", true, CardType.Unknown),        // Invalid Mastercard length
            TestData("5111111111111110", false, CardType.Unknown)     // Invalid Mastercard Luhn validation
        )
    }

    data class TestData(
        val cardNumber: String,
        val isPassingLuhnValidation: Boolean,
        val expectedCardType: CardType
    )
}