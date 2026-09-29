package com.wishINC.bdayboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wishINC.bdayboard.data.Birthday
import java.time.LocalDate

@Composable
fun AddEditBirthdayDialog(
    birthdayToEdit: Birthday? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, day: Int, month: Int, year: Int?) -> Unit
) {
    val isEdit = birthdayToEdit != null
    var name by remember { mutableStateOf(birthdayToEdit?.name ?: "") }
    var dayText by remember { mutableStateOf(birthdayToEdit?.day?.toString() ?: "") }
    var monthText by remember { mutableStateOf(birthdayToEdit?.month?.toString() ?: "") }
    var yearText by remember { mutableStateOf(birthdayToEdit?.year?.toString() ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (isEdit) "Edit Birthday" else "Add Birthday",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Name") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = if (nameError) {{ Text("Name is required") }} else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Day and Month row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = dayText,
                        onValueChange = {
                            dayText = it.filter { c -> c.isDigit() }.take(2)
                            dateError = false
                        },
                        label = { Text("Day") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = dateError,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = monthText,
                        onValueChange = {
                            monthText = it.filter { c -> c.isDigit() }.take(2)
                            dateError = false
                        },
                        label = { Text("Month") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = dateError,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                if (dateError) {
                    Text(
                        text = "Enter a valid day and month",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Year field (optional)
                OutlinedTextField(
                    value = yearText,
                    onValueChange = {
                        yearText = it.filter { c -> c.isDigit() }.take(4)
                    },
                    label = { Text("Year (optional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val trimmedName = name.trim()
                            if (trimmedName.isBlank()) {
                                nameError = true
                                return@Button
                            }
                            val day = dayText.toIntOrNull()
                            val month = monthText.toIntOrNull()
                            if (day == null || month == null || month !in 1..12 || day !in 1..31) {
                                dateError = true
                                return@Button
                            }
                            // Validate day for the month
                            val maxDay = try {
                                if (month == 2) 29 // allow Feb 29
                                else LocalDate.of(2000, month, 1).lengthOfMonth()
                            } catch (e: Exception) { 31 }
                            if (day > maxDay) {
                                dateError = true
                                return@Button
                            }
                            val year = yearText.toIntOrNull()?.takeIf { it in 1900..LocalDate.now().year }
                            // If year text is not empty but couldn't parse to a valid year, it's an issue
                            // but we treat it as "no year" — the field is optional
                            onSave(trimmedName, day, month, if (yearText.isBlank()) null else year)
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isEdit) "Save" else "Add")
                    }
                }
            }
        }
    }
}
