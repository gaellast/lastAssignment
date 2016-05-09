package fr.gaellast.lastassignment.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.gaellast.lastassignment.R;

/**
 * Created by gaellast on 08/05/2016.
 */
public class AdapteurSelectedApp extends RecyclerView.Adapter<AdapteurSelectedApp.ViewHolder> {

    public Context context;
    private List<AppData> _data;
    private ListApp listApp;

    public AdapteurSelectedApp(Context _context, ArrayList<AppData> _infoApp, ListApp _listApp) {
        this.context = _context;
        this._data = _infoApp;
        this.listApp= _listApp;
    }

    public class            ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView    IVItem_app;
        public TextView     TVItem_app;
        public Button       BDeleteItem_app;
        public String       packageName;
        public int          position;

        public ViewHolder(View itemView) {
            super(itemView);
            TVItem_app = (TextView) itemView.findViewById(R.id.TVItem_app);
            IVItem_app = (ImageView) itemView.findViewById(R.id.IVItem_app);
            BDeleteItem_app = (Button) itemView.findViewById(R.id.BDelete_item_app);
            BDeleteItem_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listApp.removeApp(packageName);
                    _data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, _data.size());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null)
                context.startActivity(intent);
            else
                Toast.makeText(context, "can't start", Toast.LENGTH_SHORT);
        }
    }

    public void addApp(AppData appData) {
        this._data.add(appData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.packageName = _data.get(position).packageName;
        holder.TVItem_app.setText(_data.get(position).appName);
        holder.IVItem_app.setImageDrawable(_data.get(position).image);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return this._data.size();
    }
}
