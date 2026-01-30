package test;

import java.util.Date;
import java.util.Calendar;
import java.nio.charset.StandardCharsets;

public class Message {
    Calendar calendar = Calendar.getInstance();
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date = calendar.getTime();
    
    //3 CTORs for each input. converts the input to match the other variables.
    public Message(String str) {
        this.asText = str;

        this.data = this.asText.getBytes();
        //Try to convert to double
        double tempDouble;
        try {
            tempDouble = Double.parseDouble(asText);
        } catch (NumberFormatException e){
            tempDouble = Double.NaN;
            System.err.println("The given string cannot be converted to Double.");
        }
        this.asDouble = tempDouble;
    }

    public Message(byte[] data) {
        this(new String(data,StandardCharsets.UTF_8));
    }
    public Message(double doub) {
        this(String.valueOf(doub));
    }



}
