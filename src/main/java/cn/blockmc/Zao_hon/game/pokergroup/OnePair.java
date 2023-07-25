package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.List;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.game.PokerCard;

public class OnePair extends AbstractPokerGroup {
	private List<PokerCard> cards;
	private int pairIndex = 0;

	private OnePair(List<PokerCard> cards, int pairIndex) {
		this.cards = cards;
		this.pairIndex = pairIndex;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			OnePair g = (OnePair) o;

			int pairCom = this.cards.get(pairIndex).getNum().compareTo(g.cards.get(g.pairIndex).getNum());
			if (pairCom != 0) {
				return pairCom;
			} else {
				for (int i = 0; i < cards.size(); i++) {
					if (i == pairIndex || i == pairIndex + 1) {
						continue;
					}
					int com = cards.get(i).getNum().compareTo(g.cards.get(i).getNum());
					if (com != 0) {
						return com;
					}
				}
			}
			return 0;
		}
	}

	public static OnePair fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		PokerCard card = null;
//		Application.logger.debug(list.toString());
		for (int i = 0; i < list.size(); i++) {
			PokerCard c = list.get(i);
			if (card != null && card.getNum() == c.getNum()) {
				return new OnePair(list, i - 1);
			}
			card = c;
		}
		return null;
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.ONE_PAIR;
	}

	@Override
	public String toString() {
		String str = "";

		for (int i = 0; i < cards.size(); i++) {
			if (i == pairIndex || i == pairIndex + 1) {
				continue;
			}
			PokerCard c = cards.get(i);
			str += (c + " ");
		}
		return "Pair " + cards.get(pairIndex).getNum() + " plus " + str;
	}

}
