package liftinggoals.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.liftinggoals.R;

public class CreateExerciseDialog extends AppCompatDialogFragment {
    private EditText exerciseName;
    private CreateExerciseDialog.CreateExerciseDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.workout_edit_create_exercise_dialog, null);

        builder.setView(view)
                .setTitle("New Exercise")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = exerciseName.getText().toString();
                        listener.applyChanges(name);
                    }
                });

        exerciseName = view.findViewById(R.id.workout_edit_create_exercise_name);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateExerciseDialog.CreateExerciseDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CreateExerciseDialog");
        }
    }

    public interface CreateExerciseDialogListener
    {
        void applyChanges(String name);
    }
}
