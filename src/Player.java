import ingenium.math.Vec3;

public class Player {
    private Card cards[];

    public Player (int handSize) {
        cards = new Card[handSize];
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public void update (ingenium.Time time) {
        cards[0].getMeshes()[0].setRotation(cards[0].getMeshes()[0].getRotation().add(new Vec3(0, 1, 0).mulFloat(time.getDeltaTime())));
    }

    public void setRandomCards (Card allCards[]) {
        for (int i = 0; i < cards.length; i++) {
            cards[i] = allCards[(int)((float) allCards.length * Math.random())];
        }
    }
}
