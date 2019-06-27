package be.vub.Linking.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapElement {

    private String uri;
    //wkt point(longitude, latitude)
    private String latitude;
    private String longitude;

    private String type;
    private String labelEn; //English
    private String labelGa; //Irish
    private String form;
    private String function;
    private String geometry;
    private Date lastUpdated;

    public MapElement(String uri, String wkt, String type, String labelEn, String labelGa, String form, String function, String geometry, String lastUpdated) {
        this.uri = uri;

        //"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT (-6.48776054541606 53.3711604511507)" ^^<http://www.opengis.net/ont/geosparql#wktLiteral>
        //System.out.println("ori_wkt"+wkt);
        String tempwkt = wkt.substring(wkt.lastIndexOf("(") + 1, wkt.lastIndexOf(")") );
        //System.out.println("ori_wkt"+tempwkt);
        String[] parts = tempwkt.split(" ");
        this.latitude = parts[1];
        this.longitude = parts[0];

        //System.out.println("after:1:"+latitude+" 2:"+longitude);

        this.type = type;
        this.labelEn = labelEn;
        this.labelGa = labelGa;
        this.form = form;
        this.function = function;
        this.geometry = geometry;

        try {
            this.lastUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastUpdated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String giveLeafletPoint(){
        return latitude+", "+longitude;
    }

    public String getPopupText(){
        /*return "<b>latitude:</b>"+ latitude+"<br>"+
                "Testing popupin element"+labelEn;*/
        return "<b>latitude:</b> "+ latitude+"<br>"+
                "<b>longitude:</b> "+ longitude+"<br>"+
                "<b>type:</b> "+ type+"<br>"+
                "<b>labelEn:</b> "+ labelEn+"<br>"+
                "<b>labelGa:</b> "+ labelGa+"<br>"+
                "<b>form:</b> "+ form+"<br>"+
                "<b>function:</b> "+ function+"<br>"+
                "<b>geometry:</b> "+ geometry+"<br>"+
                "<b>lastUpdated:</b> "+ lastUpdated.toString()+"<br>";
    }
}
