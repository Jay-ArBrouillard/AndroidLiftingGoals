package liftinggoals.misc;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.data.RoutineTable;
import liftinggoals.data.UserRoutinesTable;
import liftinggoals.models.ExerciseModel;
import liftinggoals.models.RoutineModel;
import liftinggoals.models.RoutineWorkoutModel;
import liftinggoals.models.UserModel;
import liftinggoals.models.UserRoutineModel;
import liftinggoals.models.WorkoutExerciseModel;
import liftinggoals.models.WorkoutModel;
import liftinggoals.data.DatabaseHelper;

public class RoutineModelHelper {
    private static DatabaseHelper db;

    public static ArrayList<RoutineModel> populateRoutineModels(Context context, int userId)
    {
        db = new DatabaseHelper(context);
        db.openDB();

        for (UserModel u : db.getAllUsers())
        {
            System.out.println("user: "  + u.getUsername() + " : isAdmin ->" + u.getIsAdmin());

        }

        UserModel thisUser = db.getUser(userId);
        ArrayList<RoutineModel> routineModels;

        if (thisUser != null && thisUser.getIsAdmin() == 1)
        {
            routineModels = (ArrayList<RoutineModel>) db.getAllRoutines();
        }
        else
        {
            routineModels = (ArrayList<RoutineModel>) db.getAllRoutinesForUser(userId);
        }

        if (routineModels == null) routineModels = new ArrayList<>();

        for (int i = 0; i < routineModels.size(); i++)
        {
            ArrayList<WorkoutModel> listOfWorkouts = new ArrayList<>();
            int routineId = routineModels.get(i).getRoutineId();

            ArrayList<RoutineWorkoutModel> routineWorkoutList = (ArrayList<RoutineWorkoutModel>) db.getRoutineWorkoutsByRoutineId(routineId);
            if (routineWorkoutList == null) routineWorkoutList = new ArrayList<>();

            for (int j = 0; j < routineWorkoutList.size(); j++)
            {
                int workoutId = routineWorkoutList.get(j).getWorkoutId();
                listOfWorkouts.add(db.getWorkout(workoutId));
                ArrayList<WorkoutExerciseModel> workoutExerciseList = (ArrayList<WorkoutExerciseModel>) db.getAllWorkoutExercisesByWorkoutId(workoutId);
                if (workoutExerciseList == null) workoutExerciseList = new ArrayList<>();

                for (int k = 0; k < workoutExerciseList.size(); k++)
                {
                    int exerciseId = workoutExerciseList.get(k).getExerciseId();
                    ExerciseModel exerciseModel = db.getExercise(exerciseId);
                    workoutExerciseList.get(k).setExercise(exerciseModel);
                }

                listOfWorkouts.get(j).setNumberExercises(workoutExerciseList.size());   //Added so numExercisesDisplays right
                listOfWorkouts.get(j).setExercises(workoutExerciseList);
            }

            routineModels.get(i).setWorkouts(listOfWorkouts);
        }

        db.closeDB();

        return routineModels;
    }
}
