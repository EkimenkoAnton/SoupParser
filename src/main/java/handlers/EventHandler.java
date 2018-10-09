package handlers;

import dataSets.EventDataSet;
import dataSets.PublicationDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 09.10.2018.
 */
public class EventHandler {
    public List<EventDataSet> handle(List<List<String>> rawData){
        List<EventDataSet> eventList = new ArrayList<EventDataSet>();
        for (List<String > row: rawData) {
            EventDataSet eventDataSet = new EventDataSet();
            Integer i = 0;
            for (String col:row) {
                if(null==col) col="";
                switch (++i) {
                    case 1:
                        eventDataSet.name = col;
                        break;
                    case 2:
                        eventDataSet.dates = parseFromHtml(col);
                        break;
                    case 3:
                        eventDataSet.year = col;
                        break;
                    case 4:
                        eventDataSet.type =col;
                        break;
                    case 5:
                        eventDataSet.rank = col;
                        break;
                    case 6:
                        eventDataSet.role = col;
                        break;
                }
            }
            eventList.add(eventDataSet);
        }
        return eventList;
    }

    private String parseFromHtml(String desc){
        if( null == desc || desc.length() == 0) return "";
        Document doc = Jsoup.parse(desc);
        return  (null == doc) ? "" : doc.text();
    }
}
