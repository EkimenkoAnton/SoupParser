import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Created by Home on 09.10.2018.
 */
public class MailJsonReader {
    public Set<String> readJsonAsTree(String json) throws Exception{

        Set<String> mails = new HashSet<String>();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode contactNode = rootNode.path("contacts");

            if (contactNode.isMissingNode())  return mails;
            JsonNode corpemailNode = contactNode.path("corpemail");
            if (!corpemailNode.isMissingNode()) {
                String corpmail = corpemailNode.getTextValue();
                if (null!=corpmail) mails.add(corpmail);
            }
            JsonNode mailsNode = contactNode.path("email");
            if (mailsNode.isMissingNode())  return mails;
            JsonNode dataNode = mailsNode.path("data");
            if (dataNode.isMissingNode() || !dataNode.isArray()) return mails;

            Iterator<JsonNode> dataIterator = dataNode.iterator();
            while (dataIterator.hasNext()){
                JsonNode currentNode = dataIterator.next();
                JsonNode urlNode = currentNode.path("url");
                if(urlNode.isMissingNode())  continue;
                String mail = urlNode.getTextValue();
                if (null!=mail) mails.add(mail);
            }
        } catch (IOException e) {
            throw new Exception(e);
        }
        return mails;

    }//JsonReader
}
