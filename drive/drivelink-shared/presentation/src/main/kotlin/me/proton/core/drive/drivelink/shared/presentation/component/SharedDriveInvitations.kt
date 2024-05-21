/*
 * Copyright (c) 2022-2024 Proton AG.
 * This file is part of Proton Core.
 *
 * Proton Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Core.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:OptIn(ExperimentalMaterialApi::class)

package me.proton.core.drive.drivelink.shared.presentation.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import me.proton.core.compose.theme.ProtonDimens.DefaultButtonMinHeight
import me.proton.core.compose.theme.ProtonDimens.DefaultIconSize
import me.proton.core.compose.theme.ProtonDimens.ExtraSmallSpacing
import me.proton.core.compose.theme.ProtonDimens.SmallSpacing
import me.proton.core.compose.theme.ProtonTheme
import me.proton.core.compose.theme.defaultNorm
import me.proton.core.drive.base.domain.entity.Permissions
import me.proton.core.drive.base.presentation.component.TopAppBar
import me.proton.core.drive.contact.presentation.component.ChipItem
import me.proton.core.drive.contact.presentation.component.ChipsListField
import me.proton.core.drive.contact.presentation.component.ContactSuggestionState
import me.proton.core.drive.contact.presentation.component.SuggestionItem
import me.proton.core.drive.contact.presentation.viewstate.ContactSuggestion
import me.proton.core.drive.drivelink.shared.presentation.viewevent.SharedDriveInvitationsViewEvent
import me.proton.core.drive.drivelink.shared.presentation.viewstate.PermissionViewState
import me.proton.core.drive.drivelink.shared.presentation.viewstate.PermissionsViewState
import me.proton.core.drive.drivelink.shared.presentation.viewstate.SaveButtonViewState
import me.proton.core.drive.drivelink.shared.presentation.viewstate.SharedDriveInvitationsViewState
import me.proton.core.drive.share.user.domain.entity.ShareUserInvitation
import me.proton.core.drive.i18n.R as I18N
import me.proton.core.presentation.R as CorePresentation

@Composable
fun SharedDriveInvitations(
    viewState: SharedDriveInvitationsViewState,
    saveButtonViewState: SaveButtonViewState,
    viewEvent: SharedDriveInvitationsViewEvent,
    modifier: Modifier = Modifier,
) {
    BackHandler { viewEvent.onBackPressed() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        TopAppBar(
            navigationIcon = painterResource(id = CorePresentation.drawable.ic_arrow_back),
            onNavigationIcon = viewEvent.onBackPressed,
            title = stringResource(
                id = I18N.string.title_share_via_invitations,
                viewState.linkName
            ),
            modifier = Modifier.statusBarsPadding(),
        )
        EmailForm(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            invitations = viewState.invitations,
            contactSuggestions = viewState.contactSuggestions,
            onInviteesChanged = viewEvent.onInviteesChanged,
            onSuggestionTermTyped = viewEvent.onSearchTermChanged,
            emailValidator = viewEvent.isValidEmailAddress,
        )
        Divider(
            color = ProtonTheme.colors.separatorNorm,
        )
        if (viewState.showPermissions) {
            PermissionSelect(
                permissionViewState = viewState.permissionsViewState.selected,
                onClick = {
                    viewEvent.onPermissions()
                    keyboardController?.hide()
                },
            )
            Divider(
                color = ProtonTheme.colors.separatorNorm,
            )
        }
        Spacer(modifier = Modifier.weight(1F))
        SendContainer(saveButtonViewState, onSave = {
            keyboardController?.hide()
            viewEvent.onSave()
        })
    }
}

@Composable
private fun EmailForm(
    invitations: List<ShareUserInvitation>,
    contactSuggestions: List<ContactSuggestion>,
    onSuggestionTermTyped: (String) -> Unit,
    onInviteesChanged: (List<String>) -> Unit,
    emailValidator: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier) {

        val contactSuggestionState = remember(contactSuggestions) {
            ContactSuggestionState(
                contactSuggestions.isNotEmpty(),
                contactSuggestions.map {
                    SuggestionItem(
                        it.name,
                        it.email
                    )
                })
        }
        val actions = remember {
            ChipsListField.Actions(
                onSuggestionTermTyped = onSuggestionTermTyped,
                onSuggestionsDismissed = {
                    onSuggestionTermTyped("")
                },
                onListChanged = { items ->
                    onInviteesChanged(items.map { item -> item.value })
                }
            )
        }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Row(
            modifier = Modifier.padding(
                horizontal = ExtraSmallSpacing,
                vertical = SmallSpacing
            ),
            horizontalArrangement = Arrangement.spacedBy(SmallSpacing)
        ) {
            Box(
                modifier = Modifier.size(DefaultButtonMinHeight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(DefaultIconSize),
                    painter = painterResource(id = CorePresentation.drawable.ic_proton_user_plus),
                    tint = ProtonTheme.colors.iconWeak,
                    contentDescription = null,
                )
            }

            ChipsListField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterVertically),
                hint = stringResource(id = I18N.string.share_via_invitations_add_people_hint),
                value = invitations.toChipItem(),
                focusRequester = focusRequester,
                actions = actions,
                contactSuggestionState = contactSuggestionState,
                chipValidator = emailValidator,
            )
        }
    }
}

@Composable
private fun PermissionSelect(
    permissionViewState: PermissionViewState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val selected = permissionViewState
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(
                horizontal = ExtraSmallSpacing,
                vertical = SmallSpacing
            ),
        horizontalArrangement = Arrangement.spacedBy(SmallSpacing)
    ) {
        Box(
            modifier = Modifier.size(DefaultButtonMinHeight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(DefaultIconSize),
                painter = painterResource(id = selected.icon),
                tint = ProtonTheme.colors.iconWeak,
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier
                .align(CenterVertically)
                .weight(1F),
            text = selected.label,
            style = ProtonTheme.typography.defaultNorm,
        )
    }
}


private fun List<ShareUserInvitation>.toChipItem(): List<ChipItem> = map { it.toChipItem() }
private fun ShareUserInvitation.toChipItem(): ChipItem = when (isValid) {
    true -> ChipItem.Valid(email)
    else -> ChipItem.Invalid(email)
}

@Preview
@Composable
fun SharedDriveInvitationsPreview(
    @PreviewParameter(ViewStateParameterProvider::class) viewState: SharedDriveInvitationsViewState,
) {
    ProtonTheme {
        SharedDriveInvitations(
            viewState = viewState,
            viewEvent = object : SharedDriveInvitationsViewEvent {
                override val onSearchTermChanged: (String) -> Unit = {}
                override val onInviteesChanged: (List<String>) -> Unit = {}
                override val onPermissions: () -> Unit = {}
                override val onPermissionsChanged: (Permissions) -> Unit = {}
                override val onBackPressed: () -> Unit = {}
                override val onRetry: () -> Unit = {}
                override val onSave: () -> Unit = {}
                override val isValidEmailAddress: (String) -> Boolean = { true }
            },
            saveButtonViewState = SaveButtonViewState(
                label = "Sharing with ${viewState.invitations.size} persons",
                isVisible = viewState.invitations.isNotEmpty(),
                isEnabled = true,
                inProgress = false,
            ),
        )
    }
}

class ViewStateParameterProvider : PreviewParameterProvider<SharedDriveInvitationsViewState> {
    private val invitations = listOf(
        ShareUserInvitation(
            email = "me@pm.me",
            permissions = Permissions().add(Permissions.Permission.WRITE),
            isValid = true
        ),
        ShareUserInvitation(
            email = "verylongaddressemail@protonmail.com",
            permissions = Permissions().add(Permissions.Permission.WRITE),
            isValid = true
        ),
        ShareUserInvitation(
            email = "you@pm.me",
            permissions = Permissions().add(Permissions.Permission.WRITE),
            isValid = true
        ),
        ShareUserInvitation(
            email = "me@pm.me",
            permissions = Permissions().add(Permissions.Permission.WRITE),
            isValid = true
        ),
        ShareUserInvitation(
            email = "me@pm.me",
            permissions = Permissions().add(Permissions.Permission.WRITE),
            isValid = true
        ),
    )
    private val emptyViewState =
        SharedDriveInvitationsViewState(
            linkName = "Folder",
            isLinkNameEncrypted = false,
            showPermissions = true,
            invitations = emptyList(),
            contactSuggestions = emptyList(),
            permissionsViewState = PermissionsViewState(
                options = listOf(
                    PermissionViewState(
                        icon = CorePresentation.drawable.ic_proton_eye,
                        label = "Viewer",
                        selected = true,
                        permissions = Permissions()
                    ),
                    PermissionViewState(
                        icon = CorePresentation.drawable.ic_proton_pen,
                        label = "Editor",
                        selected = false,
                        permissions = Permissions()
                    ),
                )
            )
        )
    override val values = sequenceOf(
        emptyViewState,
        emptyViewState.copy(invitations = invitations.subList(0, 1)),
        emptyViewState.copy(invitations = invitations),
        emptyViewState.copy(
            showPermissions = false,
            invitations = listOf(
                ShareUserInvitation(
                    email = "external@mail.com",
                    permissions = Permissions().add(Permissions.Permission.WRITE),
                    isValid = false
                )
            )
        ),
    )
}
