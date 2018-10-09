package dataSets;

/**
 * Created by Home on 08.10.2018.
 */
public class PublicationDataSet{
    public String type;
    public String description;
    public String year;
    public String magazineType;

    @Override
    public String toString() {
        return "\t"+type + '\t'
                + magazineType + '\t'
                + year  + '\t'
                + description;
    }

    public String header() {
        return "\tТип\tИндексирование\tГод\tВыходные данные";
    }

}