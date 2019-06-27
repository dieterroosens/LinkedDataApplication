package be.vub.Linking;

public class FilterElement {
    private String objectField;
    private int counter;
    private String property;

    public FilterElement(String property, String objectField, int counter) {
        this.property = property.substring(property.lastIndexOf("#") + 1);
        this.objectField = objectField;
        this.counter = counter;
    }

    public void increaseCounter() {
        this.counter++;
    }

    public String getObjectField() {
        return objectField;
    }

    @Override
    public String toString() {
        return "<input type=\"checkbox\" name=\""+property+"\" value=\""+objectField+"\">" + objectField + "(" + counter +" element(s))<br>";

    }
}
