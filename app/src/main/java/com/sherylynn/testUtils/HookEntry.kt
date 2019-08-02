package com.sherylynn.testUtils

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by zpp0196 on 2018/6/23 0023.
 */

class HookEntry : IXposedHookLoadPackage {

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == BuildConfig.APPLICATION_ID) {
            XposedHelpers.findAndHookMethod(
                    MainActivity::class.java.name,
                    lpparam.classLoader,
                    "toastMessage",
                    XC_MethodReplacement.returnConstant("I had Hooked!!!!")
            )
        }
    }
}