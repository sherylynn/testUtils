package com.sherylynn.testUtils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by zpp0196 on 2018/6/23 0023.
 */

class HookEntry : IXposedHookLoadPackage {

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        /*
        if (lpparam.packageName == BuildConfig.APPLICATION_ID) {
            XposedHelpers.findAndHookMethod(
                    MainActivity::class.java.name,
                    lpparam.classLoader,
                    "toastMessage",
                    XC_MethodReplacement.returnConstant("I had Hooked!!!!")
            )
        }else
         */
         if(lpparam?.packageName=="com.tencent.mm"){
            var hookClass="com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI";
            var hookMethodName="onCreate";
            XposedHelpers.findAndHookMethod(
                    hookClass,
                    lpparam.classLoader,
                    hookMethodName,
                    Bundle::class,
                    object: XC_MethodHook(){
                        override fun afterHookedMethod(param: MethodHookParam?) {
                            var walletBalanceActivity=param!!.thisObject;
                            var allField=walletBalanceActivity.javaClass.declaredFields;
                            for (field in allField){
                                field.isAccessible=true;
                                var fieldObject = field.get(walletBalanceActivity);
                                if(fieldObject!=null && fieldObject is TextView){
                                    fieldObject.addTextChangedListener(TextViewWatcher (fieldObject));
                                    XposedBridge.log(field.getName() + ", " + fieldObject.getText().toString());
                                }
                            }
                        }
                    }
            );
        }
    }
}
class TextViewWatcher(private val textView: TextView) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable) {
        var text =p0.toString()
        XposedBridge.log("textView内容: " + text);
        if(text.contains("¥")){
            textView.removeTextChangedListener(this)
            textView.text=LAST_TEXT
            textView.addTextChangedListener(this)
        }
    }
    companion object{
        val LAST_TEXT="20000.00";
    }
}