package com.example.character_creator_app.character_creation.feature_traits

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel

@Composable
fun FeatureTraitRoute(
    parentEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {

    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    FeatureTraitScreen(
        onBack = onBack,
        onNext = onNext,
        sharedViewModel = sharedViewModel
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureTraitScreen(
    onBack: () -> Unit,
    onNext: () -> Unit,
    sharedViewModel: SharedCharacterViewModel,
) {
    val characterState by sharedViewModel.characterState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feature_traits_label)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button_label))
                    }
                }
            )
        },
        bottomBar = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.next_button_label))
                }
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = stringResource(R.string.feature_traits_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = characterState.featureTrait,
                onValueChange = { newValue ->
                    sharedViewModel.updateFeatureTrait(newValue)
                },
                label = { Text(stringResource(R.string.feature_traits_field_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 8.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                ),
                singleLine = false,
                placeholder = { Text(stringResource(R.string.feature_traits_field_placeholder)) }
            )
        }
    }
}



//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun FeatureTraitPrev() {
//    FeatureTraitScreen(
////        sharedViewModel =
//        onBack = {},
//        onNext = {}
//    )
//}


//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true)
//@Composable
//private fun FeatureTraitPrev() {
//    FeatureTraitScreen(
////        sharedViewModel =
//        onBack = {},
//        onNext = {}
//    )
//}