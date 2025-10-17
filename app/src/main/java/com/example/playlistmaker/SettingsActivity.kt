package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.ui.theme.PlaylistMakerTheme


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
                fontFamily = AppTypography.YSD_Medium500,
                fontSize = 22.sp,
                color = Color(0xFF1A1B22)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            SettingsMenuItem(
                title = stringResource(R.string.dark_theme),
                iconRes = null,
                isDark = isDark
            )

            SettingsMenuItem(
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

            SettingsMenuItem(
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

            SettingsMenuItem(
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SettingsScreenPreview() {
    PlaylistMakerTheme { SettingsScreen(onBack = {}) }
}