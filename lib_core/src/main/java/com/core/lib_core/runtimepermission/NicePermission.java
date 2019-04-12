package com.core.lib_core.runtimepermission;

import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author 李澄锋<br>
 * 2018/8/29
 */
public class NicePermission {

    private FragmentActivity activity;
    private NicePermissionListener permissionListener = null;

    public NicePermission(FragmentActivity activity, NicePermissionListener permissionListener) {
        this.activity = activity;
        this.permissionListener = permissionListener;
    }

    public void requestPermission(final String... permissons) {
        requestSet(permissons);

    }
//        private void requestOne(final String permisson) {
//            RxPermissions rxPermissions = new RxPermissions(AbstractActivity.this);
//            rxPermissions
//                    .request(permisson).subscribe(new Action1<Boolean>() {
//                @Override
//                public void call(Boolean aBoolean) {
//                    onGranted(aBoolean);
//                    ToastUtil.showToastMessage(AbstractActivity.this, permisson.toString() + ", aBoolean = " + aBoolean);
//
//                }
//            });
//        }

    private void requestSet(String[] permissons) {

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(permissons).subscribe(granted -> {
            if (granted) {
                // All requested permissions are granted
                if (permissionListener != null) {
                    permissionListener.onGranted();
                }
            } else {
                // At least one permission is denied
                if (permissionListener != null) {
                    permissionListener.onRefused();
                }
            }
        });
//        rxPermissions.request(permissons).subscribe(new Subscriber<Boolean>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Boolean granted) {
//                if (granted) {  // All requested permissions are granted
//                    if (permissionListener != null) {
//                        permissionListener.onGranted();
//                    }
//                } else {  // At least one permission is denied
//                    if (permissionListener != null) {
//                        permissionListener.onRefused();
//                    }
//                }
////                    NiceLogUtil.i("RxPermissions" + " , permission.granted = " + permission.granted + " , permission.shouldShowRequestPermissionRationale = "
////                            + permission.shouldShowRequestPermissionRationale + " , permission.name = " + permission.name + " , permission.toString() = " + permission.toString());
//            }
//        });
    }
}
