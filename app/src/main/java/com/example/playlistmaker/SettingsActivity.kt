package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistMakerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen(onBack = { finish() })
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val isDark = remember { mutableStateOf(false) }

    val shareMessage = stringResource(R.string.share_message, context.packageName)
    val shareChooserTitle = stringResource(R.string.share_chooser_title)

    val devEmail = stringResource(R.string.dev_email)
    val emailSubject = stringResource(R.string.email_subject)
    val emailBody = stringResource(R.string.email_body)
    val emailChooserTitle = stringResource(R.string.email_chooser_title)

    val userAgreementLink = stringResource(R.string.user_agreement_link)

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
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = null,
                tint = Color(0xFF1A1B22),
                modifier = Modifier
                    .size(16.dp)
                    .clickable(onClick = onBack)
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            MenuItem(
                title = stringResource(R.string.dark_theme),
                iconRes = null,
                isDark = isDark
            )

            MenuItem(
                iconRes = R.drawable.share,
                title = stringResource(R.string.share_app),
                iconWidth = 16.dp,
                iconHeight = 18.dp,
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, shareChooserTitle))
                }
            )

            MenuItem(
                iconRes = R.drawable.support,
                title = stringResource(R.string.write_to_devs),
                iconWidth = 20.dp,
                iconHeight = 18.dp,
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:")
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(devEmail))
                        putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                        putExtra(Intent.EXTRA_TEXT, emailBody)
                    }
                    context.startActivity(Intent.createChooser(emailIntent, emailChooserTitle))
                }
            )

            MenuItem(
                iconRes = R.drawable.chevron_right,
                title = stringResource(R.string.user_agreement),
                iconWidth = 8.dp,
                iconHeight = 14.dp,
                onClick = {
                    val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(userAgreementLink)
                    }
                    context.startActivity(agreementIntent)
                }
            )
        }
    }
}


@Composable
private fun MenuItem(
    iconRes: Int?,
    title: String,
    onClick: (() -> Unit)? = null,
    isDark: androidx.compose.runtime.MutableState<Boolean>? = null,
    iconWidth: Dp = 24.dp,
    iconHeight: Dp = 24.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 21.dp)
            .padding(end = 12.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )

        if (iconRes == null && isDark != null) {
            Switch(
                checked = isDark.value,
                onCheckedChange = { isDark.value = it }
            )
        } else if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color(0xFFAEAFB4),
                modifier = Modifier
                    .width(iconWidth)
                    .height(iconHeight)
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SettingsScreenPreview() {
    PlaylistMakerTheme { SettingsScreen(onBack = {}) }
}