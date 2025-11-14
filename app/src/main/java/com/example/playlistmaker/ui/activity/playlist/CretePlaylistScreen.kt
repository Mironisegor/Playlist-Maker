import com.example.playlistmaker.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.font.AppTypography
import com.example.playlistmaker.ui.viewmodel.PlaylistViewModel

@Composable
fun CreatePlaylistScreen(
    onBack: () -> Unit,
    playlistViewModel: PlaylistViewModel
) {
    var nameText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var isNameFocused by remember { mutableStateOf(false) }
    var isDescriptionFocused by remember { mutableStateOf(false) }
    val isCreateEnabled = nameText.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    contentDescription = null,
                    tint = Color(0xFF1A1B22),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onBack)
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(R.string.new_playlist),
                fontFamily = AppTypography.YSD_Medium500,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(136.dp))

        Icon(
            painter = painterResource(id = R.drawable.add_photo_icon),
            contentDescription = null,
            tint = Color(0xFFAEAFB4),
            modifier = Modifier
                .size(80.dp)
        )

        Spacer(modifier = Modifier.height(150.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                label = {
                    Text(
                        "Название*",
                        color = if (isNameFocused) Color(0xFF3772E7) else Color(0xFF1A1B22)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isNameFocused = focusState.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22),
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    cursorColor = Color(0xFF3772E7),
                    focusedIndicatorColor = Color(0xFF3772E7),
                    unfocusedIndicatorColor = Color(0xFFAEAFB4)
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1A1B22),
                    fontFamily = AppTypography.YSD_Regular400
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = {
                    Text(
                        "Описание",
                        color = if (isDescriptionFocused) Color(0xFF3772E7) else Color(0xFF1A1B22)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isDescriptionFocused = focusState.isFocused
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color(0xFF1A1B22),
                    unfocusedTextColor = Color(0xFF1A1B22),
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    cursorColor = Color(0xFF3772E7),
                    focusedIndicatorColor = Color(0xFF3772E7),
                    unfocusedIndicatorColor = Color(0xFFAEAFB4)
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1A1B22),
                    fontFamily = AppTypography.YSD_Regular400
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 17.dp)
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        color = if (isCreateEnabled) Color(0xFF3772E7) else Color(0xFFAEAFB4),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        enabled = isCreateEnabled,
                        onClick = {
                            playlistViewModel.createNewPlayList(nameText, descriptionText)
                            onBack()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "СОЗДАТЬ",
                    fontFamily = AppTypography.YSD_Medium500,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}