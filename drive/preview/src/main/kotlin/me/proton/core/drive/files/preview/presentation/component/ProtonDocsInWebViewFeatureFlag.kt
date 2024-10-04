/*
 * Copyright (c) 2024 Proton AG.
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

package me.proton.core.drive.files.preview.presentation.component

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.proton.core.domain.entity.UserId
import me.proton.core.drive.base.domain.provider.ConfigurationProvider
import me.proton.core.drive.feature.flag.domain.entity.FeatureFlagId.Companion.driveDocsWebView
import me.proton.core.drive.feature.flag.domain.extension.on
import me.proton.core.drive.feature.flag.domain.usecase.GetFeatureFlagFlow
import javax.inject.Inject

class ProtonDocsInWebViewFeatureFlag @Inject constructor(
    private val getFeatureFlagFlow: GetFeatureFlagFlow,
    private val configurationProvider: ConfigurationProvider,
) {

    operator fun invoke(userId: UserId): Flow<Boolean> =
        getFeatureFlagFlow(
            featureFlagId = driveDocsWebView(userId),
            emitNotFoundInitially = false,
        )
            .map { driveDocsWebView ->
                configurationProvider.protonDocsWebViewFeatureFlag && driveDocsWebView.on
            }
}
