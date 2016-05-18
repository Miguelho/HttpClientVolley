package miguel.apicall.Utils;

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

}
