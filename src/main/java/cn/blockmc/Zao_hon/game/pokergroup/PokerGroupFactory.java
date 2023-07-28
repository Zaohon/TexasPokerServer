package cn.blockmc.Zao_hon.game.pokergroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.raistlic.common.permutation.Combination;

import cn.blockmc.Zao_hon.Application;
import cn.blockmc.Zao_hon.game.PokerCard;

public class PokerGroupFactory {

	public static AbstractPokerGroup findBestGroup(PokerCard[] hands, PokerCard[] shared) {
		PokerCard[] combCards = new PokerCard[5];
		AbstractPokerGroup group = null;

		for (List<PokerCard> cards : Combination.of(Arrays.asList(shared), 3)) {

			for (int i = 0; i < 2; i++) {
				combCards[i] = hands[i];
			}
			for (int i = 0; i < 3; i++) {
				combCards[i + 2] = cards.get(i);
			}

			AbstractPokerGroup newGroup = fitGroup(combCards);
			if (group == null) {
				group = newGroup;
			} else {
				group = newGroup.compareTo(group) > 0 ? newGroup : group;
			}
		}
		return group;
	}

	public static AbstractPokerGroup fitGroup(PokerCard[] cards) {
		for (Class<? extends AbstractPokerGroup> clazz : groups) {
			AbstractPokerGroup group = getFitGroup(clazz, cards);
			if (group != null) {
				return group;
			}

		}
		return null;

	}

	public static String printPokerCards(PokerCard[] cards, int lenth) {
		String str = "";
		for (int i = 0; i < lenth; i++) {
			PokerCard card = cards[i];
			str += (card + " ");
		}
		return str;
	}

	private static LinkedList<Class<? extends AbstractPokerGroup>> groups = new LinkedList<Class<? extends AbstractPokerGroup>>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		{
			this.add(Flush.class);
			this.add(Straight.class);
			this.add(ThreeofaKind.class);
			this.add(OnePair.class);
			this.add(Trash.class);

		}

	};

	private static AbstractPokerGroup getFitGroup(Class<? extends AbstractPokerGroup> clazz, PokerCard[] cards) {
		try {
			Method method = clazz.getDeclaredMethod("fit", cards.getClass());
			Object o = method.invoke(null, (Object) cards);
			return (AbstractPokerGroup) o;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
