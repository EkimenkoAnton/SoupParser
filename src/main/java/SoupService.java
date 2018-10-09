import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 07.10.2018.
 */
public class SoupService {

    Map<String, String> loginCookies = new HashMap<String, String>();

    public SoupService() {

    }

    public Document getPage(String url) throws IOException {
        /*String cookie="ORA_WWV_RAC_INSTANCE=2; " +
                "REMEMBER_ME=FFF62CE37FE6F87B0F3D805C508B5C1C:FB860DF2489B25BA6EF086802FFCC214; " +
                "CUSTOM_COOKIE=03.10.2018 22:21:55; " +
                "ISU_AP_COOKIE=ORA_WWV-cPYCKmRNaC8UzcveC5TT0AYV; ISU_LIB_SID=ORA_WWV-cPYCKmRNaC8UzcveC5TT0AYV";

*/

/*
        Connection.Response res = Jsoup
                .connect("loginPageUrl")
                .data("loginField", "login@login.com", "passField", "pass1234")
                .method(Connection.Method.POST)
                .execute();
*/
//This will get you cookies
        //Map<String, String> loginCookies = res.cookies();


//And this is the easiest way I've found to remain in session
        Document doc = Jsoup.connect(url)
                .cookies(loginCookies)
                .get();
        return doc;
        //String title = doc.body().html();
        //System.out.println(title);
    }


}
