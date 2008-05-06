package ru.amse.gomoku.ui.gui.tournament;

/**
 *
 */
public class StatisticsHolder {

    private String[] myNames;
    private Item[][] myItems;
    private int myLen;

    public StatisticsHolder(String[] namesOfPlayers) {
        myNames = namesOfPlayers;
        myLen = myNames.length;
        if (myLen == 1) {
            myLen++;
            myNames = new String[] {namesOfPlayers[0] + " first"
                                   , namesOfPlayers[0] + " second"};
        }
        myItems = new Item[myLen][myLen - 1];
        addItems();
    }

    private void addItems() {
        for (int i = 0; i < myLen; i++) {
            int k = 0;
            for (int j = 0; j < myLen; j++) {
                if (i == j) {
                    k--;
                } else if (!myNames[i].equals(myNames[j])) {
                    myItems[i][k] = new Item(myNames[i], myNames[j]);
                }
                k++;
            }
        }
    }

    public void addGameResult(boolean withWin
                             , String first
                             , String second
                             , String winner) {
        Item needed = search(first, second);
        if (withWin) {
            needed.add(winner);
        } else {
            needed.add();
        }
    }

    public int getWins(String first, String second) {
        int result = 0;

        Item item = search(first, second);
        result += item.getWins(first);
        item = search(second, first);
        return result + item.getWins(first);
    }

    public int getDraws(String first, String second) {
        int result = 0;

        Item item = search(first, second);
        result += item.getDraws();
        item = search(second, first);
        return result + item.getDraws();
    }

    private Item search(String first, String second) {

        for (int i = 0; i < myNames.length; i++) {
            if (myNames[i].equals(first)) {
                int k = 0;
                for (int j = 0; j < myNames.length; j++) {
                    if (j == i) {
                        k--;
                    } else if (myNames[j].equals(second)) {
                        return myItems[i][k];
                    }
                    k++;
                }
            }
        }
        return null;
    }

    private class Item {

        private String myFirstPlayer;
        private String mySecondPlayer;

        private int myFirstWins = 0;
        private int mySecondWins = 0;
        private int myDraws = 0;

        public Item(String first, String second) {
            myFirstPlayer = first;
            mySecondPlayer = second;
        }

        public void add(String name) {
            if (myFirstPlayer.equals(name)) {
                myFirstWins++;
            } else if (mySecondPlayer.equals(name)) {
                mySecondWins++;
            }
        }

        public void add() {
            myDraws++;
        }

        public boolean isMine(String name) {
            return myFirstPlayer.equals(name) || mySecondPlayer.equals(name);
        }

        public int getWins(String name) {
            if (myFirstPlayer.equals(name)) {
                return myFirstWins;
            } else if (mySecondPlayer.equals(name)) {
                return mySecondWins;
            }
            return -1;
        }

        public int getDraws() {
            return myDraws;
        }
    }
}
