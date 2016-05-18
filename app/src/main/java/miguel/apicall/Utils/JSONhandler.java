package miguel.apicall.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Usuario on 18/05/2016.
 */
public class JSONhandler {
    public static String getReadableDateFromLong(long dateinmilisseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
        return sdf.format(new Date(dateinmilisseconds));
    }

    public static String getHoursMinutesSecondsFromDateDifference(long dateinmilisseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(dateinmilisseconds));
    }

//{"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0NjM1NjU4NDEsImV4cCI6MTQ2NDc3NTQ0MX0.V5vL97iyIuMnLYqw9nJCmVz3niko-CbVdu2c6HPsdus","userId":"573432c90901d3b81b0e7969"}
    public static String getJSONObjectFromBackend(String jsonString,String fieldName){
        String output=null;
        JSONObject jsonObject=null;
        try {
            jsonObject= new JSONObject(jsonString);
            output=jsonObject.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
            output="Error: nothing";
        }

        return output;
    }
}
