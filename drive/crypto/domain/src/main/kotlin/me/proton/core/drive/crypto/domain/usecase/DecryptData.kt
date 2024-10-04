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

package me.proton.core.drive.crypto.domain.usecase

import me.proton.core.crypto.common.pgp.SessionKey
import me.proton.core.drive.crypto.domain.entity.CipherSpec
import me.proton.core.drive.cryptobase.domain.CryptoScope
import kotlin.coroutines.CoroutineContext

interface DecryptData {

    /**
     * Decrypts the given Base64 [input] using the provided [SessionKey].
     * @return Decrypted data as [ByteArray]
     */
    suspend operator fun invoke(
        decryptKey: SessionKey,
        input: String,
        cipherSpec: CipherSpec,
        coroutineContext: CoroutineContext = CryptoScope.EncryptAndDecrypt.coroutineContext,
    ): Result<ByteArray>
}
