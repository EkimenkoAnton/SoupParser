import dataSets.EventDataSet;
import dataSets.PublicationDataSet;
import handlers.EventHandler;
import handlers.PublicationsHandler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoupParser {

    Pattern patternJs = Pattern.compile("(\\{\"recordsTotal.*\\]\\]\\});");

    public static void main(String[] args){

        try {
            SoupParser soupParser = new SoupParser();
            List<String> groups= new ArrayList<String>();
            groups.add("R3235");
            groups.add("R3236");
            groups.add("R3237");
            groups.add("R3240");
            groups.add("R3241");
            groups.add("R3335");
            groups.add("R3336");
            groups.add("R3340");
            groups.add("R3338");
            groups.add("R3435");
            groups.add("R3442");
            groups.add("R3440");
            groups.add("R3441");
            groups.add("R3438");

            for (String group:groups) {
                System.out.println("=======================");
                System.out.println("ГРУППА\t"+group);
                System.out.println("=======================");
                new SoupParser().start(group);
            }
            //soupParser.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void start(String groupId) throws IOException {

        //String groupId="R4241";

        String groupTagId="report_R1725000247147253423";
        String publicationTagId="R1724073431179133097";
        String eventTagId="R1293424228395371640";
        if(null!=groupId) groupId=groupId.toLowerCase();
        String url="https://isu.ifmo.ru/pls/apex/f?p=2143:GR:100050754085423::NO::GR_GR,GR_DATE:"+groupId;
        SoupService soupService = new SoupService();
        Document groupDoc = soupService.getPage(url);
        Map<String,String> map = getUsers(groupDoc,groupTagId);
        //for (String id:map.keySet()) System.out.println(id + map.get(id));
        if(null == map || map.isEmpty()) return;
        for (String id:map.keySet()) {
            //String id="181945";
            String isuLink="https://isu.ifmo.ru/pls/apex/f?p=2143:3:100050754085423::NO:RP:PID:" + id;
            System.out.println(id+"\t"+map.get(id));
            System.out.println("ИСУ:\t"+isuLink);

            Document userDoc = soupService.getPage((isuLink));
            try {
                Set<String> mails = getMails(userDoc);
                if(null != mails && !mails.isEmpty()){
                    StringBuilder sbMails = new StringBuilder();
                    for (String mail:mails) {
                        if(sbMails.length()>0) sbMails.append(",");
                        sbMails.append(mail);
                    }
                    System.out.println("mails:\t"+sbMails.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                List<PublicationDataSet> userPuplications = getPublications(userDoc,publicationTagId);
                if (null != userPuplications) {
                    System.out.println("Публикации:");
                    System.out.println(new PublicationDataSet().header());
                    int i = 0;
                    for (PublicationDataSet userPuplication : userPuplications) {
                        System.out.println(++i + "" + userPuplication);
                    }
                } else  System.out.println("Публицаций еще нет");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                List<EventDataSet> userEvents = getEvents(userDoc,eventTagId);
                if (null != userEvents) {
                    System.out.println("Мероприятия:");
                    System.out.println(new EventDataSet().header());
                    int i = 0;
                    for (EventDataSet userEvent : userEvents) {
                        System.out.println(++i + "" + userEvent);
                    }
                } else System.out.println("Мероприятий еще нет");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.print("\r\n");
        }
    }

    private Map<String,String> getUsers(Document doc, String tagId){
        HashMap<String,String> users = new HashMap<String, String>();
        if (null == doc) return null;
        Element table = doc.getElementById(tagId);
        if (null == table) return null;
        Iterator<Element> rows = table.select("tr").iterator();
        while(rows.hasNext()){
            Iterator<Element> col = rows.next().select("td").iterator();
            if (null == col) continue;
            String id=null;
            String name=null;
            while(col.hasNext()){
                Element element = col.next();
                if(null == id || null==name) {
                    if (element.attr("headers").equals("ИД")) {
                        id = element.text();
                        continue;
                    }
                    if (element.attr("headers").equals("ФИО")) {
                        name = element.text();
                        continue;
                    }
                }else break;
            }
            if(null!=id && null!=name)
                users.put(id,name);
        }
        return users;
    }

    private List<PublicationDataSet> getPublications(Document document, String tagId) throws Exception {
        List<PublicationDataSet> publications = null;
        String json = getJsonByTagID(document,tagId);
        if (null == json) return null;
        //TODO
        publications = new PublicationsHandler().handle(new JsonReader().readJsonAsTree(json));
        return publications;
    }


    private List<EventDataSet> getEvents(Document document, String tagId) throws Exception {
        List<EventDataSet> events = null;
        String json = getJsonByTagID(document,tagId);
        if (null == json) return null;
        events = new EventHandler().handle(new JsonReader().readJsonAsTree(json));
        return events;
    }

    private Set<String> getMails(Document document) throws Exception {
        Set<String> mails =null;
        Element div = document.getElementById("left_main_person_info");
        if (null == div) return mails;
        Element contactElement = div.getElementsByAttributeValueContaining("data-mustache-template","person-contacts").first();
        if (null == contactElement) return mails;
        String json = contactElement.text();
        if (null == json || json.length()==0) return mails;
        mails = new MailJsonReader().readJsonAsTree(json);
        return mails;
    }

    private String getJsonByTagID(Document document, String tagId){
        if(null == document || null == tagId) return null;
        Element element = document.getElementById(tagId);
        if (null == element) return null;
        Element jsElement = element.getElementsByTag("script").first();
        if (null == jsElement) return null;
        return getJson(jsElement.toString());
    }

    private String getJson(String str){
        Matcher matcherStart = patternJs.matcher(str);
        if (matcherStart.find()) {
            String json=matcherStart.group(1);
            if(json!=null) return json;
        }
        return null;
    }



    //System.out.print(iterator.next().getElementsByAttribute(""));
    //System.out.println(iterator.next().getElementsByAttributeValueContaining("headers","ИД").);
    //System.out.println(iterator.next().attr("headers"));
    //System.out.println(iterator.next().getElementsByTag("td").attr("headers"));
    //System.out.println(iterator.next().);
    //System.out.println("text : "+iterator.next().text()); //kolom -1
    //System.out.println("text : "+iterator.next().text()); //kolom -2

}
