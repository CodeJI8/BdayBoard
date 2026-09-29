package com.wishINC.bdayboard.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wishINC.bdayboard.ui.theme.NextBirthdayCardDark
import com.wishINC.bdayboard.ui.theme.NextBirthdayCardLight
import com.wishINC.bdayboard.util.BirthdayDisplayInfo

@Composable
fun NextBirthdayCard(
    info: BirthdayDisplayInfo,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isSystemInDarkTheme()) NextBirthdayCardDark else NextBirthdayCardLight

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = if (info.isToday) "\uD83C\uDF89 Birthday Today!" else "\uD83C\uDF82 Next Birthday",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info.birthday.name,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = info.daysUntilLabel,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            if (info.turningAge != null) {
                Text(
                    text = "Turning ${info.turningAge}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}
