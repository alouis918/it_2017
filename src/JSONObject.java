/**
 * 
 */

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Freddy
 *
 */
public class JSONObject implements IJsonSerialize {
	private String JSONString=null;
	private Map<String, Object> internalValues =null;

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#addString(java.lang.String, java.lang.String)
	 */
	public JSONObject() {
		// TODO Auto-generated constructor stub
		internalValues=new HashMap<String,Object>();
	}
	@Override
	public void addString(String key, String str) {
		// TODO Auto-generated method stub
		internalValues.put(key, str);
	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#addInteger(java.lang.String, int)
	 */
	@Override
	public void addInteger(String key, int num) {
		// TODO Auto-generated method stub
		internalValues.put(key, num);

	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#addDouble(java.lang.String, double)
	 */
	@Override
	public void addDouble(String key, double num) {
		// TODO Auto-generated method stub
		internalValues.put(key, num);
	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#addArray(java.lang.String, java.util.Map)
	 */
	@Override
	public void addArray(String key, Map<String, Object> array) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#getString()
	 */
	@Override
	public String getString() {
		// TODO Auto-generated method stub
		JSONString="{";
		for (Map.Entry<String,Object> element : internalValues.entrySet()) 
		{
			JSONString+=element.getKey()+":"+element.getValue().toString()+",";
		}
		JSONString+= "}";		
		return JSONString.replaceFirst("(,.$)", "}");
	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#parseString(java.lang.String)
	 */
	@Override
	public void parseString(String str) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile(":");
		 Matcher m = p.matcher(str);
		 Boolean keyFound=true; String key=null,val=null;
		 while(m.find())
		 {
			 if(keyFound)
			  {
				key= m.group();
				 keyFound=false;
			  }
			 else
			 {
				 val=m.group();
				 keyFound=true;
			 }
			 internalValues.put(key, val);
		 }

	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#getObjects()
	 */
	@Override
	public Map<String, Object> getObjects() {
		// TODO Auto-generated method stub
		
		return internalValues;
	}

	/* (non-Javadoc)
	 * @see jsonLibrary.IJsonSerialize#getKey(java.lang.String)
	 */
	@Override
	public Object getKey(String key) {
		// TODO Auto-generated method stub
		
		return internalValues.get(key);
	}

}
