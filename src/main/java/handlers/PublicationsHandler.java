package handlers;

import dataSets.PublicationDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Home on 08.10.2018.
 */
public class PublicationsHandler {

    public List<PublicationDataSet> handle(List<List<String>> rawData){
        List<PublicationDataSet> publicationsList = new ArrayList<PublicationDataSet>();
        for (List<String > row: rawData) {
            PublicationDataSet publicationDataSet = new PublicationDataSet();
            Integer i = 0;
            for (String col:row) {
                if(null==col) col="";
                switch (++i) {
                    case 2:
                        publicationDataSet.type = col;
                        break;
                    case 3:
                        publicationDataSet.description = parseFromHtml(col);
                        break;
                    case 4:
                        publicationDataSet.year = parseFromHtml(col);
                        break;
                    case 5:
                        publicationDataSet.magazineType = parseFromHtml(col);
                        break;
                }
            }
            publicationsList.add(publicationDataSet);
        }
        return publicationsList;
    }

    private String parseFromHtml(String desc){
        if( null == desc || desc.length() == 0) return "";
        Document doc = Jsoup.parse(desc);
        return  (null == doc) ? "" : doc.text();
    }
}
