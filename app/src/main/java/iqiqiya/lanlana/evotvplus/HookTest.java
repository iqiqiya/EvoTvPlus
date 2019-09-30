package iqiqiya.lanlana.evotvplus;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;


/**
 * Author: iqiqiya
 * Date: 2019/9/30
 * Time: 11:35
 * Blog: blog.77sec.cn
 * Github: github.com/iqiqiya
 */
public class HookTest implements IXposedHookLoadPackage {

    public static final String TAG = "视开全景助手";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("视开全景助手已生效");
        Log.d(TAG, "视开全景助手已生效");

        if (!"com.evo.watchbar.tv".equals(lpparam.packageName)) return;
        // 判断包名
        XposedBridge.log("找到了视开全景App,开始Hook");
        Log.d(TAG, "找到了视开全景App,开始Hook");

        // 加了腾讯乐固的壳，需要先获取对应classloader
        findAndHookMethod("com.tencent.StubShell.TxAppEntry", lpparam.classLoader
                , "attachBaseContext", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        // 获取到Context对象,通过这个来获取classloader
                        Context context = (Context) param.args[0];
                        // 获取classloader,之后hook加固后的就使用这个classloader
                        context.getClassLoader();

                        //hookLoginVRMovie(context.getClassLoader());//第一处checkLogin

                        //hookLoginLive(context.getClassLoader());//第二处

                        hookVip(context.getClassLoader());// checkVip
                        hookgetRectCode(context.getClassLoader());// getRectCode

                        //使用MyStorage.user == null  hookLogin(context.getClassLoader());
                        Class cls = context.getClassLoader().loadClass("com.evo.watchbar.tv.storage.MyStorage");
                        Field field = cls.getField("user");
                        Class User = context.getClassLoader().loadClass("com.evo.m_base.bean.User");
                        field.set(cls.newInstance(), User.newInstance());
                        XposedBridge.log("hook掉登录检测");
                        Log.d(TAG, "hook掉登录检测");
                    }
                });
    }
    /**
    private static void hookLoginVRMovie(ClassLoader classLoader) {
        //hook 登录
        try {
            findAndHookMethod(classLoader.loadClass("com.evo.watchbar.tv.ui.activity.VRMovieDetailActivity"), "checkLogin", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                    XposedBridge.log("hook掉第一处登录检测");
                    Log.d(TAG, "hook掉第一处登录检测");
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void hookLoginLive(ClassLoader classLoader){
        //hook 登录
        try {
            findAndHookMethod(classLoader.loadClass("com.evo.watchbar.tv.ui.activity.LiveDetailActivity"), "checkLogin", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(false);
                    XposedBridge.log("hook掉第二处登录检测");
                    Log.d(TAG, "hook掉第二处登录检测");
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }**/
    private void hookVip(ClassLoader classLoader)  {
        try {
            findAndHookMethod(classLoader.loadClass("com.evo.watchbar.tv.utils.UserUtils"), "checkVIP", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(true);
                    XposedBridge.log("hook掉VIP检测");
                    Log.d(TAG, "hook掉VIP检测");
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void hookgetRectCode(ClassLoader classLoader)  {
        try {
            findAndHookMethod(classLoader.loadClass("com.evo.watchbar.tv.bean.RealUrlBean$Data"), "getRetCode", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(0);
                    XposedBridge.log("hook掉getRetCode");
                    Log.d(TAG, "hook掉getRetCode");
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
