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

package me.proton.android.drive.lock.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import me.proton.core.drive.base.data.db.Column.KEY
import me.proton.core.drive.base.data.db.Column.LAST_ACCESS_TIME

@Entity(
    primaryKeys = [KEY],
)
data class EnableAppLockEntity(
    @ColumnInfo(name = KEY)
    val key: String,
    @ColumnInfo(name = LAST_ACCESS_TIME)
    val timestamp: Long,
)
