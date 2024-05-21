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

package me.proton.android.drive.ui.test.flow.share

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import me.proton.android.drive.ui.annotation.FeatureFlag
import me.proton.android.drive.ui.robot.PhotosTabRobot
import me.proton.android.drive.ui.rules.Scenario
import me.proton.android.drive.ui.test.AuthenticatedBaseTest
import me.proton.core.drive.feature.flag.domain.entity.FeatureFlag.State.NOT_FOUND
import me.proton.core.drive.feature.flag.domain.entity.FeatureFlagId.Companion.DRIVE_SHARING
import org.junit.Test
import org.junit.runner.RunWith
import me.proton.core.drive.i18n.R as I18N

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class DeleteSharedLinkFlowTest : AuthenticatedBaseTest() {

    @Test
    @Scenario(4)
    @FeatureFlag(DRIVE_SHARING, NOT_FOUND)
    fun stopSharingActiveLinkViaBottomSheet() {
        val file = "shared.jpg"

        PhotosTabRobot
            .clickFilesTab()
            .scrollToItemWithName(file)
            .clickMoreOnItem(file)
            .clickStopSharing()
            .confirmStopSharing()
            .verify {
                nodeWithTextDisplayed(I18N.string.description_files_stop_sharing_action_success)
                itemIsDisplayed(file, isShared = false)
            }
    }

    @Test
    @Scenario(4)
    @FeatureFlag(DRIVE_SHARING, NOT_FOUND)
    fun stopSharingActiveLinkViaManageLink() {
        val file = "shared.jpg"

        PhotosTabRobot
            .clickFilesTab()
            .scrollToItemWithName(file)
            .clickMoreOnItem(file)
            .clickManageLink()
            .clickStopSharing()
            .confirmStopSharing()
            .verify {
                nodeWithTextDisplayed(I18N.string.description_files_stop_sharing_action_success)
                robotDisplayed()
                itemIsDisplayed(file, isShared = false)
            }
    }

    @Test
    @Scenario(4)
    @FeatureFlag(DRIVE_SHARING, NOT_FOUND)
    fun stopSharingExpiredLinkViaBottomSheet() {
        val file = "expiredSharedFile.jpg"

        PhotosTabRobot
            .clickFilesTab()
            .scrollToItemWithName(file)
            .clickMoreOnItem(file)
            .clickStopSharing()
            .confirmStopSharing()
            .verify {
                nodeWithTextDisplayed(I18N.string.description_files_stop_sharing_action_success)
                itemIsDisplayed(file, isShared = false)
            }
    }
}
