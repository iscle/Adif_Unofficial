package me.iscle.adifunofficial.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecentArrivalCard(
    modifier: Modifier = Modifier,
    stationName: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Llegadas",
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(text = "A $stationName")
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
fun RecentArrivalCardPreview() {
    RecentArrivalCard(
        stationName = "Madrid",
        onClick = {},
    )
}