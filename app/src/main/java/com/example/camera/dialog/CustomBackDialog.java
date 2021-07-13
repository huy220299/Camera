package com.example.camera.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.camera.R;

public class CustomBackDialog extends Dialog implements
    android.view.View.OnClickListener {

  public Activity c;
  public Dialog d;
  public RelativeLayout yes, no;
  public String text;

  public CustomBackDialog(Activity a) {
    super(a);
    // TODO Auto-generated constructor stub
    this.c = a;
  }
  public CustomBackDialog(Activity a,String text) {
    super(a);
    // TODO Auto-generated constructor stub
    this.c = a;
    this.text =text;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.custom_alert_dialog);
    TextView textView = findViewById(R.id.txt_dia);
    if (text!=null){
      textView.setText(text);
    }
    yes =  findViewById(R.id.btn_yes);
    no =  findViewById(R.id.btn_no);
    yes.setOnClickListener(this);
    no.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.btn_yes:
      c.finish();
      break;
    case R.id.btn_no:
      dismiss();
      break;
    default:
      break;
    }
    dismiss();
  }
}