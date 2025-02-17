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

package me.proton.android.drive.ui.test.flow.rename

import dagger.hilt.android.testing.HiltAndroidTest
import me.proton.android.drive.ui.annotation.Scenario
import me.proton.android.drive.ui.robot.FilesTabRobot
import me.proton.android.drive.ui.robot.PhotosTabRobot
import me.proton.android.drive.ui.robot.PreviewRobot
import me.proton.android.drive.ui.test.BaseTest
import me.proton.core.test.android.instrumented.utils.StringUtils
import me.proton.core.test.rule.annotation.PrepareUser
import org.junit.Test
import me.proton.core.drive.i18n.R as I18N

@HiltAndroidTest
class RenamingFileSuccessFlowTest : BaseTest() {

    @Test
    @PrepareUser(loginBefore = true)
    @Scenario(forTag = "main", value = 4)
    fun renameViaPreviewWindowSucceeds() {
        val oldName = "image.jpg"
        val newName = "picture.jpg"

        PhotosTabRobot
            .clickFilesTab()
            .scrollToItemWithName(oldName)
            .clickOnFile(oldName)
            .clickOnContextualButton()
            .clickRename()
            .clearName()
            .typeName(newName)
            .clickRename(PreviewRobot)
            .verify {
                nodeWithTextDisplayed(
                    StringUtils.stringFromResource(
                        I18N.string.link_rename_successful,
                        newName
                    )
                )
                topBarWithTextDisplayed(newName)
            }
    }

    @Test
    @PrepareUser(loginBefore = true)
    @Scenario(forTag = "main", value = 9)
    fun renameAnonymousFile() {
        val oldName = "anonymous-file"
        val newName = "anonymous-file-renamed"
        PhotosTabRobot
            .clickFilesTab()
            .clickMoreOnItem(oldName)
            .clickRename()
            .typeName(newName)
            .clickRename(FilesTabRobot)
            .verify {
                nodeWithTextDisplayed(
                    StringUtils.stringFromResource(
                        I18N.string.link_rename_successful,
                        newName
                    )
                )
                itemIsDisplayed(newName)
            }
    }
}
