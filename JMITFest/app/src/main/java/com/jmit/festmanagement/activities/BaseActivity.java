package com.jmit.festmanagement.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.CustomDialogFragment;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.VolleyInterface;

public class BaseActivity extends AppCompatActivity implements VolleyInterface {
    boolean showing = false;
    CustomDialogFragment alertDialog;

    public void showDialog() {
        showing = true;
        if (alertDialog == null) {
            alertDialog = new CustomDialogFragment();
        }

        if (showing)
        {
            FragmentManager fm = getSupportFragmentManager();
            alertDialog.show(fm, "fragment_edit_name");
        }
    }

    public void dismissDialog() {
        showing = false;
        if (alertDialog!=null && alertDialog.isVisible())
            alertDialog.dismissAllowingStateLoss();
    }
    void startActivity(Class c){
        Intent newIntent = new Intent(this, c);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    @Override
    public void requestStarted(int requestCode) {
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        FLog.d(response);
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        FLog.d(error.getMessage());
        String msg = "";
        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
            msg = getResources().getString(R.string.network_error);
        } else if (error instanceof ParseError) {
            msg = "Response Parse Error";
        } else if (error instanceof AuthFailureError) {
            msg = "Authentication Failure Error";
        } else if (error instanceof ServerError) {
            msg = "Server Error";
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
