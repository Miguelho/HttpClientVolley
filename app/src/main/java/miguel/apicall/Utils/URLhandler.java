package miguel.apicall.Utils;

/**
 * Created by Usuario on 17/05/2016.
 */
public class URLhandler {


    public static String generateURL(String url,String[]params,String[]values) {

        for (int i = 0; i < params.length; i++) {
            url += "?" + params[i] + "=%" + String.valueOf(i+1) + "$s";
            if (i != params.length - 1)
                url += "&";
        }
        for(String x : values)
            url=String.format(url,x);
        return url;

    }

}
