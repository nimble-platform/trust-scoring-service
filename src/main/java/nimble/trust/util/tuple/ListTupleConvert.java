package nimble.trust.util.tuple;



import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * ListTupleConvert util
 *@author markov
 *
 */
public class ListTupleConvert {
	
	@SuppressWarnings("unchecked")
	public static synchronized <Element extends Object> List<Element> toListOfTupleElement(List<?> setOFTuples, int element){
		List<Element> set = Lists.newArrayList();
		for (Object tuple : setOFTuples) {
			try {
				Method  m = tuple.getClass().getMethod("getT"+Integer.valueOf(element).toString(), new Class<?>[0]);
				Element result = (Element) m.invoke(tuple, new Object[0]);
				set.add(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return set;
	}

}
