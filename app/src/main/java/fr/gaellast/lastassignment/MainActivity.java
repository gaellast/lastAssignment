package fr.gaellast.lastassignment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fr.gaellast.lastassignment.model.AdapterAllApp;
import fr.gaellast.lastassignment.model.AdapteurSelectedApp;
import fr.gaellast.lastassignment.model.AppData;
import fr.gaellast.lastassignment.model.ListApp;

public class        MainActivity extends AppCompatActivity {

    private Dialog  dialog;
    private ListApp listApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("preference", Context.MODE_PRIVATE);
        listApp = new ListApp(preferences);
        this.initView();
    }

    /**
     * initialise la vue de mainActivity et de la dialog box
     */
    private void initView() {
        Button BAddItem = (Button) findViewById(R.id.BAddAppActivityMain);

        initRecyclerViewApp();
        createDialog();
        assert BAddItem != null;
        BAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentRecyclerView();
                dialog.show();
            }
        });
    }

    /**
     * initialise la view de la recyclerView
     */
    private void initRecyclerViewApp() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RVAcitivtyMain);
        ArrayList<AppData> infoApp = new ArrayList<>();
        AppData item;
        int     i = 0;
        JSONArray jsonArray = listApp.getJsonArray();
        PackageManager pm = getPackageManager();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);

        while (i < jsonArray.length()) {
            item = new AppData();
            try {
                item.packageName = (String)jsonArray.get(i);
                item.appName = pm.getApplicationLabel(pm.getApplicationInfo(item.packageName, 0)).toString();
                item.image = pm.getApplicationIcon(item.packageName);
                infoApp.add(item);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("applicationIcon", e.getMessage());
            } catch (JSONException e) {
                Log.e("get apppackage error", e.getMessage());
            }
            ++i;
        }
        AdapteurSelectedApp adapteur = new AdapteurSelectedApp(getApplicationContext(), infoApp,
                listApp);
        recyclerView.setAdapter(adapteur);
    }

    /**
     * ajoute les nouvelles apps a la recycler a la volée.
     * clean la List<> pour eviter les ajouts en doublon
     * @param listPackage
     */
    private void addAppToRecycler(List<String> listPackage) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RVAcitivtyMain);
        AdapteurSelectedApp adapter = (AdapteurSelectedApp) recyclerView.getAdapter();
        AppData appData;
        int     i = 0;
        PackageManager pm = getPackageManager();

        while (i < listPackage.size()) {
            appData = new AppData();

            appData.packageName = listPackage.get(i);
            try {
                appData.appName = pm.getApplicationLabel(pm.getApplicationInfo(appData.packageName, 0)).toString();
                appData.image = pm.getApplicationIcon(appData.packageName);
                adapter.addApp(appData);
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("error ad app", e.getMessage());
            }
            ++i;
        }
    }

    /**
     *              DialogBox
     *
     * creation de la dialogBox pout ajouter des apps a nos preference
     * initialise sa vue (comprend les boutons aussi)
     */
    private void createDialog() {
        this.dialog = new Dialog(MainActivity.this);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_app);
        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.RVDialodApp);
        Button BCancel = (Button) dialog.findViewById(R.id.BCancelDialogApp);
        Button BValidate = (Button) dialog.findViewById(R.id.BValidateDialogApp);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        BCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((AdapterAllApp)recyclerView.getAdapter()).selected.clear();
            }
        });
        BValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterAllApp adapterAllApp = (AdapterAllApp)recyclerView.getAdapter();
                List<String> listPackage = adapterAllApp.selected;

                if (listPackage.size() < 1)
                    return;

                dialog.cancel();

                int     i = 0;

                while (i < listPackage.size()) {
                    listApp.addApp(listPackage.get(i));
                    ++i;
                }
                listApp.saveData();
                addAppToRecycler(listPackage);
            }
        });
    }

    /**
     *              DialogBox
     *
     * initialise / met a jour la recyclerView de la dialogBox
     */
    private void setContentRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.RVDialodApp);
        PackageManager pm = this.getPackageManager();

        assert recyclerView != null;
        recyclerView.removeAllViews();
        List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<AppData> infoApp = new ArrayList<>();
        AppData item;
        int     i = 0;
        clearList(list);

        while (i < list.size()) {
            item = new AppData();
            try {
                item.packageName = list.get(i).packageName;
                item.appName = pm.getApplicationLabel(pm.getApplicationInfo(item.packageName, 0)).toString();
                item.image = pm.getApplicationIcon(list.get(i).packageName);
                infoApp.add(item);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("applicationIcon", e.getMessage());
            }
            ++i;
        }
        AdapterAllApp myAdapter = new AdapterAllApp(infoApp);
        recyclerView.setAdapter(myAdapter);
    }

    /**
     *              DialogBox
     *
     * supprime les application deja recuperer pour qu'elle ne soit pas afficher
     */
    private void clearList(List<ApplicationInfo> list) {
        int     i = 0;

        while (i < list.size()) {
            if (listApp.alredyHere(list.get(i).packageName)) {
                list.remove(i);
                --i;
            }
            ++i;
        }
    }
}
