package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.util.Formatters

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PaymentDialog(
    totalAmount: Long,
    onDismiss: () -> Unit,
    onConfirmPayment: (paidAmount: Long, paymentMethod: String) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("Tunai") } // "Tunai", "QRIS", "Transfer"
    var cashInput by remember { mutableStateOf(totalAmount.toString()) }

    val paidAmount = if (selectedMethod == "Tunai") {
        cashInput.toLongOrNull() ?: 0L
    } else {
        totalAmount
    }

    val isAmountSufficient = paidAmount >= totalAmount
    val changeAmount = if (isAmountSufficient) paidAmount - totalAmount else 0L
    val deficitAmount = if (!isAmountSufficient) totalAmount - paidAmount else 0L

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("payment_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pembayaran",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Pilih metode pembayaran kasir",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Batal")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Total Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TOTAL TAGIHAN",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = Formatters.formatRupiah(totalAmount),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Method Selector
                Text(
                    text = "Metode Pembayaran",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMethodTab(
                        title = "Tunai",
                        icon = Icons.Default.Money,
                        isSelected = selectedMethod == "Tunai",
                        onClick = {
                            selectedMethod = "Tunai"
                            if (cashInput.isBlank() || (cashInput.toLongOrNull() ?: 0) < totalAmount) {
                                cashInput = totalAmount.toString()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodTab(
                        title = "QRIS",
                        icon = Icons.Default.QrCode,
                        isSelected = selectedMethod == "QRIS",
                        onClick = { selectedMethod = "QRIS" },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodTab(
                        title = "Transfer",
                        icon = Icons.Default.AccountBalance,
                        isSelected = selectedMethod == "Transfer",
                        onClick = { selectedMethod = "Transfer" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Details based on method
                if (selectedMethod == "Tunai") {
                    Text(
                        text = "Uang Tunai Diterima (Rp)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = cashInput,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() }
                            cashInput = filtered
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        placeholder = { Text("0") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cash_input_field"),
                        textStyle = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Cash Chips
                    val quickOptions = calculateQuickOptions(totalAmount)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickOptions.forEach { amount ->
                            val label = if (amount == totalAmount) "Uang Pas" else Formatters.formatRupiah(amount)
                            FilterChip(
                                selected = paidAmount == amount,
                                onClick = { cashInput = amount.toString() },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Change / Deficit Calculation Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isAmountSufficient) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                            )
                            .border(
                                1.dp,
                                if (isAmountSufficient) Color(0xFF86EFAC) else Color(0xFFFCA5A5),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAmountSufficient) "Kembalian" else "Uang Kurang",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isAmountSufficient) Color(0xFF166534) else Color(0xFF991B1B)
                            )
                            Text(
                                text = if (isAmountSufficient) {
                                    Formatters.formatRupiah(changeAmount)
                                } else {
                                    "- ${Formatters.formatRupiah(deficitAmount)}"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = if (isAmountSufficient) Color(0xFF166534) else Color(0xFF991B1B)
                            )
                        }
                    }
                } else if (selectedMethod == "QRIS") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QRIS",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            Text(
                                text = "Scan QRIS Warung Makan Niswa",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "NMID: ID1020304050607 | Niswa Food",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pastikan pelanggan sudah scan dan status bayar berhasil di aplikasi e-wallet.",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Rekening Transfer Warung Niswa:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• BCA: 873-092-1244 (an. Niswa)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "• BRI: 0192-01-098273-50-1 (an. Niswa)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }

                    Button(
                        onClick = {
                            onConfirmPayment(paidAmount, selectedMethod)
                        },
                        enabled = isAmountSufficient,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_payment_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Bayar & Struk")
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodTab(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2D5CE)
    val bgColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun calculateQuickOptions(total: Long): List<Long> {
    val list = mutableListOf<Long>()
    list.add(total) // Exact amount

    val roundingSteps = listOf(10000L, 20000L, 50000L, 100000L, 150000L, 200000L)
    for (step in roundingSteps) {
        if (step > total && !list.contains(step)) {
            list.add(step)
        }
    }
    // Also include next 10k round if not in list
    val nextRound = ((total / 10000L) + 1) * 10000L
    if (nextRound > total && !list.contains(nextRound)) {
        list.add(nextRound)
    }

    return list.sorted().take(5)
}
