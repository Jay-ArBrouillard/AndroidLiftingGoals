package liftinggoals.models;

public class UserRoutineModel {
    private int userRoutineId;
    private int userId;
    private int routineId;

    public UserRoutineModel()
    {
    }

    public int getUserRoutineId() {
        return userRoutineId;
    }

    public void setUserRoutineId(int userRoutineId) {
        this.userRoutineId = userRoutineId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }
}
