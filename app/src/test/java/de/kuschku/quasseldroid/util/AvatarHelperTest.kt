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

package de.kuschku.quasseldroid.util

import de.kuschku.libquassel.protocol.Message_Flag
import de.kuschku.libquassel.protocol.Message_Type
import de.kuschku.quasseldroid.persistence.QuasselDatabase
import de.kuschku.quasseldroid.settings.MessageSettings
import de.kuschku.quasseldroid.util.avatars.AvatarHelper
import de.kuschku.quasseldroid.viewmodel.data.Avatar
import org.junit.Test
import org.threeten.bp.Instant

class AvatarHelperTest {
  @Test
  fun testGravatarAvatars() {
    val message = QuasselDatabase.MessageData(
      messageId = 1,
      time = Instant.now(),
      type = Message_Type.of(Message_Type.Plain),
      flag = Message_Flag.of(),
      bufferId = 0,
      networkId = 0,
      sender = "justJanne",
      senderPrefixes = "",
      realName = "Janne Koschinski <janne@kuschku.de>",
      avatarUrl = "",
      content = "Lorem Ipsum I Dolor Sit Amet",
      ignored = false
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = true,
          showIRCCloudAvatars = true
        ),
        message
      ).contains(
        Avatar.GravatarAvatar("https://www.gravatar.com/avatar/81128f11cae692bc486e3f88b854ddf1?d=404")
      )
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = false,
          showIRCCloudAvatars = false
        ),
        message
      ).isEmpty()
    )
  }

  @Test
  fun testIrcCloudAvatars() {
    val message = QuasselDatabase.MessageData(
      messageId = 1,
      time = Instant.now(),
      type = Message_Type.of(Message_Type.Plain),
      flag = Message_Flag.of(),
      bufferId = 0,
      networkId = 0,
      sender = "jwheare!sid2@irccloud.com",
      senderPrefixes = "",
      realName = "James Wheare",
      avatarUrl = "",
      content = "Lorem Ipsum I Dolor Sit Amet",
      ignored = false
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = true,
          showIRCCloudAvatars = true
        ),
        message
      ).contains(
        Avatar.IRCCloudAvatar("https://static.irccloud-cdn.com/avatar-redirect/2")
      )
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = false,
          showIRCCloudAvatars = false
        ),
        message
      ).isEmpty()
    )
  }

  @Test
  fun testActualAvatars() {
    val message = QuasselDatabase.MessageData(
      messageId = 1,
      time = Instant.now(),
      type = Message_Type.of(Message_Type.Plain),
      flag = Message_Flag.of(),
      bufferId = 0,
      networkId = 0,
      sender = "jwheare!sid2@irccloud.com",
      senderPrefixes = "",
      realName = "James Wheare",
      avatarUrl = "https://quasseldroid.info/favicon.png",
      content = "Lorem Ipsum I Dolor Sit Amet",
      ignored = false
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = true,
          showIRCCloudAvatars = true
        ),
        message
      ).contains(
        Avatar.NativeAvatar("https://quasseldroid.info/favicon.png")
      )
    )

    assert(
      AvatarHelper.avatar(
        MessageSettings(
          showGravatarAvatars = false,
          showIRCCloudAvatars = false
        ),
        message
      ) == listOf(Avatar.NativeAvatar("https://quasseldroid.info/favicon.png"))
    )
  }
}
