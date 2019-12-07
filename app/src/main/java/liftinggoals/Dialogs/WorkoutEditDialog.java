package liftinggoals.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import liftinggoals.activities.WorkoutEditActivity;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.services.WorkoutExerciseService;

public class WorkoutEditDialog extends AppCompatDialogFragment implements Parcelable {
    private EditText minimumSets;
    private EditText minimumReps;
    private EditText maximumSets;
    private EditText maximumReps;
    private EditText weight;
    private WorkoutEditDialogListener listener;
    private int position;
    private WorkoutExerciseModel model;

    public WorkoutEditDialog()
    {
    }

    public WorkoutEditDialog(int position, WorkoutExerciseModel model)
    {
        this.position = position;
        this.model = model;
    }

    public WorkoutEditDialog(Parcel in) {
        position = in.readInt();
    }

    public static final Creator<WorkoutEditDialog> CREATOR = new Creator<WorkoutEditDialog>() {
        @Override
        public WorkoutEditDialog createFromParcel(Parcel in) {
            return new WorkoutEditDialog(in);
        }

        @Override
        public WorkoutEditDialog[] newArray(int size) {
            return new WorkoutEditDialog[size];
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.workout_edit_dialog, null);

        builder.setView(view)
                .setTitle("Exercise Details")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String minSets = minimumSets.getText().toString();
                        String maxSets = maximumSets.getText().toString();
                        String minReps = minimumReps.getText().toString();
                        String maxReps =  maximumReps.getText().toString();
                        String strWeight = weight.getText().toString();

                        WorkoutExerciseModel workoutExerciseModel;
                        try {
                            workoutExerciseModel = new WorkoutExerciseModel();
                            workoutExerciseModel.setExerciseId(model.getExerciseId());
                            if (!isNullOrEmpty(minSets)) { workoutExerciseModel.setMinimumSets(Integer.parseInt(minSets)); }
                            if (!isNullOrEmpty(minReps)) { workoutExerciseModel.setMinimumReps(Integer.parseInt(minReps)); }
                            if (!isNullOrEmpty(maxSets)) { workoutExerciseModel.setMaximumSets(Integer.parseInt(maxSets)); }
                            if (!isNullOrEmpty(maxReps)) { workoutExerciseModel.setMaximumReps(Integer.parseInt(maxReps)); }
                            if (!isNullOrEmpty(strWeight)) { workoutExerciseModel.setIntensity(Double.parseDouble(strWeight)); }

                            if (model.getIntensity() == 0 || model.getIntensity() == 0.0) { model.setIntensity(0); }
                            if (workoutExerciseModel.getIntensity() == 0 || workoutExerciseModel.getIntensity() == 0.0) { workoutExerciseModel.setIntensity(0); }

                            if (isNullOrEmpty(minSets) && isNullOrEmpty(minReps) && isNullOrEmpty(maxSets) && isNullOrEmpty(maxReps) && isNullOrEmpty(strWeight))
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "You entered no data!", Toast.LENGTH_SHORT).show();
                            }
                            else if (model.getMinimumSets() == workoutExerciseModel.getMinimumSets() &&
                                     model.getMinimumReps() == workoutExerciseModel.getMinimumReps() &&
                                     model.getMaximumSets() == workoutExerciseModel.getMaximumSets() &&
                                     model.getMaximumReps() == workoutExerciseModel.getMaximumReps() &&
                                     model.getIntensity() == workoutExerciseModel.getIntensity())
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "You made no new changes!", Toast.LENGTH_SHORT).show();
                            }
                            else if (workoutExerciseModel.getMinimumSets() < 0 || workoutExerciseModel.getMinimumReps() < 0 || workoutExerciseModel.getMaximumSets() < 0 ||
                                    workoutExerciseModel.getMaximumReps() < 0 || workoutExerciseModel.getIntensity() < 0)
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "You cannot enter negative values!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                listener.editExerciseDetails(position, workoutExerciseModel);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "Must enter a number for Sets,Reps,intensity", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        minimumSets = view.findViewById(R.id.workout_edit_dialog_min_sets);
        maximumSets = view.findViewById(R.id.workout_edit_dialog_max_sets);
        minimumReps = view.findViewById(R.id.workout_edit_dialog_min_reps);
        maximumReps = view.findViewById(R.id.workout_edit_dialog_max_reps);
        weight = view.findViewById(R.id.workout_edit_dialog_intensity);

        if (model.getMinimumSets() != 0) { minimumSets.setText(String.valueOf(model.getMinimumSets())); }
        if (model.getMinimumReps() != 0) { minimumReps.setText(String.valueOf(model.getMinimumReps())); }
        if (model.getMaximumSets() != 0) { maximumSets.setText(String.valueOf(model.getMaximumSets())); }
        if (model.getMaximumReps() != 0) { maximumReps.setText(String.valueOf(model.getMaximumReps())); }
        if (model.getIntensity() != 0) { weight.setText(Double.toString(model.getIntensity())); }

        return builder.create();
    }

    private boolean isNullOrEmpty(String str)
    {
        if (str == null || str.isEmpty())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (WorkoutEditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement WorkoutEditDialog");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
    }

    public interface WorkoutEditDialogListener
    {
        void editExerciseDetails(int position, WorkoutExerciseModel model);
    }
}
