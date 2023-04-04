/*
 * Copyright (c) 2023 Proton AG.
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
package me.proton.core.drive.thumbnail.data.provider

import android.content.Context
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Size
import dagger.hilt.android.qualifiers.ApplicationContext
import me.proton.core.drive.base.presentation.entity.FileTypeCategory
import java.io.File
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class AudioThumbnailProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileThumbnailProvider(
    context = context,
    category = FileTypeCategory.Audio,
    prefix = "audio_thumbnail_",
) {

    override fun fileToBitmap(file: File, size: Size) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ThumbnailUtils.createAudioThumbnail(
                file,
                size,
                null
            )
        } else {
            null
        }
}
