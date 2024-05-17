package com.freemimp.mobilecreditcardkata.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freemimp.mobilecreditcardkata.R
import com.freemimp.mobilecreditcardkata.domain.CardType

@Composable
fun CreditCardScreen(
    viewModel: CardTypeViewModel = viewModel(),
) {
    val state = viewModel.state.collectAsState().value
    CreditCardContent(state = state, uiEvent = viewModel::handleUiEvent)
}

@Composable
fun CreditCardContent(state: CardTypeState, uiEvent: (UiEvent) -> Unit) {
    CreditCardSection(state = state, uiEvent = uiEvent)
}

@Composable
fun CreditCardSection(state: CardTypeState, uiEvent: (UiEvent) -> Unit) {
    var cardNumber by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cardholder Name",

                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "John Doe",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                trailingIcon = {
                    if (state is CardTypeState.Success) {
                        val icon = when (state.cardType) {
                            CardType.Visa -> ImageVector.vectorResource(id = R.drawable.ic_visa_card)
                            CardType.Mastercard -> ImageVector.vectorResource(id = R.drawable.ic_mastercard)
                            CardType.Amex -> ImageVector.vectorResource(id = R.drawable.ic_american_express)
                            CardType.Discover -> ImageVector.vectorResource(id = R.drawable.ic_discover_card)
                            CardType.Unknown -> Icons.Default.Warning
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Card Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Card Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                value = if (state is CardTypeState.Success) {
                    state.cardNumber
                } else {
                    ""
                },
                onValueChange = {
                    if (it.isDigitsOnly()) uiEvent(UiEvent.OnCardNumberChange(it))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = "XXXX XXXX XXXX XXXX") },
                visualTransformation = CreditCardVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Expiry Date",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "12/24",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = "CVV",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "123",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun CreditCardScreenPreview() {
    CreditCardContent(
        CardTypeState.Success(
            cardNumber = "4111111111111111",
            cardType = CardType.Visa
        ), {})
}

class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmedText = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        val formattedText = buildString {
            trimmedText.forEachIndexed { index, char ->
                append(char)
                if ((index + 1) % 4 == 0 && index != trimmedText.lastIndex) append(' ')
            }
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in (1..4) -> offset
                    in (5..8) -> offset + 1
                    in (9..12) -> offset + 2
                    in (13..16) -> offset + 3
                    else -> offset
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in (1..4) -> offset
                    in (5..9) -> offset - 1
                    in (11..14) -> offset - 2
                    in (15..19) -> offset - 3
                    else -> offset
                }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetTranslator)
    }
}