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
       //Put your cookie
        loginCookies.put("","");

    }

    public Document getPage(String url) throws IOException {



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
