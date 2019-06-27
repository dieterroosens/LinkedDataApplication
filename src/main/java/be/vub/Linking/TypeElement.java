package be.vub.Linking;

import java.util.*;

public class TypeElement {
    private String label;//label
    private List<Element> elementList;
    private Map<String, List<String>> properties;
    private int counter;

    public TypeElement(String type, int counter, Element firstElement) {
        //this.label = property.substring(property.lastIndexOf("#") + 1);
        this.label = type;
        this.counter = counter;
        elementList=new ArrayList<>();
        elementList.add(firstElement);
        properties=new HashMap<>();
        addPropertiesOfElement(firstElement);
    }

    private void addPropertiesOfElement(Element firstElement) {
        Iterator it = firstElement.getProperties().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            addProperty(pair.getKey().toString(), (List<String>) pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void addProperty(String property, List<String> values){
        for (String temp : values){
            addProperty(property, temp);
        }
    }

    public void addProperty(String predicate, String object){
        if(properties.containsKey(predicate)){
            List<String> objects=properties.get(predicate);
            objects.add(object);
        }
        else{
            List<String> values=new ArrayList<>();
            values.add(object);
            properties.put(predicate, values);
        }
    }


    public void increaseCounter() {
        this.counter++;
    }

    public String getLabel() {
        return label;
    }

    public void addElement(Element element){
        elementList.add(element);
        addPropertiesOfElement(element);
    }

    public void updateElement(Element e, String predicate, String object){
        int i =getElementPosition(e);
        Element element = elementList.get(i);
        element.addProperty(predicate, object);
        addProperty(predicate, object);
    }

    public boolean containsElement(Element element){
        return elementList.contains(element);
    }

    private int getElementPosition(Element element){
        return elementList.indexOf(element);
    }


    @Override
    public String toString() {
        return label + "(" + counter +" element(s) of this type)<br>";

    }

    public String printAllProperties() {
        StringBuffer s = new StringBuffer( label + "(" + counter +" element(s) of this class)<br>");
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            s.append("---"+pair.getKey() +"("+((List<String>) pair.getValue()).size()+" element(s) with this predicate)<br>");
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return s.toString();
    }

    public String printAllPropertiesAndValues() {
        StringBuffer s = new StringBuffer( "*"+label + "(" + counter +" element(s) of this type)<br>");
        Iterator it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            String property = label.substring(label.lastIndexOf("#") + 1) + ((String) pair.getKey()).substring(((String) pair.getKey()).lastIndexOf("#") + 1);
            String filterDivName= property + "filterDiv";
            s.append("<input type=\"button\"  id=\""+property+"Button\" onclick=\"showHideFilterElements('"+filterDivName+"','"+property+"Button')\" value=\"+\"> "+pair.getKey() +"("+((List<String>) pair.getValue()).size()+" element(s) with this predicate)");
            //autocomplete field here
            s.append(autocomplete(property));
            s.append(autocompleteJSValuesArray(property, (List<String>) pair.getValue()));
            //end autocomplete field
            s.append("<br>");
            s.append("<div id='"+filterDivName+"' style=\"display:none\">");
            for(String value : (List<String>)pair.getValue()) {
                s.append("<input type=\"checkbox\" name=\""+((String)pair.getKey())+"\" value=\""+value+"\">"+value+"<br>");
            }
            s.append("</div>");
            //it.remove(); // avoids a ConcurrentModificationException
        }
        return s.toString();
    }

    //https://www.w3schools.com/howto/howto_js_autocomplete.asp
    private String autocomplete(String property){
        StringBuffer s = new StringBuffer("<div class=\"autocomplete\" style=\"width:300px;\">\n" +
                                            "    <input id=\"myInput"+property+"\" type=\"text\" name=\"my"+property+"\" placeholder=\"Autocomplete Field\">\n" +
                                            "  </div>");
        return s.toString();
    }

    private String autocompleteJSValuesArray(String name, List<String> values){
        StringBuffer s = new StringBuffer("<script>\n" +
                "var "+name+" = [");
        Iterator i = values.iterator();
        for (;;) {
            String value= (String)i.next();
            s.append("\""+value.substring(value.lastIndexOf("#") + 1)+"\"");
            if (! i.hasNext()) break;
            s.append(", ");
        }
        s.append("];\n");
        s.append("autocomplete(document.getElementById(\"myInput"+name+"\"), "+name+");");
        s.append("</script>");
        return s.toString();
    }
}
