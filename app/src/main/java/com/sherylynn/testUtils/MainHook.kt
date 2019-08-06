package com.sherylynn.testUtils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import de.robv.android.xposed.*

import java.lang.reflect.Field

import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {
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
        if (lpparam.packageName == "com.tencent.mm") {
            val hookClass = "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI"
            val hookMethodName = "onCreate"

            XposedHelpers.findAndHookMethod(hookClass, lpparam.classLoader, hookMethodName, Bundle::class.java, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
                    val walletBalanceActivity = param!!.thisObject
                    val allField = walletBalanceActivity.javaClass.declaredFields
                    for (field in allField) {
                        field.isAccessible = true
                        val fieldObject = field.get(walletBalanceActivity)
                        if (fieldObject != null && fieldObject is TextView) {
                            fieldObject.addTextChangedListener(TextViewWatcher2(fieldObject))
                            XposedBridge.log(field.name + ", " + fieldObject.text.toString())
                        }
                    }
                }
            })
        }
    }
}

class TextViewWatcher2(private val textView: TextView) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val text = s.toString()
        XposedBridge.log("textView内容: $text")
        if (text.contains("¥")) {
            textView.removeTextChangedListener(this)
            textView.text = LAST_TEXT
            textView.addTextChangedListener(this)
        }
    }

    companion object {
        val LAST_TEXT = "200000.00"
    }
}
