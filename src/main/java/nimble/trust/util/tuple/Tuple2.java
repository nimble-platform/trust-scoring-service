package nimble.trust.util.tuple;



public class Tuple2<T1, T2> extends Tuple{
	

	private T1 T1;
	
	private T2 T2;
	
	public Tuple2(T1 t1, T2 t2){
		this.T1 = t1;
		this.T2 = t2;
	}
	
	public T1 getT1() {
		return T1;
	}
	
	
	public T2 getT2() {
		return T2;
	}
	
	public void setT1(T1 t){
		T1 = t;
	}
	
	public void setT2(T2 t){
		T2 = t;
	}
	
	
	
	

}
