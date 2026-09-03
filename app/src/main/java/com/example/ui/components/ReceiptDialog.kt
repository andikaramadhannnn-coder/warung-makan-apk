package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Order
import com.example.util.Formatters

@Composable
fun ReceiptDialog(
    order: Order,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val items = order.parseItems()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("receipt_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sukses",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = "Pembayaran Berhasil",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_receipt_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simulated Thermal Receipt Paper
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFCFBF9))
                        .border(1.dp, Color(0xFFE5DDD5), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Brand Title
                        Text(
                            text = "WARUNG MAKAN NISWA",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = Color(0xFF261D1A),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Masakan Rumahan Enak & Murah",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF65534C),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Jl. Raya Warung Niswa No. 12",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF8C766D),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        DottedDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Transaction Details
                        ReceiptRow(label = "No. Transaksi", value = order.orderNumber)
                        ReceiptRow(label = "Waktu", value = Formatters.formatDateTime(order.timestamp))
                        ReceiptRow(label = "Tipe Pesanan", value = order.orderType)
                        ReceiptRow(label = "Keterangan", value = order.customerInfo.ifBlank { "-" })
                        ReceiptRow(label = "Metode Bayar", value = order.paymentMethod)

                        Spacer(modifier = Modifier.height(8.dp))
                        DottedDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Itemized List
                        items.forEach { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF261D1A),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = Formatters.formatRupiah(item.subtotal),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFF261D1A)
                                    )
                                }
                                Row {
                                    Text(
                                        text = "${item.quantity} x ${Formatters.formatRupiah(item.price)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF78655E)
                                    )
                                    if (item.notes.isNotBlank()) {
                                        Text(
                                            text = " (${item.notes})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        DottedDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        // Totals
                        ReceiptRow(
                            label = "TOTAL",
                            value = Formatters.formatRupiah(order.totalAmount),
                            isBold = true,
                            fontSize = 16.sp
                        )
                        ReceiptRow(
                            label = "Bayar (${order.paymentMethod})",
                            value = Formatters.formatRupiah(order.paidAmount)
                        )
                        ReceiptRow(
                            label = "Kembalian",
                            value = Formatters.formatRupiah(order.changeAmount),
                            isBold = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        DottedDivider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Terima kasih atas kunjungan Anda!",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF261D1A),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Selamat Menikmati Hidangan",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF78655E),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dismiss_receipt_button")
                    ) {
                        Text("Selesai")
                    }

                    Button(
                        onClick = { shareReceiptText(context, order) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("share_receipt_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan",
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text("Bagikan Struk")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    fontSize: androidx.compose.ui.unit.TextUnit = 13.sp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = fontSize,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = if (isBold) Color(0xFF261D1A) else Color(0xFF65534C)
        )
        Text(
            text = value,
            fontSize = fontSize,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            fontFamily = if (isBold) FontFamily.Monospace else FontFamily.Default,
            color = Color(0xFF261D1A)
        )
    }
}

@Composable
private fun DottedDivider() {
    HorizontalDivider(
        color = Color(0xFFD4C8BE),
        thickness = 1.dp
    )
}

private fun shareReceiptText(context: Context, order: Order) {
    val items = order.parseItems()
    val sb = java.lang.StringBuilder()
    sb.append("================================\n")
    sb.append("      WARUNG MAKAN NISWA        \n")
    sb.append("  Masakan Rumahan Enak & Murah  \n")
    sb.append("================================\n")
    sb.append("No: ${order.orderNumber}\n")
    sb.append("Waktu: ${Formatters.formatDateTime(order.timestamp)}\n")
    sb.append("Tipe: ${order.orderType}\n")
    if (order.customerInfo.isNotBlank()) {
        sb.append("Info: ${order.customerInfo}\n")
    }
    sb.append("--------------------------------\n")
    items.forEach { item ->
        val notesStr = if (item.notes.isNotBlank()) " (${item.notes})" else ""
        sb.append("${item.name}$notesStr\n")
        sb.append("  ${item.quantity} x ${Formatters.formatRupiah(item.price)} = ${Formatters.formatRupiah(item.subtotal)}\n")
    }
    sb.append("--------------------------------\n")
    sb.append("TOTAL: ${Formatters.formatRupiah(order.totalAmount)}\n")
    sb.append("Bayar (${order.paymentMethod}): ${Formatters.formatRupiah(order.paidAmount)}\n")
    sb.append("Kembalian: ${Formatters.formatRupiah(order.changeAmount)}\n")
    sb.append("================================\n")
    sb.append("Terima kasih atas kunjungannya!\n")
    sb.append("Selamat Menikmati Hidangan :)\n")

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, sb.toString())
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Bagikan Struk Warung Niswa")
    context.startActivity(shareIntent)
}
