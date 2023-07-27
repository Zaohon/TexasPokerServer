package cn.blockmc.Zao_hon.game.pokergroup;

import java.util.Arrays;
import java.util.List;

import cn.blockmc.Zao_hon.game.PokerCard;
import cn.blockmc.Zao_hon.game.PokerCard.Num;

public class Straight extends AbstractPokerGroup {
	private List<PokerCard> cards;
	private final Num topNum ;

	private Straight(List<PokerCard> cards, Num topNum) {
		this.cards = cards;
		this.topNum = topNum;
	}

	@Override
	public int compareTo(AbstractPokerGroup o) {
		if (this.getType().compareTo(o.getType()) != 0) {
			return this.getType().compareTo(o.getType());
		} else {
			Straight s = (Straight) o;
			return topNum.compareTo(s.topNum);
		}
	}

	public static Straight fit(PokerCard[] cards) {
		List<PokerCard> list = Arrays.asList(cards.clone());
		list.sort((c1, c2) -> c1.getNum().compareTo(c2.getNum()));
		
		Num num = list.get(0).getNum();
		for (PokerCard c : list) {
			if(num.compareTo(c.getNum())==1) {
				num=c.getNum();
			}else {
				return null;
			}
		}
		return new Straight(list,num);
	}

	@Override
	public PokerGroupType getType() {
		return PokerGroupType.STRAIGHT;
	}

	@Override
	public String toString() {
		return "Straight with top" + topNum;
	}

}
