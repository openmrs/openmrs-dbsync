package org.openmrs.eip.utils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

@Slf4j
public final class JsonUtils {

    private JsonUtils() {}

  
    
    /**
     * Utility method to extract and concatenate json array key values
     * @param jsonArray json array string
     * @param key key parameter whose values will be concatenated 
     * @return concatenated values for key of json array elements
     */
    public static String convertToValuesArrayForKey(final String jsonArray, final String key) {
    	if (jsonArray == null || jsonArray.isEmpty()) {
    		return "";
    	}
    	final StringBuilder builder = new StringBuilder("");
    	JSONArray array = new JSONArray(jsonArray);
    	array.forEach((item) -> {builder.append(",\"" + ((JSONObject)item).getString(key) + "\"");});
    	return builder.toString().substring(1);
    }
    
    /**
     * Utility method to determine if jsonArray contains item with matching key value pair
     * @param jsonArray json array string
     * @param key key parameter whose value will used for comparison 
     * @param value value to be compared with
     * @return true/false
     */
    public static boolean containsItemWithKeyEqualsValue(final String jsonArray, final String key, final String value) {
    	if (jsonArray == null || jsonArray.isEmpty()) {
    		return false;
    	}
    	JSONArray array = new JSONArray(jsonArray);
    	Iterator<Object> it = array.iterator();
    	while (it.hasNext()) {
    		if (value.equalsIgnoreCase(((JSONObject)it.next()).getString(key))) return true;
    	}
    	return false;
    }
    
    /**
     * Utility method to return item's property's value given the provided array contains the item
     * @param jsonArray json array string
     * @param propertyName property name whose value will be returned
     * @param key key parameter whose value will be compared 
     * @param value value to be compared with
     * @return property value
     */
    public static String getPropertyValueWhereKeyMatchesValue(final String jsonArray, final String propertyName, final String key, final String value) {    	
    	String propertyValue = "";
    	JSONArray array = new JSONArray(jsonArray);
    	Iterator<Object> it = array.iterator();
    	while (it.hasNext()) {
    		JSONObject obj = ((JSONObject)it.next());
    		if (value.equalsIgnoreCase(obj.getString(key))) {
    			propertyValue = obj.getString(propertyName);
    			break;
    		}
    	}
    	return propertyValue;
    }
}
