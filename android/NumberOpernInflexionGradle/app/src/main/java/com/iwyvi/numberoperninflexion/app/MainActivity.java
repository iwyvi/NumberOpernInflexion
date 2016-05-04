package com.iwyvi.numberoperninflexion.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private EditText et = null;
    private TextView tv = null;
    private Spinner sp1 = null;
    private Spinner sp2 = null;
    private ChangeContent cc = null;
    private static Boolean isExit = false;
    private static Boolean autoCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText)findViewById(R.id.editText);
        tv = (TextView)findViewById(R.id.textView);
        sp1 = (Spinner)findViewById(R.id.spinner1);
        sp2 = (Spinner)findViewById(R.id.spinner2);
        cc = new ChangeContent();

        SharedPreferences settings = this.getSharedPreferences("settings", MODE_PRIVATE);
        autoCopy = settings.getBoolean("autocopy",false);

        View btn = findViewById(R.id.btnFloatingAction);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button_click(v);
            }
        });

        tv.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                String content = tv.getText().toString();
                if("".equals(content)){
                    Toast.makeText(getApplicationContext(), "无复制内容", Toast.LENGTH_SHORT).show();
                }else {
                    copyToClipboard(tv.getText().toString());
                    Toast.makeText(getApplicationContext(), "数字谱已复制", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_autocopy).setChecked(autoCopy);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            //Toast.makeText(getApplicationContext(), "小纯洁 2016.1.30", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //    设置Title的内容
            builder.setTitle("关于");
            //    设置Content来显示一个信息
            builder.setMessage("第一次做安卓，根本不会做界面" + "\n" + "by:小纯洁(IwYvI)" + "\n" + "2016.1.30");
            //    设置一个PositiveButton
            builder.setPositiveButton("确定", null);
            builder.show();
            return true;
        }else if(id == R.id.action_autocopy){
            item.setChecked(!item.isChecked());
            autoCopy = item.isChecked();

            SharedPreferences settings = this.getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor localEditor = settings.edit();
            localEditor.putBoolean("autocopy",autoCopy);
            localEditor.commit();
            return true;
        }else if(id == R.id.action_cleanall){
            et.setText("");
            tv.setText("");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethod.SHOW_EXPLICIT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    public void button_click(View view){
        String result = cc.change(et.getText().toString(),sp1.getSelectedItemPosition() - sp2.getSelectedItemPosition());
        if(!"".equals(result)){
            tv.setText(result);
            if(autoCopy){
                copyToClipboard(result);
                Toast.makeText(this, "转换后的数字谱已复制", Toast.LENGTH_SHORT).show();
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0) ;
        }
    }

    public void copyToClipboard(String data){
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("opern",data);
        clipboard.setPrimaryClip(clip);
    }

}
