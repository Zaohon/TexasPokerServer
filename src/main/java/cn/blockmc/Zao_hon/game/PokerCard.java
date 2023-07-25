package cn.blockmc.Zao_hon.game;

public class PokerCard {
	public enum Decor {

		SPADE("Spade"), HEART("Heart"), DIAMOND("Diamond"), CLUB("Club");
		
		Decor(String name){
			this.name = name;
		}
		String name;
	}

	public enum Num {
		 N2("2"),N3("3"),N4("4"), N5("5"),  N6("6"), N7("7"),N8("8"), N9("9"), N10("10"),  J("J"), Q("Q"), K("K"), A("1");
		
		Num(String name){
			this.name = name;
		}
		String name;
	}

	private Decor decor;
	private Num num;

	public PokerCard(Decor decor, Num num) {
		this.decor = decor;
		this.num = num;
	}

	public Decor getDecor() {
		return decor;
	}

	public Num getNum() {
		return num;
	}
	
	@Override
	public String toString() {
		return "{"+decor.name + " " + num.name+"}";
	}
}
