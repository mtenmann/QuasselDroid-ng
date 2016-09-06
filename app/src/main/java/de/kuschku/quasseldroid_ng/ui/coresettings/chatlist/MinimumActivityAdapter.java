/*
 * QuasselDroid - Quassel client for Android
 * Copyright (C) 2016 Janne Koschinski
 * Copyright (C) 2016 Ken Børge Viktil
 * Copyright (C) 2016 Magnus Fjell
 * Copyright (C) 2016 Martin Sandsmark <martin.sandsmark@kde.org>
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kuschku.quasseldroid_ng.ui.coresettings.chatlist;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import de.kuschku.libquassel.syncables.types.interfaces.QBufferViewConfig;
import de.kuschku.quasseldroid_ng.R;
import de.kuschku.quasseldroid_ng.ui.theme.AppContext;

class MinimumActivityAdapter implements SpinnerAdapter {
    List<QBufferViewConfig.MinimumActivity> list = Arrays.asList(
            QBufferViewConfig.MinimumActivity.NONE,
            QBufferViewConfig.MinimumActivity.OTHER,
            QBufferViewConfig.MinimumActivity.MESSAGE,
            QBufferViewConfig.MinimumActivity.HIGHLIGHT
    );
    private AppContext context;

    public MinimumActivityAdapter(AppContext context) {
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView view = (TextView) inflater.inflate(R.layout.widget_spinner_item_toolbar, parent, false);
        QBufferViewConfig.MinimumActivity minimumActivity = getItem(position);
        view.setText(minimumActivity == null ? "" : context.themeUtil().translations.minimumActivity(minimumActivity));
        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QBufferViewConfig.MinimumActivity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView view = (TextView) inflater.inflate(R.layout.widget_spinner_item_inline, parent, false);
        QBufferViewConfig.MinimumActivity minimumActivity = getItem(position);
        view.setText(minimumActivity == null ? "" : context.themeUtil().translations.minimumActivity(minimumActivity));
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int indexOf(QBufferViewConfig.MinimumActivity minimumActivity) {
        return list.indexOf(minimumActivity);
    }
}
