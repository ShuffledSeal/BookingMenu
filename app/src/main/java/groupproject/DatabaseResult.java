package groupproject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

final class DatabaseResult {
    private ArrayList<LinkedHashMap<String, String>> resources;

    public DatabaseResult() {
        this.resources = new ArrayList<LinkedHashMap<String, String>>();
    }

    /*
     * numberOfResources
     * The number of resources from the database query
     * params:
     * None
     * returns:
     * int
     */
    public int numberOfResources() {
        return resources.size();
    }

    public int numberOfLabels() {
        return resources.get(0).size();
    }

    private String gVal(String label, int index) {
        if (numberOfResources() == 0) {
            return "";
        }
        return resources.get(index).get(label);
    }

    /*
     * getValue
     * Get the label's corresponding value for the FIRST resource
     * params:
     * String label -> The column's name/label
     * returns:
     * String
     */
    public String getValue(String label) {
        return gVal(label, 0);
    }

    /*
     * getValue
     * Get the label's corresponding value for the index's resource
     * params:
     * String label -> The column's name/label
     * int index -> The resource index
     * returns:
     * String
     */
    public String getValue(String label, int index) {
        return gVal(label, index);
    }

    private String[] gLab(int index) {
        Object[] vals = resources.get(index).keySet().toArray();
        String[] sVals = new String[vals.length];
        for (int i = 0; i < vals.length; i++) {
            sVals[i] = vals[i].toString();
        }
        return sVals;
    }

    /*
     * getLabels
     * Get an array of column labels for the FIRST resource
     * params:
     * None
     * returns:
     * String[]
     */
    public String[] getLabels() {
        return gLab(0);
    }

    /*
     * getValue
     * Get an array of column labels for the index's resource
     * params:
     * int index
     * returns:
     * String[]
     */
    public String[] getLabels(int index) {
        return gLab(index);
    }

    public String getLabel(int columnIndex, int resourceIndex) {
        return gLab(resourceIndex)[columnIndex];
    }

    /*
     * addNewResource
     * Add a new resource from the database query
     * params:
     * HashMap<String, String> newResource -> The new resource, where each key in
     * the hashmap is a column label. One hashmap relates to one row in the table
     * returns:
     * void
     */
    public void addNewResource(LinkedHashMap<String, String> newResource) {
        resources.add(newResource);
    }

    public ArrayList<LinkedHashMap<String, String>> getResources() {
        return resources;
    }
}