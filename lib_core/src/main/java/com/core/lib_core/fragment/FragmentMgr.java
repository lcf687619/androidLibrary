package com.core.lib_core.fragment;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.core.lib_core.utils.NiceLogUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主要用来管理Fragment<br>
 * 全局的：方法都是static类型、主要用在{@link BaseFragment}、{@link FrameBaseFragment} 中
 *
 * @author coffee
 */
@SuppressLint("UseSparseArrays")
public class FragmentMgr {
    private FragmentActivity context;

    private FragmentMgr() {
    }

    /**
     * key：Fragment的类型：如ChatFragment.class value： Fragment对象：new ChatFragment()
     */
    private ArrayList<FrameBaseFragment> fragments = new ArrayList<FrameBaseFragment>();
    /**
     * key：Fragment的类型：如ChatFragment.class value： 盛放Fragment的view容器
     */
    private HashMap<Integer, FrameBaseFragment> viewsContainer = new HashMap<Integer, FrameBaseFragment>();

    private static HashMap<String, FragmentMgr> instances = new HashMap<String, FragmentMgr>();

    private FragmentTransaction trac;

    public static FragmentMgr getInstance(FragmentActivity context) {
        String key = context.getClass().getSimpleName();
        FragmentMgr instance = instances.get(key);
        if (instance == null) {
            instance = new FragmentMgr();
            instances.put(key, instance);
            instance.context = context;
        }
        return instance;
    }

    public static synchronized HashMap<String, FragmentMgr> getAllInstance() {
        HashMap<String, FragmentMgr> alls = new HashMap<String, FragmentMgr>();
        synchronized (alls) {
            alls.putAll(instances);
        }
        return alls;
    }

    /**
     * 介意FragmengActivity 销毁的时候，调用
     */
    public void onDestroy(FragmentActivity context) {
        String key = context.getClass().getSimpleName();
        FragmentMgr instance = instances.get(key);
        if (instance != null) {
            instance.removeAlls();
            instances.remove(key);// 删除
        }
    }

    private FragmentActivity getActivity() {
        return this.context;
    }

    /**
     * @param context
     * @param containerViewId
     * @param fragment
     */
    public void add(FragmentActivity context, int containerViewId, FrameBaseFragment fragment) {
        Class<? extends FrameBaseFragment> fragmentClass = fragment.getClass();
        if (trac == null) {// 每次都重新
            FragmentManager fm = context.getSupportFragmentManager();
            trac = fm.beginTransaction();
            trac.add(containerViewId, fragment, fragmentClass.getName());
            trac.commitAllowingStateLoss();
            trac = null;
        } else { // 批量
            trac.add(containerViewId, fragment, fragmentClass.getName());
            if (fragment.isToBackStack()) {
                trac.addToBackStack(null);
            }
        }
        viewsContainer.put(containerViewId, fragment);
    }

    /************************************ fragment管理 *******************************************/
    /**
     * Fragment移除 一般只需要传入this对象
     */
    public void remove(Fragment fragment) {
        if (fragment == null || getActivity() == null) {
            return;
        }
        try {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trac = manager.beginTransaction();
            trac.remove(fragment);
            trac.commitAllowingStateLoss();
        } catch (Exception e) {
            // java.lang.IllegalStateException: Activity has been destroyed
            NiceLogUtil.w("fragment：" + e.getMessage(), e);
        }
    }

    /**
     * 销毁Fragment 、清空 {@link #fragments} {@link #viewsContainer}
     */
    public void removeAlls() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        int backStackCount = manager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = manager.getBackStackEntryAt(i).getId();
            manager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //
        fragments.clear();
        viewsContainer.clear();
    }

    /**
     * 加载view
     * <p>
     * 传入一个R.id.main_fragment_开头的变量
     *
     * @param fragment
     */

    public void replace(FrameBaseFragment fragment, int... containerViewIds) {
        if (getActivity() == null) {
            return;
        }
        int containerViewId = -1;
        if (containerViewIds.length > 0) {
            containerViewId = containerViewIds[0];
        } else {
            containerViewId = getActivity().getResources().getIdentifier("main_content", "id", getActivity().getPackageName());
        }
        View viewGroup = getActivity().findViewById(containerViewId);
        if (viewGroup == null) {
            NiceLogUtil.e("fragment：replace fragment 失败");
            return;
        } else {
            viewGroup.setVisibility(View.VISIBLE);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trac = manager.beginTransaction();
            trac.replace(containerViewId, fragment, fragment.getClass().getName());
            if (fragment.isToBackStack()) {
                trac.addToBackStack(null);
            }
            trac.commitAllowingStateLoss();
        }
        //
        viewsContainer.put(containerViewId, fragment);
    }

    /**
     * 叠加
     *
     * @param containerViewIds
     * @param fragment
     */
    public void overlying(FrameBaseFragment fragment, int... containerViewIds) {
        if (getActivity() == null) {
            return;
        }
        int containerViewId = -1;
        if (containerViewIds.length > 0) {
            containerViewId = containerViewIds[0];
        } else {
            containerViewId = getActivity().getResources().getIdentifier("main_content", "id", getActivity().getPackageName());
        }
        View viewGroup = getActivity().findViewById(containerViewId);
        if (viewGroup == null) {
            return;
        } else {
            viewGroup.setVisibility(View.VISIBLE);
            FragmentManager manager = getActivity().getSupportFragmentManager();
            Fragment old = manager.findFragmentByTag(fragment.getClass().getName());
            // 已经存在一个同名的Fragment
            if (old != null) {
                // replace(containerViewId, fragment);
                remove(old);
            }
            FragmentTransaction trac = manager.beginTransaction();
            trac.add(containerViewId, fragment, fragment.getClass().getName());
            if (fragment.isToBackStack()) {
                trac.addToBackStack(null);
            }
            trac.commitAllowingStateLoss();
        }
    }

    /**
     * 后退
     */
    public void back() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack();
    }

    /**
     * 获取MainFragment管理的所有Fragment<br>
     * 注意需要返回一个副本。否则便利的时候可能会抛异常
     *
     * @return
     */
    public ArrayList<FrameBaseFragment> getAllFragments() {
        ArrayList<FrameBaseFragment> items = new ArrayList<FrameBaseFragment>();
        items.addAll(fragments);
        return items;
    }

    /**
     * 注意以下两个方法要在Fragment的生命周期里。管理。需要手动调用
     *
     * @param fragment
     */
    public void addToContainer(FrameBaseFragment fragment) {
        fragments.add(fragment);
    }

    public void removeFromContainer(FrameBaseFragment fragment) {
        fragments.remove(fragment);
        for (Integer key : viewsContainer.keySet()) {
            if (viewsContainer.get(key).equals(fragment)) {
                viewsContainer.remove(fragment);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getFragment(Class<? extends FrameBaseFragment> fragmentClass) {
        if (fragmentClass != null) {
            if (fragments.toString().contains(fragmentClass.getName())) {
                for (FrameBaseFragment fragment : fragments) {
                    if (fragment.toString().contains(fragmentClass.getName())) {
                        return (T) fragment;
                    }
                }
            }
        }
        return null;
    }

    public FrameBaseFragment getFragment(int viewContainerId) {
        return viewsContainer.get(viewContainerId);
    }

}
