package liftinggoals.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import net.steamcrafted.loadtoast.LoadToast;
import liftinggoals.services.DefaultRoutineService;

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
                        Intent intent = new Intent(getActivity().getApplicationContext(), DefaultRoutineService.class);
                        intent.putExtra("username", username);
                        getActivity().startService(intent);
                    }
                });
        return builder.create();
    }
}
