package com.example.hossainkabir.calculatorweb;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Hossain Kabir on 5/1/2017.
 */
public class WebViewInterface {
    Context context;

    boolean firstInOperator=true;
    boolean firstDot=false;
    boolean equalButtonActive=false;

    String lastOperator,firstDigit,lastDigit;
    String finlaRetResult;

    double tempFirstValue;
    double tempSecondValue;
    double roundOffResult;
    String autoEqualfinal;

    public WebViewInterface(Context context){
        this.context=context;
    }

    @JavascriptInterface
    public String addNum(String num,String displayValue){

        String retNum=num;
        firstInOperator=true;
        equalButtonActive=false;

        if(displayValue.trim().equals("") && (num.equals("."))){
            retNum="";
        }
       else if(!(firstDot) && num.equals(".")){
            retNum=num;
            firstDot=true;
        }
        else if((firstDot) && (num.equals("."))) {
            retNum = "";
        }
        else if(!num.equals(".")){
            retNum=num;
        }

        return retNum;
    }

    @JavascriptInterface
    public String addOperator(String opr,String displayValue){
        String retOperator;
        firstDot=false;
        if(firstInOperator) {

            if (displayValue.trim().equals("")) {
                retOperator= "";
            } else {
                lastOperator=opr;
                Toast.makeText(context, "Last Operator :" + lastOperator + "first Time Opr :" + opr, Toast.LENGTH_LONG).show();
                Toast.makeText(context, "D Value :" + displayValue + " Opr :" + opr, Toast.LENGTH_LONG).show();
                displayValue+=opr;
            }
            firstInOperator=false;
        }else {
            lastOperator=opr;
            Toast.makeText(context, "Last Operator :" + lastOperator + "Second Time Opr :" + opr, Toast.LENGTH_LONG).show();
            displayValue = displayValue.substring(0,displayValue.length()-1) + opr;
        }
        return displayValue;
    }

    @JavascriptInterface
    public String getResult(String expr){
        firstDot=true;

        if(equalButtonActive){
            String autoEqual=this.AutoEqual();
            Toast.makeText(context,autoEqual,Toast.LENGTH_SHORT).show();
            return autoEqual;

        }else {
            equalButtonActive=true;
            String[] splitted = expr.split("[-+*/]");
            for (String split : splitted) {
                firstDigit = splitted[0];
                lastDigit = splitted[1];
                Toast.makeText(context, " 1st =" + firstDigit + " 2nd =" + lastDigit + "Opr =" + lastOperator, Toast.LENGTH_LONG).show();
            }


            tempFirstValue = Double.parseDouble(firstDigit);
            tempSecondValue = Double.parseDouble(lastDigit);
            double tempFinlaResult;

            if (lastOperator.equals("+")) {
                tempFinlaResult = tempFirstValue + tempSecondValue;
                double roundOffResult = Math.round(tempFinlaResult * 100) / 100D;
                finlaRetResult = "" + roundOffResult;
                return finlaRetResult;
            } else if (lastOperator.equals("-")) {
                tempFinlaResult = tempFirstValue - tempSecondValue;
                finlaRetResult = "" + tempFinlaResult;
                double roundOffResult = Math.round(tempFinlaResult * 100) / 100D;
                finlaRetResult = "" + roundOffResult;
                return finlaRetResult;
            } else if (lastOperator.equals("*")) {
                tempFinlaResult = tempFirstValue * tempSecondValue;
                finlaRetResult = "" + tempFinlaResult;
                double roundOffResult = Math.round(tempFinlaResult * 100) / 100D;
                finlaRetResult = "" + roundOffResult;
                return finlaRetResult;
            } else if (lastOperator.equals("/")) {

                if (firstDigit.equals("0") && lastDigit.equals("0")) {
                    finlaRetResult = "" + 0;
                } else if (firstDigit.equals("0") && !lastDigit.equals("0")) {
                    finlaRetResult = "" + 0;
                } else if (lastDigit.equals("0") && !firstDigit.equals("0")) {
                    finlaRetResult = "Cannot Divide by Zero ";
                } else {

                    tempFinlaResult = tempFirstValue / tempSecondValue;
                    finlaRetResult = "" + tempFinlaResult;
                    roundOffResult = Math.round(tempFinlaResult * 100) / 100D;
                    finlaRetResult = "" + roundOffResult;
                }
            }
        }
        autoEqualfinal=finlaRetResult;
        return finlaRetResult;


    }

    public String AutoEqual(){

        double autoEqualDouble = Double.parseDouble(autoEqualfinal);

        if (tempSecondValue==0.0){
            finlaRetResult = "" + autoEqualDouble;
        }else {

            if(lastOperator.equals("+")){
                autoEqualDouble=autoEqualDouble+tempSecondValue;
                finlaRetResult = "" + autoEqualDouble;
            }
            else if(lastOperator.equals("-")){
                autoEqualDouble=autoEqualDouble-tempSecondValue;
                finlaRetResult = "" + autoEqualDouble;
            }
            else if(lastOperator.equals("*")){
                autoEqualDouble=autoEqualDouble-tempSecondValue;
                finlaRetResult = "" + autoEqualDouble;
            }
            else if(lastOperator.equals("/")){
                autoEqualDouble=autoEqualDouble-tempSecondValue;
                finlaRetResult = "" + autoEqualDouble;
            }
        }
        return finlaRetResult;
    }
}
