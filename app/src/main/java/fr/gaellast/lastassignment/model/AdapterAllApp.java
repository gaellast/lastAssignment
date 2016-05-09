package fr.gaellast.lastassignment.model;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.gaellast.lastassignment.R;

/**
 * Created by gaellast on 05/05/2016.
 */
public class    AdapterAllApp extends RecyclerView.Adapter<AdapterAllApp.ViewHolder> {

    private     List<AppData> _data;
    public      List<String> selected = new ArrayList<>();

    public AdapterAllApp(List<AppData> list) {
        this._data = list;
    }

    public class            ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView    IVCan_find_app;
        public TextView     TVCan_find_app;
        public LinearLayout LLCan_find_app;
        public String       packageName;

        public ViewHolder(View itemView) {
            super(itemView);
            TVCan_find_app = (TextView) itemView.findViewById(R.id.TVCan_find_app);
            IVCan_find_app = (ImageView) itemView.findViewById(R.id.IVCan_find_app);
            LLCan_find_app = (LinearLayout) itemView.findViewById(R.id.LLCan_find_app);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selected.contains(packageName)) {
                selected.remove(packageName);
                v.setBackgroundColor(Color.WHITE);
                return;
            } else
            selected.add(packageName);
            v.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.can_find_app, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.packageName = _data.get(position).packageName;
        holder.TVCan_find_app.setText(_data.get(position).appName);
        holder.IVCan_find_app.setImageDrawable(_data.get(position).image);
        if (selected.contains(holder.packageName))
            holder.LLCan_find_app.setBackgroundColor(Color.BLUE);
        else
            holder.LLCan_find_app.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return this._data.size();
    }

}
