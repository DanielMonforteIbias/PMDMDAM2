package es.riberadeltajo.notificaciones;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiverSMS extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String origen = null;
        String msg = null;

        if (bundle != null) {
            //obtenemos el mensaje original SMS:
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                String format = bundle.getString("format");
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                origen = msgs[i].getOriginatingAddress();
                msg = msgs[i].getMessageBody().toString();
            }

            Toast.makeText(context, "SMS Recibido de " + origen + ":" + msg, Toast.LENGTH_LONG).show();

            //continua el proceso normal de broadcast
            // es decir, llega el sms y se almacena en la bandeja de entrada
            this.clearAbortBroadcast();

        }
    }
}