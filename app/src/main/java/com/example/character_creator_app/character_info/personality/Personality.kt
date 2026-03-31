package com.example.character_creator_app.character_info.personality

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.character_creator_app.R
import data.local.entity.CharacterEntity


@Composable
fun PersonalityTabContent(
    character: CharacterEntity
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PersonalityCard(
            label = stringResource(R.string.personality_traits_label),
            value = character.personalTraits.ifEmpty { stringResource(R.string.personality_traits_placeholder) }
        )

        PersonalityCard(
            label = stringResource(R.string.personality_ideals_label),
            value = character.ideals.ifEmpty { stringResource(R.string.personality_ideals_placeholder) }
        )

        PersonalityCard(
            label = stringResource(R.string.personality_bonds_label),
            value = character.bonds.ifEmpty { stringResource(R.string.personality_bonds_placeholder) }
        )

        PersonalityCard(
            label = stringResource(R.string.personality_flaws_label),
            value = character.flaws.ifEmpty { stringResource(R.string.personality_flaws_placeholder) }
        )
    }
}

@Composable
fun PersonalityCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                lineHeight = 20.sp
            )
        }
    }
}
