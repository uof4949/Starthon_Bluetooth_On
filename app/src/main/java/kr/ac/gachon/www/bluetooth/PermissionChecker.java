package kr.ac.gachon.www.bluetooth;

/**
 * Created by Kalios on 2017-08-26.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

class PermissionsChecker {
    private final Context context;

    public PermissionsChecker(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

}