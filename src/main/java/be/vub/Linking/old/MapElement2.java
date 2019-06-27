package be.vub.Linking.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MapElement2 {

    public class Point{
        private String latitude;
        private String longitude;

        public Point(String latitude, String longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getPoint() {
            return "["+latitude+", "+longitude+"]";
        }
    }
    private String uri;
    //wkt point(longitude, latitude)
    private String latitude;
    private String longitude;

    private List<Point> points;
    private List<List<Point>> multiPoints;

    private String type;
    private String labelEn; //English
    private String labelGa; //Irish
    private String form;
    private String function;
    private String geometry;
    private Date lastUpdated;

    public MapElement2(String uri, String wkt, String type, String labelEn, String labelGa, String form, String function, String geometry, String lastUpdated) {
        this.uri = uri;
        points= new ArrayList<>();
        //"<http://www.opengis.net/def/crs/EPSG/0/4326> POINT (-6.48776054541606 53.3711604511507)" ^^<http://www.opengis.net/ont/geosparql#wktLiteral>
        if(wkt.startsWith("POLYGON")) {
            String tempwkt = wkt.substring(wkt.lastIndexOf("(") + 1, wkt.lastIndexOf(")") - 1);
            String[] wktPoints = tempwkt.split(", ");

            for (String p : wktPoints) {
                String[] temp_p = p.split(" ");
                this.latitude = temp_p[1];
                this.longitude = temp_p[0];
                points.add(new Point(latitude, longitude));
            }
        }
        else{
            /*if (wkt.startsWith("MULTIPOLYGON")) {
                String temp1wkt = wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")") - 1);
                //TODO split multipolyon into lists of polygons
                //TODO Adapt data structure: http://arthur-e.github.io/Wicket/
                String tempwkt = wkt.substring(wkt.indexOf("(") + 1, wkt.indexOf(")"));

            } else {*/
                String tempwkt = wkt.substring(wkt.lastIndexOf("(") + 1, wkt.lastIndexOf(")"));
                System.out.println("ori_wkt" + tempwkt);
                String[] parts = tempwkt.split(" ");
                this.latitude = parts[1];
                this.longitude = parts[0];
                points.add(new Point(latitude, longitude));
            //}
        }
        //System.out.println("after:1:"+latitude+" 2:"+longitude);

        this.type = type;
        this.labelEn = labelEn;
        this.labelGa = labelGa;
        this.form = form;
        this.function = function;
        this.geometry = geometry;

        if(!lastUpdated.isEmpty()) {
            try {
                this.lastUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(lastUpdated);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String giveLeafletPoint(){
        return points.get(0).getPoint();
    }

    public String giveAllPoints(){
        StringBuffer s=new StringBuffer();
        Iterator i = points.iterator();
        s.append("[\n");
        for (;;) {
            Point value= (Point)i.next();
            s.append(value.getPoint());
            if (! i.hasNext()) break;
            s.append(", ");
        }
        s.append("]\n");

        return s.toString();
        //return points.get(0).getPoint();
    }

    public List<Point> getPoints() {
        return points;
    }



    public String getPopupText(){
        /*return "<b>latitude:</b>"+ latitude+"<br>"+
                "Testing popupin element"+labelEn;*/
        return "<b>latitude:</b> "+ latitude+"<br>"+
                "<b>longitude:</b> "+ longitude+"<br>"+
                "<b>type:</b> "+ type+"<br>"+
                "<b>labelEn:</b> "+ labelEn+"<br>"+
                "<b>labelGa:</b> "+ labelGa+"<br>"+
                //"<b>form:</b> "+ form+"<br>"+
                "<b>function:</b> "+ function+"<br>"+
                "<b>geometry:</b> "+ geometry+"<br>";
                //"<b>lastUpdated:</b> "+ lastUpdated.toString()+"<br>";
    }
}
