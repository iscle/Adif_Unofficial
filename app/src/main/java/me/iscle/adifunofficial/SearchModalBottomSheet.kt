package me.iscle.adifunofficial

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "SearchModalBottomSheet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchModalBottomSheet(
    onDismissRequest: () -> Unit,
    defaultQuery: String = "",
) {
    val density = LocalDensity.current
    val sheetState = remember(density) {
        SheetState(
            skipPartiallyExpanded = true,
            density = density,
        )
    }

    val view = LocalView.current
    val insets = remember(view) { ViewCompat.getRootWindowInsets(view) }
    val systemBarsInsets = remember(insets) {
        insets?.getInsets(WindowInsetsCompat.Type.systemBars())?.let {
            WindowInsets(
                left = it.left,
                top = it.top,
                right = it.right,
                bottom = it.bottom,
            )
        }
    }
    LaunchedEffect(systemBarsInsets) {
        Log.d(TAG, "SearchModalBottomSheet: systemBarsInsets = $systemBarsInsets")
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        windowInsets = systemBarsInsets ?: WindowInsets.systemBars,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            var query by remember(defaultQuery) { mutableStateOf(defaultQuery) }
            val focusRequester = remember { FocusRequester() }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(focusRequester),
                label = { Text(text = "Buscar") },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(100) {
                    Text(text = "Item $it")
                }
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Preview
@Composable
fun SearchModalBottomSheetPreview() {
    SearchModalBottomSheet(
        onDismissRequest = {},
        defaultQuery = "Madrid",
    )
}