package miguel.apicall.Utils;

import org.json.JSONArray;
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

    public static String getStringFromJSONObjectBackend(String jsonString,String fieldName){
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

    public static JSONArray getJSONARRAYfromJSONObjectBackend(String jsonString, String fieldName){
        JSONArray output=null;
        JSONObject jsonObject=null;
        try {
            jsonObject= new JSONObject(jsonString);
            output=jsonObject.getJSONArray(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
            output=null;
        }
        return output;

    }



    }
