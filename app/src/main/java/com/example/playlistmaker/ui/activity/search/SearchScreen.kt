package com.example.playlistmaker.ui.activity.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.ui.theme.AppTypography
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.SearchState
import com.example.playlistmaker.ui.viewmodel.SearchViewModel


@Composable
fun SearchScreen(
    onBack: () -> Unit,
    modifier: Modifier,
    viewModel: SearchViewModel,
    onNavigateToTrackDetails: (com.example.playlistmaker.data.dto.Track) -> Unit = {}
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val savedSearchText by viewModel.searchText.collectAsState()
    
    var text by remember { mutableStateOf(savedSearchText) }
    
    LaunchedEffect(savedSearchText) {
        if (text != savedSearchText) {
            text = savedSearchText
        }
    }

    LaunchedEffect(text) {
        if (viewModel.searchText.value != text) {
            viewModel.setSearchText(text)
        }
    }

    var searchFieldFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val showHistory = text.isEmpty() && searchHistory.isNotEmpty() && searchFieldFocused

    LaunchedEffect(text, searchFieldFocused, screenState) {
        if (text.isEmpty() && !searchFieldFocused && screenState !is SearchState.Success) {
            viewModel.resetSearchState()
        }
    }

    val searchFieldShape = if (showHistory) {
        RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(8.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        viewModel.resetSearchState()
                        viewModel.setSearchText("")
                        onBack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.search_title),
                fontFamily = AppTypography.YSD_Medium500,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(
                        color = Color(0xFFE6E8EB),
                        shape = searchFieldShape
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFAEAFB4),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                viewModel.search(text)
                                focusRequester.freeFocus()
                                keyboardController?.hide()
                            }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    BasicTextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                        },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 36.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                searchFieldFocused = focusState.isFocused
                                if (!focusState.isFocused && text.isEmpty()) {
                                    viewModel.resetSearchState()
                                }
                            },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF1A1B22),
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFF3772E7)),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (text.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.search_hint),
                                        color = Color(0xFFAEAFB4),
                                        fontFamily = AppTypography.YSD_Regular400,
                                        fontSize = 16.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (text.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.search_clear),
                            tint = Color(0xFFAEAFB4),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    text = ""
                                    viewModel.setSearchText("")
                                    viewModel.resetSearchState()
                                }
                        )
                    }
                }
            }

            if (showHistory) {
                Box {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFE6E8EB),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                    ) {
                        items(searchHistory.size) { index ->
                            HistoryListItem(
                                historyRequest = searchHistory[index],
                                onClick = {
                                    text = searchHistory[index]
                                    viewModel.search(searchHistory[index])
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp),
                        thickness = 1.dp,
                        color = Color(0xFFAEAFB4)
                    )
                }
            }
        }

        when (screenState) {
            is SearchState.Initial -> {
            }

            is SearchState.Searching -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchState.Success -> {
                val tracks = (screenState as SearchState.Success).list
                if (tracks.isNotEmpty()) {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        items(tracks.size) { index ->
                            TrackListItem(
                                track = tracks[index],
                                onClick = {
                                    onNavigateToTrackDetails(tracks[index])
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.nothing_to_show),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                                color = Color(0xFF1A1B22),
                                fontFamily = AppTypography.YSD_Medium400,
                                fontSize = 19.sp
                            )
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.error_with_connection),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(bottom = 16.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.problems_with_connection),
                                color = Color(0xFF1A1B22),
                                fontFamily = AppTypography.YSD_Medium400,
                                fontSize = 19.sp
                            )
                            Text(
                                text = stringResource(R.string.info_problem_with_connection),
                                color = Color(0xFF1A1B22),
                                fontFamily = AppTypography.YSD_Medium400,
                                fontSize = 19.sp,
                                modifier = Modifier
                                    .padding(top = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
