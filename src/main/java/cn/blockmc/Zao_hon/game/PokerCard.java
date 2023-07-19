package cn.blockmc.Zao_hon.game;

public class PokerCard {
	enum Decor {
		SPADE, HEART, DIAMOND, CLUB;
	}

	enum Num {
		A, K, Q, J, N10, N9, N8, N7, N6, N5, N4, N3, N2;
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

	public String getDesc() {
		return "("+decor.name() + " " + num.name()+")";
	}
	
	@Override
	public String toString() {
		return decor + " " + num;
	}
}
