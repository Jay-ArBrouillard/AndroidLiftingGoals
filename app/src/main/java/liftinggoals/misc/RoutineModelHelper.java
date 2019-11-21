package liftinggoals.misc;

import android.content.Context;

import java.util.ArrayList;

import liftinggoals.classes.ExerciseModel;
import liftinggoals.classes.RoutineModel;
import liftinggoals.classes.RoutineWorkoutModel;
import liftinggoals.classes.WorkoutExerciseModel;
import liftinggoals.classes.WorkoutModel;
import liftinggoals.data.DatabaseHelper;

public class RoutineModelHelper {
    private static DatabaseHelper db;

    public static ArrayList<RoutineModel> populateRoutineModels(Context context)
    {
        db = new DatabaseHelper(context);
        db.openDB();

        ArrayList<RoutineModel> routineModels = (ArrayList<RoutineModel>) db.getAllRoutines();
        if (routineModels == null) routineModels = new ArrayList<>();
        for (int i = 0; i < routineModels.size(); i++)
        {
            ArrayList<WorkoutModel> listOfWorkouts = new ArrayList<>();
            int routineId = routineModels.get(i).getRoutineId();

            ArrayList<RoutineWorkoutModel> routineWorkoutList = (ArrayList<RoutineWorkoutModel>) db.getRoutineWorkoutsByRoutineId(routineId);
            if (routineWorkoutList == null) routineWorkoutList = new ArrayList<>();

            WorkoutModel workout = null;
            for (int j = 0; j < routineWorkoutList.size(); j++)
            {
                int workoutId = routineWorkoutList.get(j).getWorkoutId();
                workout = db.getWorkout(workoutId);
                listOfWorkouts.add(workout);
                ArrayList<WorkoutExerciseModel> workoutExerciseList = (ArrayList<WorkoutExerciseModel>) db.getAllWorkoutExercisesByWorkoutId(workoutId);
                if (workoutExerciseList == null) workoutExerciseList = new ArrayList<>();

                for (int k = 0; k < workoutExerciseList.size(); k++)
                {
                    int exerciseId = workoutExerciseList.get(k).getExerciseId();
                    ExerciseModel exerciseModel = db.getExercise(exerciseId);
                    workoutExerciseList.get(k).setExercise(exerciseModel);
                }

                listOfWorkouts.get(j).setExercises(workoutExerciseList);
            }

            routineModels.get(i).setWorkouts(listOfWorkouts);
        }

        db.closeDB();

        return routineModels;
    }
}
