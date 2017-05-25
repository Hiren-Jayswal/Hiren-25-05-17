package testmap;

import java.io.*;
import java.util.*;

/* 
 * Assumption : Here map has been implemented by ArrayList.
 *  		  : Specified limit is set to 5. That means after 5th key/value insertion,
 *  		    Data will be stored to disk( Here Files as Files are stored into disk only)
 *			  : Here Key is the employee name and values are list of skills/experties he/she aquire. 
 * 
 * this map is able to handle multithreading environment also and get and put operation are thread safe.
 * Time complexity of this MAP is O(n) as it implements using ArrayList
 * 
 * */
 
class MapOpr {
	private static int count = 0;
	private List keys= Collections.synchronizedList(new ArrayList()), values = Collections.synchronizedList(new ArrayList());
	
	public synchronized  Object get(Object key)
	{
	    if (!keys.contains(key))
	    {
	    	return null;
	    }
	    else
	    {
	    	System.out.println(""+keys.get(keys.indexOf(key))+" : "+ values.get(keys.indexOf(key)));
	    }
	    return values.get(keys.indexOf(key));
	  }

	public synchronized Object put(Object key, Object value) {
		count++;
	    Object result = get(key);
	    if (!keys.contains(key)) {
	      keys.add(key);
	      values.add(value);
	    } else
	      values.set(keys.indexOf(key), value);
	    if(count == 5)
	    {
	    	try {
				FileOutputStream fos = new FileOutputStream("Backup");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(keys);
		    	oos.writeObject(values);
   		    	oos.close();
			}
			catch (FileNotFoundException  e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}	
			System.out.println("Oops...!! Maximum Memory limit reached (5 per map)..!! \n Your data has been saved to disk");
			count = 0;
			keys.clear();
			values.clear();
	    }
	    
	    return result;
	  }
}
public class TestMap implements Runnable
{	
	MapOpr m;
	Object kp,kg,v;
	Thread t;
		
		TestMap(MapOpr m1)
		{
			
			m=m1;
		}
	public void putval(Object k1,Object v1)
	{
		t=new Thread();
		kp=k1;v=v1;
		m.put(kp, v);
		t.start();
		
	}
	public void getval(Object k1)
	{
		kg=k1;
		Object ky = m.get(kg);
	}
	public void run()
	{
		
	}
	public static void main(String[] args) {
		MapOpr mo = new MapOpr();
		TestMap mm = new TestMap(mo);
		TestMap mm1 = new TestMap(mo);
		TestMap mm3 = new TestMap(mo);
		
		mm.putval("Hiren","Java,python");
		mm.putval("Bhavin","Tally,accounts");
		mm1.putval("Chirag","Ruby");
		mm1.putval("Dhaval","VLSI,C,C++");
		mm3.putval("Eshan","BIG DATA,hadoop,R");
		mm3.putval("Foram","machine learning,AI,Operating system");
		
		mm.getval("Hiren");
		mm.getval("Bhavin");
		mm1.getval("Chirag");
		mm1.getval("Dhaval");
		mm3.getval("Eshan");
		mm3.getval("Foram");
	}

}
