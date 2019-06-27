package be.vub.Linking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Element {
    private String uri;
    private Map<String, List<String>> properties;

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

    public Element(String type, String predicate, String object) {
        properties = new HashMap<>();
        this.uri = type;
        addProperty(predicate, object);
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return uri +"<br>";
    }

    /*
    public String printProperties(){
        return "properties of "+uri+"<br>";

    }*/

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Element)
        {
            sameSame = this.uri == ((Element) object).uri;
        }

        return sameSame;
    }
}
