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
package me.proton.android.drive.lock.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.proton.android.drive.lock.data.db.entity.AppLockEntity
import me.proton.core.data.room.db.BaseDao

@Dao
abstract class AppLockDao : BaseDao<AppLockEntity>() {
    @Query("""
        SELECT EXISTS(SELECT * FROM AppLockEntity)
    """)
    abstract suspend fun hasAppLock(): Boolean

    @Query("""
        SELECT EXISTS(SELECT * FROM AppLockEntity)
    """)
    abstract fun hasAppLockFlow(): Flow<Boolean>

    @Query("""
        SELECT * FROM AppLockEntity LIMIT 1
    """)
    abstract suspend fun getAppLock(): AppLockEntity

    @Query("""
        DELETE FROM AppLockEntity
    """)
    abstract suspend fun deleteAppLock()
}
