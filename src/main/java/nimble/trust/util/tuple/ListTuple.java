package nimble.trust.util.tuple;



import java.util.List;

import com.google.common.collect.Lists;

public class ListTuple<T> {

	public static synchronized <T extends Object, T1 extends Object, T2 extends Object> List<T> toList(List<Tuple2<T1, T2>> listTuple,
			TFunctor<T> functor) {
		List<T> list = Lists.newArrayList();
		for (Tuple2<?, ?> t : listTuple) {
			list.add(functor.apply(t));
		}
		return list;
	}



}
