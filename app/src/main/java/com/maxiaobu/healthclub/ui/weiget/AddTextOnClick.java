package com.maxiaobu.healthclub.ui.weiget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Administrator on 2016/10/11.
 */

public abstract class AddTextOnClick implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextClick(s);
    }

    public abstract void onTextClick(Editable s);

}
