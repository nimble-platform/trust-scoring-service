package nimble.trust.util.tuple;



public interface TFunctor<T> {

	T apply(Tuple2<?, ?> t);

}
