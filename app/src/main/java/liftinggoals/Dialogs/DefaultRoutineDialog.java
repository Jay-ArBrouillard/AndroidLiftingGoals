package liftinggoals.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import net.steamcrafted.loadtoast.LoadToast;
import liftinggoals.Services.RoutineService;

public class DefaultRoutineDialog extends AppCompatDialogFragment {
    private String username;
    private LoadToast loadToast;

    public DefaultRoutineDialog(String username, LoadToast loadToast)
    {
        this.username = username;
        this.loadToast = loadToast;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention!")
                .setMessage("Do you want to reset all default routines? Resetting default routines will delete any changes you made to them")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadToast.show();
                        Intent intent = new Intent(getActivity().getApplicationContext(), RoutineService.class);
                        intent.putExtra("username", username);
                        getActivity().startService(intent);
                    }
                });
        return builder.create();
    }
}
