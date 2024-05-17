package com.freemimp.mobilecreditcardkata.ui

import app.cash.turbine.test
import com.freemimp.mobilecreditcardkata.domain.CardType
import com.freemimp.mobilecreditcardkata.domain.DetectCardTypeUseCase
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CardTypeViewModelTest {
    private val detectCardTypeUseCase: DetectCardTypeUseCase = mockk()

    private val sut = CardTypeViewModel(detectCardTypeUseCase)

    @Test
    fun `given viewmodel created, when UiEvent is OnCardNumberChange with card number, then new state is Success with card type and card number`() {
        runTest {
            val cardNumber = "1234567890123456"
            coEvery { detectCardTypeUseCase.execute(any()) } returns CardType.Visa

            sut.handleUiEvent(UiEvent.OnCardNumberChange(cardNumber))

            sut.state.test {
                assertEquals(CardTypeState.Success(cardNumber, CardType.Visa), awaitItem())
            }
        }
    }
}