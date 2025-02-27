package com.example.furiyomi.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.furiyomi.ui.theme.FuriyomiTheme
import com.example.furiyomi.ui.theme.White80

@Composable
fun ButtonStart(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = FuriyomiTheme.colorScheme.primary,
            contentColor = FuriyomiTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play Icon",
                tint = White80,
                modifier = Modifier.offset(x = (-6.dp))
            )

            Text(
                text = label,
                style = FuriyomiTheme.typography.labelNormal,
                fontWeight = FontWeight.Medium,
                color = White80,
                modifier = Modifier.offset(x = (-2.dp))

            )
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, backgroundColor = 0xFFFFFF)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewPrimaryButton() {
    FuriyomiTheme {
        ButtonStart (
            label = "Start",
            onClick = {}
        )
    }
}