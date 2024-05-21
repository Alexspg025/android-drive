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
package me.proton.core.drive.drivelink.shared.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import me.proton.core.domain.arch.DataResult
import me.proton.core.domain.arch.ResponseSource
import me.proton.core.drive.base.domain.extension.asSuccess
import me.proton.core.drive.base.domain.extension.transformSuccess
import me.proton.core.drive.drivelink.domain.entity.DriveLink
import me.proton.core.drive.drivelink.shared.domain.entity.SharedDriveLink
import me.proton.core.drive.link.domain.usecase.GetLink
import me.proton.core.drive.share.domain.usecase.GetShare
import me.proton.core.drive.shareurl.base.domain.entity.ShareUrlId
import me.proton.core.drive.shareurl.base.domain.usecase.GetShareUrl
import me.proton.core.drive.shareurl.crypto.domain.usecase.GetCustomUrlPassword
import me.proton.core.drive.shareurl.crypto.domain.usecase.GetPublicUrl
import me.proton.core.drive.volume.domain.entity.VolumeId
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetSharedDriveLink @Inject constructor(
    private val getLink: GetLink,
    private val getShare: GetShare,
    private val getShareUrl: GetShareUrl,
    getPublicUrl: GetPublicUrl,
    getCustomUrlPassword: GetCustomUrlPassword,
) : BaseSharedDriveLink(getPublicUrl, getCustomUrlPassword) {
    operator fun invoke(driveLink: DriveLink): Flow<DataResult<SharedDriveLink?>> =
        getLink(driveLink.id).transformSuccess { dataResult ->
            val sharingDetails = dataResult.value.sharingDetails
            val shareUrlId = sharingDetails?.shareUrlId
            if (shareUrlId != null) {
                emitAll(
                    invoke(
                        volumeId = driveLink.volumeId,
                        shareUrlId = shareUrlId,
                    )
                )
            } else {
                emit(DataResult.Success(ResponseSource.Local, null))
            }
        }

    operator fun invoke(
        volumeId: VolumeId,
        shareUrlId: ShareUrlId,
    ): Flow<DataResult<SharedDriveLink>> = getShare(shareUrlId.shareId)
        .distinctUntilChanged()
        .transformSuccess { (_, _) ->
            emitAll(
                getShareUrl(
                    volumeId = volumeId,
                    shareUrlId = shareUrlId,
                )
            )
        }.transformSuccess { (_, shareUrl) ->
            emit(shareUrl.toSharedDriveLink().asSuccess)
        }
}

