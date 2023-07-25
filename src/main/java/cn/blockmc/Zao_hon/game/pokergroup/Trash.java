package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.blockmc.Zao_hon.game.PokerCard;

public class Trash extends AbstractPokerGroup {

	private List<PokerCard> cards;

	private Trash(List<PokerCard> cards) {
		this.cards = cards;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			Trash t = (Trash) o;

			for (int i = 0; i < 5; i++) {
				int com = cards.get(i).getNum().compareTo(t.cards.get(i).getNum());
				if (com != 0) {
					return com;
				}
			}
			return 0;
		}
	}

	public static Trash fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		return new Trash(list);
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.TRASH;
	}

	@Override
	public String toString() {
		String str = "";
		for (PokerCard card : cards) {
			str += card.getNum().name() + " ";
		}
		return "Trash:" + str;
	}

}
