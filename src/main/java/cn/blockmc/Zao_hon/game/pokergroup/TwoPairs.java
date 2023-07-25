package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.List;

import cn.blockmc.Zao_hon.game.PokerCard;

public class TwoPairs extends AbstractPokerGroup {
	private List<PokerCard> cards;
	private int pairIndex1 = 0;
	private int pairIndex2 = 0;

	private TwoPairs(List<PokerCard> cards, int pairIndex1, int pairIndex2) {
		this.cards = cards;
		this.pairIndex1 = pairIndex1;
		this.pairIndex2 = pairIndex2;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			TwoPairs t = (TwoPairs) o;

			int pairCom2 = this.cards.get(pairIndex2).getNum().compareTo(t.cards.get(t.pairIndex2).getNum());
			if (pairCom2 != 0) {
				return pairCom2;
			}
			int pairCom1 = this.cards.get(pairIndex1).getNum().compareTo(t.cards.get(t.pairIndex1).getNum());
			if (pairCom1 != 0) {
				return pairCom1;
			}
			for (int i = 0; i < cards.size(); i++) {
				if (i == pairIndex1 || i == pairIndex1 + 1 || i == pairIndex2 || i == pairIndex2 + 1) {
					continue;
				}
				int com = cards.get(i).getNum().compareTo(t.cards.get(i).getNum());
				if (com != 0) {
					return com;
				}
			}

			return 0;
		}
	}

	public static TwoPairs fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		PokerCard card = null;
		int pairIndex1 = 0;
		int pairIndex2 = 0;
		for (int i = 0; i < list.size(); i++) {
			PokerCard c = list.get(i);
			if (card != null && card.getNum() == c.getNum()) {
				if (pairIndex1 == 0) {
					pairIndex1 = i - 1;
				} else {
					pairIndex2 = i - 1;
					return new TwoPairs(list, pairIndex1, pairIndex2);
				}
			}
			card = c;
		}
		return null;
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.TWO_PAIRS;
	}

	@Override
	public String toString() {
		String str = "";

		for (int i = 0; i < cards.size(); i++) {
			if (i == pairIndex1 || i == pairIndex1 + 1 || i == pairIndex2 || i == pairIndex2 + 1) {
				continue;
			}
			PokerCard c = cards.get(i);
			str += (c + " ");
		}
		return "Pair " + cards.get(pairIndex1).getNum() + " and Pair " + cards.get(pairIndex2).getNum() + " plus "
				+ str;
	}

}
