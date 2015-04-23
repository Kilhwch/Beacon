package com.example.kasper.beacon.Beacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;
import com.example.kasper.beacon.R;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kasper on 3/20/2015.
 */
public class BeaconData extends BaseAdapter {

        private ArrayList<Beacon> beacons;
        private LayoutInflater inflater;

        public BeaconData(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.beacons = new ArrayList<>();
        }

        public void replaceWith(Collection<Beacon> newBeacons) {
            this.beacons.clear();
            this.beacons.addAll(newBeacons);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return beacons.size();
        }

        @Override
        public Beacon getItem(int position) {
            return beacons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflateIfRequired(view, position, parent);
            bind(getItem(position), view, position);
            return view;
        }

        private void bind(Beacon beacon, View view, int position) {
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.name.setText(String.format("Prancha " + (position+1) + " - " + beacon.getName() + " (%.2fm)", Utils.computeAccuracy(beacon)));
        }

        private View inflateIfRequired(View view, int position, ViewGroup parent) {
            if (view == null) {
                view = inflater.inflate(R.layout.device_item, null);
                view.setTag(new ViewHolder(view));
            }
            return view;
        }

        static class ViewHolder {
            final TextView name;
            ViewHolder(View view) {
                name = (TextView) view.findViewWithTag("name");
            }
        }
}
