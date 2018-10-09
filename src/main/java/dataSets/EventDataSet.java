package dataSets;

/**
 * Created by Home on 08.10.2018.
 */
public class EventDataSet {
    public String name;
    public String dates;
    public String year;
    public String type;
    public String rank;
    public String role;

    @Override
    public String toString() {
        return "\t"+type + '\t'
                + rank + '\t'
                + year  + '\t'
                + dates  + '\t'
                + role  + '\t'
                + name;
    }
    public String header() {
        return "\tТип\tРанг\tГод\tСроки\tРоль\tНаименование";
    }

}
