package liftinggoals.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import liftinggoals.models.RoutineModel;
import liftinggoals.models.WorkoutModel;

public class DeleteRoutineDialog extends AppCompatDialogFragment {
    private DeleteDialogListener listener;
    private int position;
    private Object item;

    public DeleteRoutineDialog(int position, Object item)
    {
        this.position = position;
        if (item instanceof RoutineModel)
        {
            this.item = (RoutineModel) item;
        }
        else if (item instanceof  WorkoutModel)
        {
            this.item = (WorkoutModel) item;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention")
                .setMessage("Do you wish to delete the swiped item?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelClicked(position, item);
                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteClicked(position, item);
                    }
                });

        return builder.create();
    }

    public interface DeleteDialogListener
    {
        void onDeleteClicked(int position, Object item);
        void onCancelClicked(int position, Object item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DeleteDialogListener");
        }
    }
}
