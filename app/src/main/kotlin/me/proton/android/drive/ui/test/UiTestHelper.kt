/*
 * Copyright (c) 2023 Proton AG.
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

package me.proton.android.drive.ui.test

import androidx.annotation.RestrictTo
import me.proton.android.drive.usecase.MarkOnboardingAsShown
import me.proton.android.drive.usecase.MarkWhatsNewAsShown
import me.proton.core.drive.base.domain.provider.ConfigurationProvider
import me.proton.drive.android.settings.domain.entity.WhatsNewKey
import javax.inject.Inject

@RestrictTo(RestrictTo.Scope.TESTS)
class UiTestHelper @Inject constructor(
    val configurationProvider: ConfigurationProvider,
    private val markOnboardingAsShown: MarkOnboardingAsShown,
    private val markWhatsNewAsShown: MarkWhatsNewAsShown,
) {
    suspend fun doNotShowOnboardingAfterLogin() {
        markOnboardingAsShown()
    }

    suspend fun doNotShowWhatsNewAfterLogin() {
        markWhatsNewAsShown(WhatsNewKey.PROTON_DOCS)
    }
}
