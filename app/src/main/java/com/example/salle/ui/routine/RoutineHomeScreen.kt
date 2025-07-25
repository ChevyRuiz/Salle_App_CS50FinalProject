package com.example.salle.ui.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.salle.R
import com.example.salle.ui.theme.AppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.salle.data.model.RoutineWithExercises
import com.example.salle.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineScreen(
    viewModel: RoutineHomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onPlayClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val routineHomeUiState by viewModel.routineHomeScreenUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Routines",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        RoutineScreenBody(
            routinesWithExercisesList = routineHomeUiState.routinesWithExercisesList,
            onPlayClick = onPlayClick,
            onEditClick = onEditClick,
            onDeleteClick = {
                coroutineScope.launch {
                    viewModel.deleteRoutineAndExercises(it)
                }
            },
            contentPadding = innerPadding,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
fun RoutineScreenBody(
    routinesWithExercisesList: List<RoutineWithExercises>,
    onPlayClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        if (routinesWithExercisesList.isEmpty()) {
            Text(
                text = "No routines",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            RoutineList(
                routinesWithExercises = routinesWithExercisesList,
                onPlayClick = onPlayClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun RoutineList(
    routinesWithExercises: List<RoutineWithExercises>,
    onPlayClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = routinesWithExercises, key = { it.routine.id }) { routineWithExercises ->
            RoutineWithExercisesItem(
                routineWithExercises = routineWithExercises,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                onPlayClick = onPlayClick,
                onEditClick = onEditClick,
                onDeleteClick = { onDeleteClick(routineWithExercises.routine.id) }
            )
        }
    }
}

@Composable
fun RoutineWithExercisesItem(
    routineWithExercises: RoutineWithExercises,
    onPlayClick: (Int) -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = routineWithExercises.routine.name,
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.weight(3f))
                Text(
                    text ="sets",
                    style = MaterialTheme.typography.labelMedium ,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.weight(0.2f))
                Text(
                    text ="reps",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.weight(0.2f))
                Text(
                    text ="weight",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }

            routineWithExercises.exercises.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(2f)
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = it.sets.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.weight(0.2f))
                    Text(
                        text = it.reps.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.weight(0.2f))
                    Text(
                        text = it.weight.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onPlayClick(routineWithExercises.routine.id)},
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(Modifier.weight(3f))
                FilledTonalButton(
                    onClick = { onEditClick(routineWithExercises.routine.id) },
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(Modifier.weight(0.1f))
                FilledTonalButton(
                    onClick = { deleteConfirmationRequired = true },
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Red
                    )
                }

                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick(routineWithExercises.routine.id)
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Attention") },
        text = { Text("Are you sure that you want to delete this routine?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Yes")
            }
        })
}
@Preview(showBackground = true)
@Composable
fun RoutineScreenPreview() {
    AppTheme (dynamicColor = false){
        RoutineScreen(
            modifier = Modifier.fillMaxSize(),
            onEditClick = {},
            onPlayClick = {}
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RoutineItemPreview() {
//    AppTheme (dynamicColor = false){
//        RoutineItem(
//            routine =
//                Routine(
//                name = "Monday routine",
//                exercises = fakeExercises,
//            ),
//            onEditClick = {},
//            onDeleteClick = {}
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RoutineScreenBodyPreview() {
//    AppTheme (dynamicColor = false) {
//        RoutineScreenBody(
//            routineList = fakeRoutines,
//            onEditClick = {},
//            onDeleteClick = {},
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun RoutineScreenBodyEmptyListPreview() {
//    AppTheme (dynamicColor = false) {
//        RoutineScreenBody(
//            routineList = listOf(),
//            onEditClick = {},
//            onDeleteClick = {},
//        )
//    }
//}



// Fake data
//
//val fakeExercises : List<Exercise> = listOf(
//    Exercise(
//        name = "pull up",
//        reps = 4,
//        sets = 5,
//        restTime = 1,
//        weight = 45
//    ),
//    Exercise(
//        name = "pull up and other thing",
//        reps = 4,
//        sets = 5,
//        restTime = 1,
//        weight = 45
//    )
//)
//
//val fakeRoutines : List<Routine> = listOf(
//    Routine(
//        name = "Tuesday Routine",
//        id = 1,
//        exercises = fakeExercises
//    ),
//    Routine(
//        name = "Wednesday Routine",
//        id = 2,
//        exercises = fakeExercises
//    ),
//    Routine(
//        name = "Friday Routine",
//        id = 3,
//        exercises = fakeExercises
//    ),
//)