package cn.blockmc.Zao_hon.game;

public class PokerCard {
	enum Decor {

		SPADE("Spade"), HEART("Heart"), DIAMOND("Diamond"), CLUB("Club");
		
		Decor(String name){
			this.name = name;
		}
		String name;
	}

	enum Num {
		A("1"), K("K"), Q("Q"), J("J"), N10("10"), N9("9"), N8("8"), N7("7"), N6("6"), N5("5"), N4("4"), N3("3"), N2("2");
		
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
