package com.maxiaobu.healthclub.ui.weiget.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.maxiaobu.healthclub.R;

import java.util.List;


/**
 * Created by 莫小婷 on 2016/12/2.
 */

public class CustomDialog {

    private Context mContext;
    private TextView positive;
    private TextView cancle;
    private TextView dialog_title;
    private TextView dialog_content;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;

    private SetOnClick mSetOnClick;
    /**
     * 默认的dialog
     * @param context
     */
    public CustomDialog(Context context, SetOnClick setOnClick) {

        mContext = context;
        mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_group_cancle, null);
        mBuilder.setView(view);
        positive = (TextView) view.findViewById(R.id.group_go);
        cancle = (TextView) view.findViewById(R.id.group_cancle);
        dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        mSetOnClick = setOnClick;
    }


    /**
     * ListDialog
     */
    public CustomDialog(Context context , List<String> data, SetOnClick setOnClick){


    }

    public void setTitle(String title) {
        dialog_title.setText(title);

    }

    public void setTitleColor(int color) {
        dialog_title.setTextColor(color);
    }


    public void setContent(String content) {
        dialog_content.setText(content);
    }

    public void setContentColor(int color) {
        dialog_content.setTextColor(color);
    }

    public void setPositionButtonText(String text) {

        positive.setText(text);
    }

    public void setCancleButtonText(String text) {

        cancle.setText(text);
    }

    public void create() {
        mDialog = createDialog();
    }

    public void show(){
        mDialog.show();
    }

    public AlertDialog createDialog() {

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, positive.getText().toString(), Toast.LENGTH_SHORT).show();
                mSetOnClick.onPosition(v,mDialog);
                cancleDialog();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, cancle.getText().toString(), Toast.LENGTH_SHORT).show();
                mSetOnClick.onCancle(v,mDialog);
                cancleDialog();
            }
        });

        AlertDialog dialog =  mBuilder.create();

        return dialog;

    }

    public void cancleDialog() {
        mDialog.dismiss();
    }

    public interface SetOnClick{

        void onPosition(View view, AlertDialog dialog);

        void onCancle(View view, AlertDialog dialog);
    }


}
