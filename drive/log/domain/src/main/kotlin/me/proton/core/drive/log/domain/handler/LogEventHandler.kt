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

package me.proton.core.drive.log.domain.handler

import me.proton.core.domain.entity.UserId
import me.proton.core.drive.announce.event.domain.entity.Event
import me.proton.core.drive.announce.event.domain.handler.EventHandler
import me.proton.core.drive.log.domain.entity.Log
import me.proton.core.drive.log.domain.extension.toLog
import me.proton.core.drive.log.domain.usecase.InsertLog
import javax.inject.Inject

class LogEventHandler @Inject constructor(
    private val insertLog: InsertLog,
) : EventHandler {
    override suspend fun onEvent(userId: UserId, event: Event) {
        event.log(userId)?.let { log ->
            insertLog(log)
        }
    }

    private fun Event.log(userId: UserId): Log? = when (this) {
        is Event.Download -> toLog(userId)
        is Event.Upload -> toLog(userId)
        is Event.Throwable -> toLog(userId)
        is Event.Network -> toLog(userId)
        is Event.Logger -> toLog(userId)
        else -> null
    }
}
