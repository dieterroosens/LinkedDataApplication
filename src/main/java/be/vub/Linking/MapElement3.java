package be.vub.Linking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MapElement3 {

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
    private String wkt;
    //private String latitude;
    //private String longitude;

    private List<Point> points;
    private List<List<Point>> multiPoints;

    private String type;
    private String labelEn; //English
    private String labelGa; //Irish
    private String form;
    private String function;
    private String geometry;
    private Date lastUpdated;

    public MapElement3(String uri, String wkt, String type, String labelEn, String labelGa, String form, String function, String geometry, String lastUpdated) {
        this.uri = uri;

        int lastBiggerThan =  wkt.lastIndexOf(">");
        int lastClosingBrackets =  wkt.lastIndexOf(")");
        //System.out.println("1:"+lastBiggerThan+" 2:"+lastClosingBrackets+" 3:"+wkt.length());
        if(lastBiggerThan> 0 && lastBiggerThan<lastClosingBrackets)
        {
            this.wkt= wkt.substring(lastBiggerThan+1, lastClosingBrackets+1);
        }
        else{
            this.wkt= wkt.substring(0, lastClosingBrackets+1);
        }

        //System.out.println("wkt: "+this.wkt);
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

    public String getWkt(){
        return wkt;
    }


    public String getPopupText(){
        //never " or \n

        //"elementUID", "labelEn", "UserName"
        String userName="testName";

        return  "<iframe src='interlink_input?elementURI="+uri+"&labelEn="+labelEn+"&userName="+userName+"' height='100%' width='150%' frameborder='0'/>";
                //"<script>" +
                //"function validateForm() {\n" +
                //"  var x = document.forms['interLinking']['object'].value;\n" +
                //"  x.replace('>','&gt;');" +
                //"  x.replace('<','&lt;');" +
                //"}\n" +
                //"</script>"+
                //"<b>uri:</b> "+ uri+"<br>"+
                //"<b>type:</b> <a href='"+uri+"'>"+ type+"</a><br>"+
                //"<b>label(en):</b> "+ labelEn+"<br>" +
                /*
                "<b>label(en):</b> <a href='"+uri+"'>"+ labelEn+"</a><br>"+


                        "<form name='interLinking' action='action_interlink' target='under' onsubmit='return validateForm()'>" +

                        "<table id='myTable' class='table order-list' border='1'>"+
    "<thead>"+
        "<tr>"+
            "<td>predicate</td>"+
            "<td>URI</td>"+
        "</tr>"+
		"<datalist id='interlinkingPredicates'>"+
		  "<option>withIn</option>"+
		  "<option>contains</option>"+
		  "<option>sameAs</option>"+
		  "<option>a</option>"+
		"</datalist>"+
    "</thead>"+
    "<tbody>"+
        "<tr>"+
            "<td class='col-sm-4'>"+
                "<input type='text' name='predicate' class='form-control' list='interlinkingPredicates'/>"+
            "</td>"+
            "<td class='col-sm-3'>"+
                "&lt;<input type='text' name='object'  class='form-control' title='example: <http://dbpedia.org/resource/Dublin>'/>&gt;"+
            "</td>"+
            "<td class='col-sm-2'><a class='deleteRow'></a>"+
            "</td>"+
        "</tr>"+
    "</tbody>"+
    "<tfoot>"+
        "<tr>"+
            "<td colspan='5' style='text-align: left;'>"+
                "<input type='button' class='btn btn-lg btn-block' id='addrow' value='Add Row' />"+
            "</td>"+
        "</tr>"+
        "<tr>"+
        "</tr>"+
    "</tfoot>"+
                        "<input type='submit' value='Submit'>" +
"</table>"+

                        "</form>";

*/


                /*
                        "<form action='action_sameas' target='under'>" +
                            "<b>sameAs:</b><input type='text' name='"+uri+"_sameAs' title='example: <http://dbpedia.org/resource/Dublin>' size='30'><br>" +
                            "<input type='submit' value='Submit sameAs'>" +
                        "</form>"+
                        "<form action='action_within' target='under'>" +
                            "<b>withIn:</b><input type='text' name='"+uri+"_withIn' title='example: <http://dbpedia.org/resource/Dublin>' size='30'><br>"+
                            "<input type='submit' value='Submit withIn'>" +
                        "</form>";
                */
    }

}
