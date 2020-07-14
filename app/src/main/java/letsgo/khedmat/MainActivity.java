package letsgo.khedmat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;

import letsgo.khedmat.fragments.DialogSetEndTime;
import letsgo.khedmat.helper.TinyDB;

import static letsgo.khedmat.helper.Helper.KEY_END_TIME;
import static letsgo.khedmat.helper.Helper.KEY_START_TIME;

public class MainActivity extends AppCompatActivity {
    int s, m, h;
    TextView txtD, txtH, txtM, txtS, txtDate, txtEndTime;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtD = findViewById(R.id.txtD);
        txtH = findViewById(R.id.txtH);
        txtM = findViewById(R.id.txtM);
        txtS = findViewById(R.id.txtS);
        txtDate = findViewById(R.id.txtDate);
        txtEndTime = findViewById(R.id.txtEndTime);


        initView();

        startCountTimer();

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                persianCalendar.setPersianDate(year, monthOfYear, dayOfMonth);
                                persianCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                persianCalendar.set(Calendar.MINUTE, 0);
                                persianCalendar.set(Calendar.SECOND, 0);
                                Log.i(TAG, "onDateSet: " + persianCalendar.getPersianShortDateTime());
                                TinyDB tinyDB = new TinyDB(getApplicationContext());
                                tinyDB.putLong(KEY_START_TIME, persianCalendar.getTimeInMillis());
                                initView();
                            }
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSetEndTime dialogSetEndTime = new DialogSetEndTime();
                dialogSetEndTime.setListener(new DialogSetEndTime.Listener() {
                    @Override
                    public void onSelectMonth(int month) {
                        TinyDB tinyDB = new TinyDB(getApplicationContext());
                        tinyDB.putLong(KEY_END_TIME, month);
                        initView();
                    }
                });
                dialogSetEndTime.show(getSupportFragmentManager(), "setEndTime");
            }
        });
    }

    private void initView() {
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        PersianCalendar now = new PersianCalendar(System.currentTimeMillis());
        PersianCalendar startDate = new PersianCalendar();
        startDate.setTimeInMillis(tinyDB.getLong(KEY_START_TIME, System.currentTimeMillis()));

        h = 24 - now.get(Calendar.HOUR_OF_DAY);
        m = 60 - now.get(PersianCalendar.MINUTE);
        s = 60 - now.get(PersianCalendar.SECOND);

        final long day = ((startDate.getTimeInMillis() - now.getTimeInMillis()) / 86400000);
        txtDate.setText(startDate.getPersianLongDate());

        txtD.setText(String.valueOf(day * -1));
        txtH.setText(String.valueOf(h));
        txtM.setText(String.valueOf(m));
        txtS.setText(String.valueOf(s));

        long totalMonth = tinyDB.getLong(KEY_END_TIME, 21);
        PersianCalendar endDate = (PersianCalendar) startDate.clone();
        endDate.add(Calendar.MONTH , (int) totalMonth);
        endDate.add(Calendar.DAY_OF_MONTH , 1);
//        endDate.add(Calendar.DAY_OF_YEAR , 1);
//        endDate.add(Calendar.DAY_OF_YEAR ,-30);

        txtEndTime.setText(endDate.getPersianLongDate());

    }

    private void startCountTimer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    s++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtS.setText(String.valueOf(s));
                        }
                    });
                    if (s == 60) {
                        s = 1;
                        m++;
                        if (m == 60) {
                            m = 1;
                            h++;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtH.setText(String.valueOf(h));
                                txtM.setText(String.valueOf(m));
                                txtS.setText(String.valueOf(s));
                            }
                        });
                    }
                }
            }
        });
        thread.start();
    }
}
