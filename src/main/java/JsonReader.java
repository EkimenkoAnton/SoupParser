/**
 * Created by Home on 08.10.2018.
 */

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonReader {

    public List<List<String>> readJsonAsTree(String json) throws Exception{

        List<List<String>> rows = new ArrayList<List<String>>();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode dataNode = rootNode.path("data");

            if (dataNode.isMissingNode() || ! dataNode.isArray()) throw new Exception("data node is missing"); // make cusom exception

            Iterator<JsonNode> rootIterator = dataNode.iterator();
            while (rootIterator.hasNext()){

                JsonNode currentNode = rootIterator.next();

                if (currentNode.isMissingNode() || ! currentNode.isArray()) throw new Exception("config node is missing");

                Iterator<JsonNode> innerIterator = currentNode.iterator();
                List<String> cols = new ArrayList();
                while (innerIterator.hasNext()){
                    cols.add(innerIterator.next().getTextValue());
                }
                rows.add(cols);

            }

        } catch (IOException e) {
            throw new Exception(e);
        }
        return rows;

    }//JsonReader




}


