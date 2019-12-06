package liftinggoals.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.liftinggoals.R;

import java.util.ArrayList;

public class CreateExerciseDialog extends AppCompatDialogFragment implements Parcelable {
    private EditText exerciseName;
    private CreateExerciseDialog.CreateExerciseDialogListener listener;
    //Multiple choice dialog vars
    private String[] listItems;
    private boolean[] checkedItems;
    private ArrayList<String> userItems = new ArrayList<>();

    public CreateExerciseDialog ()
    {
    }

    protected CreateExerciseDialog(Parcel in) {
        listItems = in.createStringArray();
        checkedItems = in.createBooleanArray();
        userItems = in.createStringArrayList();
    }

    public static final Creator<CreateExerciseDialog> CREATOR = new Creator<CreateExerciseDialog>() {
        @Override
        public CreateExerciseDialog createFromParcel(Parcel in) {
            return new CreateExerciseDialog(in);
        }

        @Override
        public CreateExerciseDialog[] newArray(int size) {
            return new CreateExerciseDialog[size];
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        listItems = getResources().getStringArray(R.array.exercise_muscle_groups);
        checkedItems = new boolean[listItems.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if (isChecked)
                {
                    if (!userItems.contains(listItems[position]))
                    {
                        userItems.add(listItems[position]);
                    }
                    else
                    {
                        userItems.remove(position);
                    }
                }
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.workout_edit_create_exercise_dialog, null);

        builder.setView(view)
                .setTitle("New Exercise (select at least 1 muscle group)")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (userItems.size() == 0)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Must select atleast 1 muscle group", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String name = exerciseName.getText().toString();

                        if (name == null || name.length() == 0)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), "Please enter an exercise name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        listener.createExercise(name, userItems);
                    }
                })
                .setNeutralButton("clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++)
                        {
                            checkedItems[i] = false;
                        }
                        userItems.clear();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(listItems);
        dest.writeBooleanArray(checkedItems);
        dest.writeStringList(userItems);
    }

    public interface CreateExerciseDialogListener
    {
        void createExercise(String name, ArrayList<String> selectedMuscleGroups);
    }
}
