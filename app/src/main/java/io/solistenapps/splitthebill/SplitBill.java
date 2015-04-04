package io.solistenapps.splitthebill;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;


public class SplitBill extends ActionBarActivity {

    private static final String SAVED_INITIAL_BILL = "INITIAL_BILL";
    private static final String SAVED_TIP = "SAVED_TIP";
    private static final String SAVED_CALC_TIP = "SAVED_TIP";
    private static final String SAVED_FINAL_BILL = "SAVED_FINAL_BILL";
    private static final String SAVED_PARTY_SIZE = "SAVED_PARTY_SIZE";
    private static final String SAVED_SPLIT_BILL = "SAVED_SPLIT_BILL";


    private double bill;
    private double tip;
    private double calcTip;
    private double finalBill;
    private double splitBill;

    private int partySize;

    EditText etBillAmount;

    TextView tvTipAmount;
    TextView tvCalcTip;
    TextView tvFinalBill;
    TextView tvSplitBill;
    TextView tvPartySize;

    ImageButton btnPlusTip;
    ImageButton btnMinTip;
    ImageButton btnPlusParty;
    ImageButton btnMinParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        //Check if user is coming back to app or starting new
        if(savedInstanceState == null){

            //Sets value to default
            bill = 0.0;
            tip = 15;
            calcTip = 0.0;
            finalBill = 0.0;
            partySize = 1;
            splitBill = 0.0;

        } else{

            //Populates with previously saved values
            bill = savedInstanceState.getDouble(SAVED_INITIAL_BILL);
            tip = savedInstanceState.getDouble(SAVED_TIP);
            calcTip = savedInstanceState.getDouble(SAVED_CALC_TIP);
            finalBill = savedInstanceState.getDouble(SAVED_FINAL_BILL);

        }

        etBillAmount = (EditText) findViewById(R.id.etBill);
        etBillAmount.addTextChangedListener(initialBillListener);

        tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
        tvCalcTip = (TextView) findViewById(R.id.tvCalcTipAmount);
        tvFinalBill = (TextView) findViewById(R.id.tvFinalBillAmount);
        tvSplitBill = (TextView) findViewById(R.id.tvSplitAmount);
        tvPartySize = (TextView) findViewById(R.id.tvPartySizeAmount);

        btnPlusTip = (ImageButton) findViewById(R.id.btnPlusTip);
        btnMinTip = (ImageButton) findViewById(R.id.btnMinTip);

        btnPlusParty = (ImageButton) findViewById(R.id.btnPlusParty);
        btnMinParty = (ImageButton) findViewById(R.id.btnMinParty);

        btnPlusTip.setOnClickListener(addTipListener);
        btnMinTip.setOnClickListener(addTipListener);
        btnPlusParty.setOnClickListener(addTipListener);
        btnMinParty.setOnClickListener(addTipListener);

    }

    //Listens for updated in initial bill value
    private TextWatcher initialBillListener = new TextWatcher() {

        String current = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals(current)){

                    etBillAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    try{
                        bill = Double.parseDouble(cleanString);
                    } catch(NumberFormatException e){
                        bill = 0.0;
                    }

                    String formatted = NumberFormat.getCurrencyInstance().format((bill/100));

                    current = formatted;
                    etBillAmount.setText(formatted);
                    etBillAmount.setSelection(formatted.length());

                    etBillAmount.addTextChangedListener(this);

                    updateFinalSplitBill();

                }

        }



        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //Add 1 to tip when button is clicked
    private View.OnClickListener addTipListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.btnPlusTip:
                    if(tip < 50){
                        tip++;
                        tvTipAmount.setText(String.format("%.0f", tip));
                    } else {
                        tip = 50;
                    }
                    break;
                case R.id.btnMinTip:
                    if(tip > 0){
                        tip--;
                        tvTipAmount.setText(String.format("%.0f", tip));

                    } else {
                        tip = 0;
                    }
                    break;
                case R.id.btnPlusParty:
                    partySize++;
                    tvPartySize.setText(String.format("%s", partySize));
                    break;
                case R.id.btnMinParty:
                    if(partySize > 1){
                        partySize--;
                        tvPartySize.setText(String.format("%s", partySize));

                    } else {
                        partySize = 1;
                    }
                    break;
            }

            updateFinalSplitBill();

        }
    };

    private void updateFinalSplitBill(){

        tip = Double.parseDouble(tvTipAmount.getText().toString());

        //Calculates tip amount
        calcTip = (bill*tip)/10000;
        tvCalcTip.setText(String.format("$%.02f", calcTip));

        //Calculates final bill
        finalBill = (bill/100) + calcTip;
        tvFinalBill.setText(String.format("$%.02f", finalBill));

        if(partySize > 1){
            splitBill = finalBill / partySize;
            tvSplitBill.setText(String.format("$%.02f", splitBill));
        } else {
            splitBill = 0.0;
            tvSplitBill.setText(String.format("$%.02f", splitBill));
        }


    }

    //When going out of application, saves values
    protected void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);

        outState.putDouble(SAVED_INITIAL_BILL, bill);
        outState.putDouble(SAVED_TIP, tip);
        outState.putDouble(SAVED_CALC_TIP, calcTip);
        outState.putDouble(SAVED_FINAL_BILL, finalBill);
        outState.putInt(SAVED_PARTY_SIZE, partySize);
        outState.putDouble(SAVED_SPLIT_BILL, splitBill);

    }

}
