package id.ac.uny.unyofficial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button indexPengumuman,indexBerita,home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        indexPengumuman = (Button) findViewById(R.id.btindexpeng);
        indexBerita = (Button) findViewById(R.id.btindexberita);
        home = (Button)findViewById(R.id.bthome);

        indexPengumuman.setOnClickListener(this);
        indexBerita.setOnClickListener(this);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent i;
        switch (view.getId()){
            case R.id.bthome:
                Intent iH = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(iH);
                break;

            case R.id.btindexberita:
                Intent iB = new Intent(MainActivity.this,IndexBerita.class);
                startActivity(iB);
                break;
            case R.id.btindexpeng:
                Intent iP = new Intent(MainActivity.this,IndexPengumuman.class);
                startActivity(iP);
                break;


        }

    }
}
