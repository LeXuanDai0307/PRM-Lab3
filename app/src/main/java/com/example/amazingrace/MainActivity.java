package com.example.amazingrace;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView txtMoney;
    TextView txtBetMoney;
    EditText etShip1, etShip2, etShip3;
    ImageButton btnStart, btnReset;
    CheckBox cb1, cb2, cb3;
    SeekBar sb1, sb2, sb3;
    CountDownTimer countDownTimer;
    ArrayList<Integer> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = new ArrayList<Integer>();
        setup();
        resetGame();

        CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(50000, 200) {
            @Override
            public void onTick(long l) {
                btnStart.setEnabled(false);
                btnReset.setEnabled(false);
                Random rand = new Random();

                // random and add value to processes
                if (sb1.getProgress() >= 100) {
                    if(!result.contains(1)) {
                        result.add(1);
                    }
                } else {
                    sb1.setProgress((int) (sb1.getProgress() + rand.nextInt(5)));
                }
                if (sb2.getProgress() >= 100) {
                    if(!result.contains(2)) {
                        result.add(2);
                    }
                } else {
                    sb2.setProgress((int) (sb2.getProgress() + rand.nextInt(5)));
                }
                if (sb3.getProgress() >= 100) {
                    if(!result.contains(3)) {
                        result.add(3);
                    }
                } else {
                    sb3.setProgress((int) (sb3.getProgress() + rand.nextInt(5)));
                }

                // if all ships finished the race
                if (sb1.getProgress() >= 100 && sb2.getProgress() >= 100 && sb3.getProgress() >= 100) {
                    this.cancel();
                    Toast.makeText(MainActivity.this, "Hoàn thành, thuyền về nhất là thuyền số " + result.get(0), Toast.LENGTH_SHORT).show();
                    this.onFinish();

                }
            }

            @Override
            public void onFinish() {
                // update result
                if (!result.isEmpty() && txtMoney != null) {
                    try {
                        int winningMoney = 0;
                        int betShip1 = 0;
                        int betShip2 = 0;
                        int betShip3 = 0;

                        if (cb1.isChecked()) {
                            betShip1 = getIntFromEditText(etShip1);
                        }
                        if (cb2.isChecked()) {
                            betShip2 = getIntFromEditText(etShip2);
                        }
                        if (cb3.isChecked()) {
                            betShip3 = getIntFromEditText(etShip3);
                        }

                        String moneyString = txtMoney.getText().toString().trim();
                        int money = Integer.parseInt(moneyString);

                        if (result.get(0) == 1) {
                            winningMoney += betShip1 * 3;
                        } else if (result.get(0) == 2) {
                            winningMoney += betShip2 * 3;
                        } else if (result.get(0) == 3) {
                            winningMoney += betShip3 * 3;
                        }

                        money = money - betShip1 - betShip2 - betShip3 + winningMoney;
                        txtMoney.setText(String.valueOf(money));
                        resetMatch();
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateBeforeRun()) {
                    countDownTimer.start();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private Integer getIntFromEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        return Integer.parseInt(text);
    }

    private boolean validateBeforeRun() {
        if (!cb1.isChecked() & !cb2.isChecked() & !cb3.isChecked()) {
            Toast.makeText(this, "Vui lòng đặt cược trước khi chơi!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int betShip1 = 0;
        int betShip2 = 0;
        int betShip3 = 0;
        String moneyString = txtMoney.getText().toString().trim();
        int money = Integer.parseInt(moneyString);

        if (cb1.isChecked()) {
            betShip1 = getIntFromEditText(etShip1);
            if (betShip1 <= 0 || betShip1 > money) {
                Toast.makeText(this, "Số tiền đặt cược tàu 1 không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (cb2.isChecked()) {
            betShip2 = getIntFromEditText(etShip2);
            if (betShip2 <= 0 || betShip2 > money) {
                Toast.makeText(this, "Số tiền đặt cược tàu 2 không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (cb3.isChecked()) {
            betShip3 = getIntFromEditText(etShip3);
            if (betShip3 <= 0 || betShip3 > money) {
                Toast.makeText(this, "Số tiền đặt cược tàu 3  không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        int totalBet = betShip1 + betShip2 + betShip3;
        if (money < totalBet) {
            Toast.makeText(this, "Bạn không đủ tiền để đặt cược !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetMatch() {
        sb1.setProgress(0);
        sb2.setProgress(0);
        sb3.setProgress(0);
        btnStart.setEnabled(true);
        btnReset.setEnabled(true);
        result.clear();
    }

    private void resetGame() {
        resetMatch();
        txtMoney.setText(String.valueOf(100));
        etShip1.setText("0");
        etShip2.setText("0");
        etShip3.setText("0");
        cb1.setChecked(false);
        cb2.setChecked(false);
        cb3.setChecked(false);
    }

    private void setup() {
        txtMoney = (TextView) findViewById(R.id.txtMoney);
        txtBetMoney = (TextView) findViewById(R.id.txtBetMoney);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnReset = (ImageButton) findViewById(R.id.btnReset);
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        sb1 = (SeekBar) findViewById(R.id.seekBarShip1);
        sb2 = (SeekBar) findViewById(R.id.seekBarShip2);
        sb3 = (SeekBar) findViewById(R.id.seekBarShip3);
        txtMoney = (TextView) findViewById(R.id.txtMoney);
        etShip1 = (EditText) findViewById(R.id.etShip1);
        etShip2 = (EditText) findViewById(R.id.etShip2);
        etShip3 = (EditText) findViewById(R.id.etShip3);
        sb1.setEnabled(false);
        sb2.setEnabled(false);
        sb3.setEnabled(false);
    }
}