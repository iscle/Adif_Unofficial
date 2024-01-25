package me.iscle.adifunofficial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecentDepartureCard(
    stationName: String,
) {
    OutlinedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Salidas",
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(text = "De $stationName")
            }

            // icon to indicate that this can be accessed
            Icon(
                imageVector = Icons.Filled.NavigateNext,
                contentDescription = "Navegar",
                modifier = Modifier.padding(start = 8.dp),

                )
        }
    }
}

@Preview
@Composable
fun RecentDepartureCardPreview() {
    RecentDepartureCard("Madrid")
}