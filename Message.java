package test;

import java.util.Date;
import java.util.Arrays;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;
    
    //3 CTORs for each input. converts the input to match the other variables.
    public Message(String str) {
        this.asText = str;

        this.data = this.asText.getBytes();
        //Try to convert to double
        try {
            this.asDouble = Double.parseDouble(asText);
        } catch (NumberFormatException e){
            this.asDouble = Double.NaN;
            System.err.println("The given string cannot be converted to Double.");
        }

        Calendar calendar = Calendar.getInstance();
        this.date = calendar.getTime();
    }

    public Message(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
        String str = new String(data, StandardCharsets.UTF_8);
        Message(str);
    }
    public Message(double doub) {
        String str = String.valueOf(doub);
        Message(str);
    }



}
