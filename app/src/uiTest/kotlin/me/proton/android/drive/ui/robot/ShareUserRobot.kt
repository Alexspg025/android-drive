/*
 * Copyright (c) 2023-2024 Proton AG.
 * This file is part of Proton Drive.
 *
 * Proton Drive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Drive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Drive.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.proton.android.drive.ui.robot

import me.proton.android.drive.ui.extension.isValidField
import me.proton.core.drive.drivelink.shared.presentation.component.ShareViaInvitationsTestTag
import me.proton.test.fusion.Fusion.node
import me.proton.test.fusion.FusionConfig.targetContext
import me.proton.core.drive.i18n.R as I18N

object ShareUserRobot : NavigationBarRobot, Robot {
    private val contentShareScreen get() = node.withTag(ShareViaInvitationsTestTag.content)
    private val closeButton get() = node.withText(I18N.string.common_close_action)
    private val emailTextField get() = node.isSetText()
    private val sendButton get() = node.withContentDescription(I18N.string.common_send_action)
    fun typeEmail(text: String) = apply {
        emailTextField.typeText(text).typeText(" ")
    }

    fun clickSend() = sendButton.clickTo(this)
    fun <T : Robot> clickClose(goesTo: T): T = closeButton.clickTo(goesTo)

    fun assertShareFile(folderName: String) = node.withText(
        targetContext.resources.getString(
            I18N.string.title_share_via_invitations,
        ).format(folderName)
    ).await { assertIsDisplayed() }

    fun assertValidEmail(email: String) =
        node.withText(email).hasAncestor(node.isValidField(true)).await { assertIsDisplayed() }

    fun assertInvalidEmail(email: String) =
        node.withText(email).hasAncestor(node.isValidField(false)).await { assertIsDisplayed() }

    fun assertInvitationSent(count: Int) = node.withText(
        targetContext.resources.getQuantityString(
            I18N.plurals.share_via_invitations_person_added,
            count
        ).format(count)
    ).await { assertIsDisplayed() }

    override fun robotDisplayed() {
        contentShareScreen.await { assertIsDisplayed() }
    }
}
