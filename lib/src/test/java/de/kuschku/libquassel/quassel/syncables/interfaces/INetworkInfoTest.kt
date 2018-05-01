/*
 * Quasseldroid - Quassel client for Android
 *
 * Copyright (c) 2018 Janne Koschinski
 * Copyright (c) 2018 The Quassel Project
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kuschku.libquassel.quassel.syncables.interfaces

import de.kuschku.libquassel.protocol.primitive.serializer.VariantMapSerializer
import de.kuschku.libquassel.util.roundTrip
import org.junit.Assert.assertEquals
import org.junit.Test

class INetworkInfoTest {
  @Test
  fun testSerialization() {
    val original = INetwork.NetworkInfo(
      networkName = "QuakeNet",
      identity = 5,
      serverList = listOf(
        INetwork.Server(
          host = "irc.quakenet.org",
          port = 6667
        )
      )
    )
    val copy = original.copy()
    copy.fromVariantMap(roundTrip(VariantMapSerializer, original.toVariantMap()))
    assertEquals(original, copy)
  }

  @Test
  fun testCopy() {
    val original = INetwork.NetworkInfo(
      networkName = "QuakeNet",
      identity = 5,
      serverList = listOf(
        INetwork.Server(
          host = "irc.quakenet.org",
          port = 6667
        )
      )
    )
    val copy = original.copy()
    copy.fromVariantMap(original.toVariantMap())
    assertEquals(original, copy)
  }
}
