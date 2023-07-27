package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.List;

import cn.blockmc.Zao_hon.game.PokerCard;

public class ThreeofaKind extends AbstractPokerGroup {
	private List<PokerCard> cards;
	private int kindIndex = 0;

	private ThreeofaKind(List<PokerCard> cards, int kindIndex) {
		this.cards = cards;
		this.kindIndex = kindIndex;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			ThreeofaKind t = (ThreeofaKind) o;

			int pairCom = this.cards.get(kindIndex).getNum().compareTo(t.cards.get(t.kindIndex).getNum());
			if (pairCom != 0) {
				return pairCom;
			} else {
				for (int i = 0; i < cards.size(); i++) {
					if (i == kindIndex || i == kindIndex + 1||i==kindIndex+2) {
						continue;
					}
					int com = cards.get(i).getNum().compareTo(t.cards.get(i).getNum());
					if (com != 0) {
						return com;
					}
				}
			}
			return 0;
		}
	}

	public static ThreeofaKind fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		PokerCard card = null;
		int count=1;
		for (int i = 0; i < list.size(); i++) {
			PokerCard c = list.get(i);
			if (card != null && card.getNum() == c.getNum()) {
				count++;
				if(count==3) {
					return new ThreeofaKind(list,i-2);
				}
			}else {
				count=1;
			}
			card = c;
		}
		return null;
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.THREE_OF_A_KIND;
	}

	@Override
	public String toString() {
		String str = "";

		for (int i = 0; i < cards.size(); i++) {
			if (i == kindIndex || i == kindIndex + 1||i==kindIndex+2) {
				continue;
			}
			PokerCard c = cards.get(i);
			str += (c + " ");
		}
		return "Three of a kind  " + cards.get(kindIndex).getNum() + " plus " + str;
	}

}
