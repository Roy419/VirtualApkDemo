package org.zdnuist.v;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

import org.zdnuist.library.a.CommonUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  private Button btn1,btn2,btn3,btn4;
  private static final int PERMISSION_REQUEST_CODE_STORAGE = 20171222;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btn1 = (Button) findViewById(R.id.button);
    btn1.setOnClickListener(this);
    btn2 = (Button) findViewById(R.id.button2);
    btn2.setOnClickListener(this);
    btn3 = (Button) findViewById(R.id.button3);
    btn3.setOnClickListener(this);
    btn4 = (Button) findViewById(R.id.button4);
    btn4.setOnClickListener(this);


    Log.e("zd","MainActivity");
    CommonUtil._name = "abc";
    Log.e("zd","_name_host:" + CommonUtil._name);

    Toast.makeText(this, "第一次加载请先执行[LOAD PLUGIN]", Toast.LENGTH_SHORT).show();

  }


  private void loadPlugin(Context base,String apkName){
    PluginManager pluginManager = PluginManager.getInstance(base);
    File apk = new File(Environment.getExternalStorageDirectory(), apkName);
    if (apk.exists()) {
      try {
        pluginManager.loadPlugin(apk);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onClick(View v) {
    final String pkg = "org.zdnuist.plugin.a";
    final String pkg2 = "org.zdnuist.plugin.b";
    if(v.getId() != R.id.button4) {
      if (PluginManager.getInstance(this).getLoadedPlugin(pkg) == null) {
        Toast.makeText(this, "plugin [org.zdnuist.plugin.a] not loaded", Toast.LENGTH_SHORT).show();
        return;
      }

      if (PluginManager.getInstance(this).getLoadedPlugin(pkg2) == null) {
        Toast.makeText(this, "plugin [org.zdnuist.plugin.a] not loaded", Toast.LENGTH_SHORT).show();
        return;
      }
    }
    switch (v.getId()){
      case R.id.button:
        // test Activity and Service
        Intent intent = new Intent();
        intent.setClassName(pkg, "org.zdnuist.plugin.a.PluginAActivity");
        startActivity(intent);
        break;
      case R.id.button2:
        intent = new Intent();
        intent.setClassName(pkg,"org.zdnuist.plugin.a.PluginAService");
        startService(intent);
        break;
      case R.id.button3:
        intent = new Intent();
        intent.setClassName(pkg2, "org.zdnuist.plugin.b.PluginBActivity");
        startActivity(intent);
        break;
      case R.id.button4:
        if (hasPermission()) {
          loadPlugin();
        } else {
          requestPermission();
        }

        break;
    }
  }

  private void requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_STORAGE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        requestPermission();
      } else {
        loadPlugin();
      }
      return;
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private void loadPlugin() {
    this.loadPlugin(this,"plugina-release-unsigned.apk");
    this.loadPlugin(this,"pluginb-release-unsigned.apk");
  }

  private boolean hasPermission() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    return true;
  }

}
