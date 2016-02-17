package cl.xcero.xcerolifecounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView opPlusOne;
    private TextView opPlusFive;
    private TextView opMinusOne;
    private TextView opMinusFive;
    private TextView plPlusOne;
    private TextView plPlusFive;
    private TextView plMinusOne;
    private TextView plMinusFive;
    private TextView opLife;
    private TextView plLife;
    private SurfaceView opBack;
    private SurfaceView plBack;
    private Button resetTotals;
    private int dragSensibility = 50;
    private int lifeTextSize = 70;

    private int StartTotal = 20;
    //private int plStartTotal = 20;
    private int opLifeCount = StartTotal;
    private int plLifeCount = StartTotal;
    private float lastX = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // Shake sensor
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private float shakeThreshold = 13;
    private String shakeToast = "Vidas reiniciadas.";
    private boolean shakeStatus = true;

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > shakeThreshold && shakeStatus) {
                Toast toast = Toast.makeText(getApplicationContext(), shakeToast, Toast.LENGTH_LONG);
                toast.show();
                opLifeCount = StartTotal;
                plLifeCount = StartTotal;

                String opLifeString = Integer.toString(opLifeCount);
                opLife.setText(opLifeString);

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
                //MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.hayquejalar);
                //mp.start();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Shake Sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        // make surface transparent
        SurfaceView opSurfaceTrack = (SurfaceView)findViewById(R.id.opBack);
        opSurfaceTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder opSfhTrackHolder = opSurfaceTrack.getHolder();
        opSfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        SurfaceView plSurfaceTrack = (SurfaceView)findViewById(R.id.plBack);
        plSurfaceTrack.setZOrderOnTop(true);    // necessary
        SurfaceHolder plSfhTrackHolder = plSurfaceTrack.getHolder();
        plSfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        //finders
        opPlusOne = (TextView) findViewById(R.id.opPlusOne);
        opPlusFive = (TextView) findViewById(R.id.opPlusFive);
        opMinusOne = (TextView) findViewById(R.id.opMinusOne);
        opMinusFive = (TextView) findViewById(R.id.opMinusFive);

        plPlusOne = (TextView) findViewById(R.id.plPlusOne);
        plPlusFive = (TextView) findViewById(R.id.plPlusFive);
        plMinusOne = (TextView) findViewById(R.id.plMinusOne);
        plMinusFive = (TextView) findViewById(R.id.plMinusFive);

        opLife = (TextView) findViewById(R.id.opLife);
        plLife = (TextView) findViewById(R.id.plLife);

        opBack = (SurfaceView) findViewById(R.id.opBack);
        plBack = (SurfaceView) findViewById(R.id.plBack);

        resetTotals = (Button) findViewById(R.id.resetTotals);

        // reset button
        View.OnClickListener resetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opLifeCount = StartTotal;
                plLifeCount = StartTotal;

                String opLifeString = Integer.toString(opLifeCount);
                opLife.setText(opLifeString);

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
            }
        };
        resetTotals.setOnClickListener(resetListener);

        // Op Button counter Listeners
        View.OnClickListener opPlusOneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opLifeCount = opLifeCount + 1;

                    String opLifeString = Integer.toString(opLifeCount);
                    opLife.setText(opLifeString);
                }
        };
        View.OnClickListener opPlusFiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opLifeCount = opLifeCount + 5;

                String opLifeString = Integer.toString(opLifeCount);
                opLife.setText(opLifeString);
            }
        };
        View.OnClickListener opMinusOneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opLifeCount = opLifeCount - 1;

                String opLifeString = Integer.toString(opLifeCount);
                opLife.setText(opLifeString);
            }
        };
        View.OnClickListener opMinusFiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opLifeCount = opLifeCount - 5;

                String opLifeString = Integer.toString(opLifeCount);
                opLife.setText(opLifeString);
            }
        };
        opPlusOne.setOnClickListener(opPlusOneListener);
        opPlusFive.setOnClickListener(opPlusFiveListener);
        opMinusOne.setOnClickListener(opMinusOneListener);
        opMinusFive.setOnClickListener(opMinusFiveListener);

        // Player Button counter Listeners
        View.OnClickListener plPlusFiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plLifeCount = plLifeCount + 5;

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
            }
        };
        View.OnClickListener plPlusOneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plLifeCount = plLifeCount + 1;

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
            }
        };
        View.OnClickListener plMinusFiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plLifeCount = plLifeCount - 5;

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
            }
        };
        View.OnClickListener plMinusOneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plLifeCount = plLifeCount - 1;

                String plLifeString = Integer.toString(plLifeCount);
                plLife.setText(plLifeString);
            }
        };

        plPlusOne.setOnClickListener(plPlusOneListener);
        plPlusFive.setOnClickListener(plPlusFiveListener);
        plMinusOne.setOnClickListener(plMinusOneListener);
        plMinusFive.setOnClickListener(plMinusFiveListener);

        // Touch listener - pl Life change drag
        plBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        plLife.setTextSize(lifeTextSize + 15);
                        lastX = event.getX();

                        break;
                    case MotionEvent.ACTION_UP:
                        plLife.setTextSize(lifeTextSize);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float abs = lastX - event.getX();
                        /*float bbs = Math.bbs(abs - event.getX());*/

                        if (abs > dragSensibility) {
                            plLifeCount = plLifeCount - 1;
                            String plLifeDragChangeMinus = Integer.toString(plLifeCount);
                            plLife.setText(plLifeDragChangeMinus);
                            lastX = event.getX();
                            break;
                        }

                        if (abs < -dragSensibility) {
                            plLifeCount = plLifeCount + 1;
                            String plLifeDragChangePlus = Integer.toString(plLifeCount);
                            plLife.setText(plLifeDragChangePlus);
                            lastX = event.getX();
                            break;
                        }
                        break;
                }
                return true;
            }
        });

        // Touch listener - op Life change drag
        opBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        opLife.setTextSize(lifeTextSize + 15);
                        lastX = event.getX();

                        break;
                    case MotionEvent.ACTION_UP:
                        opLife.setTextSize(lifeTextSize);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float abs = lastX - event.getX();
                        /*float bbs = Math.bbs(abs - event.getX());*/

                        if (abs > dragSensibility){
                            opLifeCount = opLifeCount - 1;
                            String opLifeDragChangeMinus = Integer.toString(opLifeCount);
                            opLife.setText(opLifeDragChangeMinus);
                            lastX = event.getX();
                            break;
                        }

                        if (abs < -dragSensibility){
                            opLifeCount = opLifeCount + 1;
                            String opLifeDragChangePlus = Integer.toString(opLifeCount);
                            opLife.setText(opLifeDragChangePlus);
                            lastX = event.getX();
                            break;
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.reset_totals) {
            //menu reset life totals
            opLifeCount = StartTotal;
            plLifeCount = StartTotal;

            String opLifeString = Integer.toString(opLifeCount);
            opLife.setText(opLifeString);

            String plLifeString = Integer.toString(plLifeCount);
            plLife.setText(plLifeString);
        } else if (id == R.id.starting_life) {
            // menu set starting life
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String setTotalsText = "Vidas iniciales:";
            builder.setTitle(setTotalsText);

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StartTotal = Integer.parseInt(input.getText().toString());
                    opLifeCount = StartTotal;
                    plLifeCount = StartTotal;

                    String opLifeString = Integer.toString(opLifeCount);
                    opLife.setText(opLifeString);

                    String plLifeString = Integer.toString(plLifeCount);
                    plLife.setText(plLifeString);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (id == R.id.life_size) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String setLifeSize = "Tamaño vidas (actual: " + lifeTextSize + "):";
            builder.setTitle(setLifeSize);

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    lifeTextSize = Integer.parseInt(input.getText().toString());
                    opLife.setTextSize(lifeTextSize);
                    plLife.setTextSize(lifeTextSize);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (id == R.id.shakeReset) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String setLifeSize = "¿Reiniciar vidas al agitar dispositivo?";
            builder.setTitle(setLifeSize);

            /*// Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);*/

            // Set up the buttons
            builder.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shakeStatus = true;
                }
            });
            builder.setNegativeButton("Desactivar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shakeStatus = false;
                }
            });
            builder.show();
        }


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cl.xcero.xcerolifecounter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

       /* // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cl.xcero.xcerolifecounter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }


}
