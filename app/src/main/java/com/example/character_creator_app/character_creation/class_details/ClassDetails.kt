package com.example.character_creator_app.character_creation.class_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.character_creator_app.R
import com.example.character_creator_app.character_creation.shared_view_model.SharedCharacterViewModel
import data.models.ClassDto

@Composable
fun ClassDetailsScreenRoute(
    className: String,
    onBack: () -> Unit,
    onNavigateToAbilityScore: () -> Unit,
    parentEntry: NavBackStackEntry
) {
    val sharedViewModel: SharedCharacterViewModel = hiltViewModel(parentEntry)

    ClassDetailsScreen(
        className = className,
        onBack = onBack,
        onNext = { details ->
            sharedViewModel.updateClassDetails(details)
            onNavigateToAbilityScore()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailsScreen(
    className: String,
    onBack: () -> Unit,
    onNext: (ClassDto) -> Unit,
    viewModel: ClassDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(className) {
        viewModel.fetchClassDetails(className)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text(stringResource(R.string.class_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_label)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { state.classDetails?.let { details -> onNext(details) } },
                    enabled = state.classDetails != null && !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.next_button_label))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        Text(text = state.errorMessage!!, color = MaterialTheme.colorScheme.error)
                    }
                }

                state.classDetails != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ClassDetailsContent(className, state.classDetails!!)
                    }
                }
            }
        }
    }
}

@Composable
fun ClassDetailsContent(
    className: String,
    classDetails: ClassDto
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = className,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.hit_die_label, classDetails.hitDice ?: ""),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        val detailsList = listOf(
            stringResource(R.string.armor_label) to classDetails.profArmor,
            stringResource(R.string.weapons_label_detail) to classDetails.profWeapons,
            stringResource(R.string.saving_throws_label) to classDetails.profSavingThrows,
            stringResource(R.string.tools_label) to classDetails.profTools
        )

        val noneText = stringResource(R.string.none_label)

        detailsList.forEach { (label, value) ->
            DetailRow(label = label, value = value ?: noneText)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        androidx.compose.material3.HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}