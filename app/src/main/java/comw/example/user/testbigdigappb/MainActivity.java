package comw.example.user.testbigdigappb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = 10;
        autoCloseApp();
    }

    private void autoCloseApp(){
        Thread thread = new Thread() {
            @Override
            public void run(){
                try{
                    while (true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textTimer = (TextView) findViewById(R.id.textTimer);
                                textTimer.setText(String.valueOf(time));
                            }
                        });
                        time--;

                        if(time == 0) {
                            finish();
                            System.exit(0);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
