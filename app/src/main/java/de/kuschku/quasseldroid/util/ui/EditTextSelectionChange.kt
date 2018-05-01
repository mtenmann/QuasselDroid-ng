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

package de.kuschku.quasseldroid.util.ui

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet

open class EditTextSelectionChange : AppCompatEditText {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    super(context, attrs, defStyleAttr)

  private var selectionChangeListener: ((IntRange) -> Unit)? = null

  fun setSelectionChangeListener(f: (IntRange) -> Unit) {
    selectionChangeListener = f
  }

  fun removeSelectionChangeListener() {
    selectionChangeListener = null
  }

  override fun onSelectionChanged(selStart: Int, selEnd: Int) {
    super.onSelectionChanged(selStart, selEnd)
    selectionChangeListener?.invoke(selStart until selEnd)
  }
}
