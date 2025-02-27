package com.example.furiyomi.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.Blue80
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White100

@Composable
fun BadgeCount(numberOfChapters: Int) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .background(Blue80, shape = RoundedCornerShape(4.dp))
    ) {
        Text(
            text = numberOfChapters.toString(),
            style = FuriyomiTheme.typography.labelSmall,
            color = White100,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, backgroundColor = 0xFFFFFF)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0x000000)
@Composable
private fun PreviewBadgeCount() {
    FuriyomiTheme {
        BadgeCount(165)
    }
}