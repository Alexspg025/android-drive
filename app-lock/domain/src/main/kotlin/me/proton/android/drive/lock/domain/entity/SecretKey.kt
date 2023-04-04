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
package me.proton.android.drive.lock.domain.entity

import me.proton.core.crypto.common.keystore.EncryptedByteArray
import me.proton.core.crypto.common.keystore.EncryptedString
import me.proton.core.crypto.common.keystore.PlainByteArray

interface SecretKey {
    /**
     * Encrypt a [String] [value] and return an [EncryptedString].
     */
    fun encrypt(value: String): EncryptedString

    /**
     * Decrypt an [EncryptedString] [value] and return a [String].
     */
    fun decrypt(value: EncryptedString): String

    /**
     * Encrypt a [PlainByteArray] [value] and return an [EncryptedByteArray].
     */
    fun encrypt(value: PlainByteArray): EncryptedByteArray

    /**
     * Decrypt an [EncryptedByteArray] [value] and return a [PlainByteArray].
     */
    fun decrypt(value: EncryptedByteArray): PlainByteArray
}
