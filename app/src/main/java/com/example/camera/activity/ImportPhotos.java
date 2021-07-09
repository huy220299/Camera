//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.example.camera.R;
//
//import java.io.File;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_PERMISSION_CODE = 1;
//    private DrawerLayout drawerLayout;
//    private CardView cardPhotoVault,cardVideoVault;
//    private long backPressTime;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        checkPermissions();
//        addControls();
//        addEvents();
//    }
//
//    private void addEvents() {
//        cardPhotoVault.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createPhotoDefaultFolder();
//                Intent intent=new Intent(MainActivity.this,PhotoVaultActivity.class);
//                startActivity(intent);
//            }
//        });
//        cardVideoVault.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createVideoDefaultFolder();
//                Intent intent=new Intent(MainActivity.this,VideoVaultActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void addControls() {
//        drawerLayout=findViewById(R.id.drawerLayout);
//        cardVideoVault=findViewById(R.id.cardVideoVault);
//        cardPhotoVault=findViewById(R.id.cardPhotoVault);
//    }
//
//    public void clickLogo(View view){
//        closeDrawer(drawerLayout);
//    }
//    public void clickSetting(View view){
//        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//        startActivity(intent);
//    }
//    public void clickBreakInAlert(View view){
//    }
//    public void clickShareToFriend(View view){
//    }
//    public void clickLikeApp(View view){
//    }
//    public void clickAboutUs(View view){
//    }
//    public void clickMenu(View view){
//        openDrawer(drawerLayout);
//    }
//    private static void openDrawer(DrawerLayout drawerLayout) {
//        drawerLayout.openDrawer(GravityCompat.START);
//    }
//    private void checkPermissions(){
//        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
//            return;
//        }
//        if (checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED &&
//                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
//                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
//            createDirectoty();
//        }else {
//            String[] permissions= {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
//            requestPermissions(permissions,REQUEST_PERMISSION_CODE);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
//                    grantResults[1] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();
//                createDirectoty();
//            }else{
//                Toast.makeText(MainActivity.this,"Permission denied",Toast.LENGTH_SHORT).show();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAndRemoveTask();
//                }
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onBackPressed() {
//        if (backPressTime + 2000 > System.currentTimeMillis()){
//            finishAffinity();
//        }else {
//            Toast.makeText(MainActivity.this, "Press back againt to exit the application!",Toast.LENGTH_SHORT).show();
//        }
//        backPressTime = System.currentTimeMillis();
//    }
//
//}