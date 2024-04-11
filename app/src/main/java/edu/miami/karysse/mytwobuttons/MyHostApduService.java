package edu.miami.karysse.mytwobuttons;

import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import android.nfc.cardemulation.HostApduService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyHostApduService extends HostApduService {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private static final String TAG = "HCEservice";
    FirebaseUser user = auth.getCurrentUser();
    String userData = user.getEmail();

    private static final byte[] SELECT_APDU = {
            (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00
    };
    private static final byte[] SELECT_AID_APDU = {
            (byte) 0xF0, (byte) 0x01, (byte) 0x02, (byte) 0x03,
            (byte) 0x04, (byte) 0x05, (byte) 0x06
    };

    private static final byte[] SUCCESS_RESPONSE = {
            (byte)0x88, (byte)0x20
    };

    private static final byte[] FAILED_RESPONSE = {
            (byte)0x63, (byte)0x00
    };

    @Override
    public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
        Log.i(TAG, "Received APDU: " + byteArrayToHex(apdu));



        if (matchesSelectApdu(apdu)) {
            // Extract the AID length from the fifth byte of the APDU command
            int aidLength = apdu[4];
            // Extract the AID from the APDU command starting from the sixth byte
            byte[] extractedAid = Arrays.copyOfRange(apdu, 5, 5 + aidLength);

            // Compare the extracted AID with the expected AID
            if (Arrays.equals(SELECT_AID_APDU, extractedAid)) {
                byte[] responseData = userData.getBytes(StandardCharsets.UTF_8);
                // Combine the response data with the SUCCESS_RESPONSE status words
                return ByteBuffer.allocate(responseData.length + SUCCESS_RESPONSE.length)
                        .put(responseData)
                        .put(SUCCESS_RESPONSE)
                        .array();
                //return SUCCESS_RESPONSE;
            } else {
                Log.i(TAG, "Received AID does not match the expected AID.");
                return FAILED_RESPONSE;
            }
        } else {
            Log.i(TAG, "Received unknown APDU command: " + byteArrayToHex(apdu));
            return FAILED_RESPONSE;
        }


    }

    private boolean matchesSelectApdu(byte[] apdu) {
        if (apdu.length < SELECT_APDU.length) {
            // The APDU is too short to contain the expected header
            return false;
        }

        // Extract the header from the APDU
        byte[] apduHeader = Arrays.copyOfRange(apdu, 0, SELECT_APDU.length);

        // Compare the extracted header with the SELECT command header
        return Arrays.equals(SELECT_APDU, apduHeader);
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }


    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "Deactivated: " + reason);
    }
}
