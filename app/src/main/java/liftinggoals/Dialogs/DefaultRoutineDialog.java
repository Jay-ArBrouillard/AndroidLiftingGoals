package liftinggoals.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.airbnb.lottie.LottieAnimationView;

import net.steamcrafted.loadtoast.LoadToast;
import liftinggoals.services.DefaultRoutineService;

public class DefaultRoutineDialog extends AppCompatDialogFragment {
    private String username;
    private LottieAnimationView loadingAnim;

    public DefaultRoutineDialog(String username, LottieAnimationView loadingAnim)
    {
        this.username = username;
        this.loadingAnim = loadingAnim;
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
                        if (!loadingAnim.isAnimating())
                        {
                            loadingAnim.setVisibility(View.VISIBLE);
                            loadingAnim.playAnimation();
                        }
                        Intent intent = new Intent(getActivity().getApplicationContext(), DefaultRoutineService.class);
                        intent.putExtra("username", username);
                        getActivity().startService(intent);
                    }
                });
        return builder.create();
    }
}
