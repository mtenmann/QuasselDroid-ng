package de.kuschku.quasseldroid.ui.chat.messages

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.util.LruCache
import android.view.LayoutInflater
import android.view.ViewGroup
import de.kuschku.libquassel.protocol.*
import de.kuschku.libquassel.util.hasFlag
import de.kuschku.quasseldroid.persistence.QuasselDatabase.DatabaseMessage
import de.kuschku.quasseldroid.util.helper.getOrPut

class MessageAdapter(
  private val messageRenderer: MessageRenderer,
  var markerLinePosition: Pair<MsgId, MsgId>? = null
) : PagedListAdapter<DatabaseMessage, QuasselMessageViewHolder>(
  object : DiffUtil.ItemCallback<DatabaseMessage>() {
    override fun areItemsTheSame(oldItem: DatabaseMessage, newItem: DatabaseMessage) =
      oldItem.messageId == newItem.messageId

    override fun areContentsTheSame(oldItem: DatabaseMessage, newItem: DatabaseMessage) =
      oldItem == newItem &&
      oldItem.messageId != markerLinePosition?.first &&
      oldItem.messageId != markerLinePosition?.second
  }) {

  private val messageCache = LruCache<Int, FormattedMessage>(512)

  fun clearCache() {
    messageCache.evictAll()
  }

  override fun onBindViewHolder(holder: QuasselMessageViewHolder, position: Int) {
    getItem(position)?.let {
      messageRenderer.bind(
        holder,
        if (it.messageId == markerLinePosition?.second || it.messageId == markerLinePosition?.first) {
          val value = messageRenderer.render(
            holder.itemView.context, it, markerLinePosition?.second ?: -1
          )
          messageCache.put(it.messageId, value)
          value
        } else {
          messageCache.getOrPut(it.messageId) {
            messageRenderer.render(
              holder.itemView.context, it, markerLinePosition?.second ?: -1
            )
          }
        })
    }
  }

  override fun getItemViewType(position: Int) = getItem(position)?.let {
    viewType(Message_Flags.of(it.type), Message_Flags.of(it.flag))
  } ?: 0

  private fun viewType(type: Message_Types, flags: Message_Flags) =
    if (flags.hasFlag(Message_Flag.Highlight)) {
      -type.value
    } else {
      type.value
    }

  override fun getItemId(position: Int): Long {
    return getItem(position)?.messageId?.toLong() ?: 0L
  }

  private fun messageType(viewType: Int): Message_Type? =
    Message_Type.of(Math.abs(viewType)).enabledValues().firstOrNull()

  private fun hasHiglight(viewType: Int) = viewType < 0

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuasselMessageViewHolder {
    val messageType = messageType(viewType)
    val hasHighlight = hasHiglight(viewType)
    val viewHolder = QuasselMessageViewHolder(
      LayoutInflater.from(parent.context).inflate(
        messageRenderer.layout(messageType, hasHighlight),
        parent,
        false
      )
    )
    messageRenderer.init(viewHolder, messageType, hasHighlight)
    return viewHolder
  }

  operator fun get(position: Int) = if (position in 0 until itemCount) {
    getItem(position)
  } else {
    null
  }
}
