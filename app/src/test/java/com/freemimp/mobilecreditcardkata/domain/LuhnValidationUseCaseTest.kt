package com.freemimp.mobilecreditcardkata.domain

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
class LuhnValidationUseCaseTest {

    private val sut: LuhnValidationUseCase = LuhnValidationUseCase()

    @ParameterizedTest(name = "given execute is invoked, when card number is {0}, then Luhn validation is {1}")
    @MethodSource("provideTestData")
    fun testLuhnValidation(testData: TestData) {
        val result = sut.execute(testData.cardNumber)
        assertEquals(testData.isValid, result)
    }

    companion object {
        @JvmStatic
        fun provideTestData() = listOf(
            TestData(cardNumber = "5387690880698907", isValid = true),  // Valid Luhn number
            TestData(cardNumber = "49927398716", isValid = true),       // Valid Luhn number
            TestData(cardNumber = "1234567812345670", isValid = true),  // Valid Luhn number
            TestData(cardNumber = "79927398713", isValid = true),       // Valid Luhn number
            TestData(cardNumber = "49927398717", isValid = false),      // Invalid Luhn number
            TestData(cardNumber = "5387690880698900", isValid = false), // Invalid Luhn number
            TestData(cardNumber = "79927398710", isValid = false)       // Invalid Luhn number
        )
    }

    data class TestData(
        val cardNumber: String,
        val isValid: Boolean,
    )
}