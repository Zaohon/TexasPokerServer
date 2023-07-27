package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.List;

import cn.blockmc.Zao_hon.game.PokerCard;

public class Flush extends AbstractPokerGroup {
	private List<PokerCard> cards;

	private Flush(List<PokerCard> cards) {
		this.cards = cards;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			Flush f = (Flush) o;

			for (int i = 0; i < 5; i++) {
				int com = cards.get(i).getNum().compareTo(f.cards.get(i).getNum());
				if (com != 0) {
					return com;
				}
			}
			return 0;
		}
	}

	public static Flush fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		if(list.stream().map(c->c.getNum()).distinct().count()==1) {
			return new Flush(list);
		}
		return null;
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.FLUSH;
	}

	@Override
	public String toString() {
		String str = "";
		return "Flush " + cards.get(0).getDecor();
	}

}
