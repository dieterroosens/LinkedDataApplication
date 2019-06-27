package be.vub.Linking;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class URIprefix {
    public static final String osi = "http://ontologies.geohive.ie/osi#";
    public static final String geo = "http://www.opengis.net/ont/geosparql#";
    public static final String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String geof = "http://www.opengis.net/def/function/geosparql/";
    public static final String prov = "http://www.w3.org/ns/prov#";
    public static final String geoff = "http://ontologies.geohive.ie/geoff#";
    public static final String VoID = "http://rdfs.org/ns/void#";
    public static final String foaf = "http://xmlns.com/foaf/0.1/";
    public static final String owl = "http://www.w3.org/2002/07/owl#";
    public static final String xsd = "http://www.w3.org/2001/XMLSchema#";
    public static final String ns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";



    public String printAllForQuery(){

        StringBuffer sb = new StringBuffer();
        Class<URIprefix> c = URIprefix.class;
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    f.get(null).toString();
                    //return s.replace(f.get(null)+"","")+" ("+f.getName()+")";
                    sb.append(" PREFIX "+f.getName()+":"+"<"+f.get(null)+"> ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public boolean containsPrefix(String s){
        Class<URIprefix> c = URIprefix.class;
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    if(s.contains(f.get(null).toString())){
                        return true;

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public String replacePrefix(String s){
        Class<URIprefix> c = URIprefix.class;
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    if(s.contains(f.get(null).toString())){
                        return s.replace(f.get(null)+"","")+" ("+f.getName()+")";
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    public String replaceConstantByFullName(String s){
        Class<URIprefix> c = URIprefix.class;
        for (Field f : c.getDeclaredFields()) {
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && Modifier.isFinal(mod)) {
                try {
                    if(s.equals(f.getName().toString())){
                        return f.get(null).toString();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    public String adaptPredicate(String prop){
        //inDataset (VoID)filterDiv
        if(prop.indexOf("(")!=-1 && prop.indexOf("(")!=-1)
        {
            String prefix = prop.substring(prop.indexOf("(")+1, prop.indexOf(")"));
            String pred = prop.substring(0, prop.indexOf("("));

            //alert("prefix:"+prefix+" pred:"+pred+" input: "+prop);
            //if(prefix.length()!=0){
            return prefix+":"+pred;
        }
        else {
            return clearFilterDiv(prop);
        }
    }

    public String clearFilterDiv(String prop){
        //inDataset (VoID)filterDiv
        return prop.substring(0, prop.indexOf("filterDiv"));

    }

    public String adaptTypeValue(String prop){
        //inDataset (VoID)filterDiv
        String prefix = prop.substring(prop.indexOf("(")+1, prop.indexOf(")"));
        String pred = prop.substring(0, prop.indexOf("("));

        //alert("prefix:"+prefix+" pred:"+pred+" input: "+prop);
        if(prefix.length()!=0){
            return "<"+replaceConstantByFullName(prefix)+pred.trim()+">";
        }
        else {
            return clearFilterDiv(prop);
        }
    }
}
