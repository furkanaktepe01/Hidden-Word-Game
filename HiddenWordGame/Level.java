package HiddenWordGame;

import java.util.*;

public class Level {

    int level;
    int guessChance;
    int tipChance;
    double HW_profit_percentage;
    double extraWordsProfit_coefficient;
    double extraGuessingChanceProfit_coefficient;
    int levelHardship;
    List<String> Table;

    public Level(String hiddenWord, String preferredTableSize) {
        if (hiddenWord.length() <= 4) {
            this.level = 1;
            this.guessChance = 5 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 2 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 70;
            this.extraWordsProfit_coefficient = 2;
            this.extraGuessingChanceProfit_coefficient = 4;
        } else if (hiddenWord.length() <= 6) {
            this.level = 2;
            this.guessChance = 8 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 2 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 75;
            this.extraWordsProfit_coefficient = 4;
            this.extraGuessingChanceProfit_coefficient = 6;
        } else if (hiddenWord.length() <= 8) {
            this.level = 3;
            this.guessChance = 10 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 3 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 80;
            this.extraWordsProfit_coefficient = 6;
            this.extraGuessingChanceProfit_coefficient = 8;
        } else if (hiddenWord.length() <= 10) {
            this.level = 4;
            this.guessChance = 12 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 4 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 85;
            this.extraWordsProfit_coefficient = 8;
            this.extraGuessingChanceProfit_coefficient = 10;
        } else if (hiddenWord.length() <= 12) {
            this.level = 5;
            this.guessChance = 14 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 5 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 90;
            this.extraWordsProfit_coefficient = 10;
            this.extraGuessingChanceProfit_coefficient = 15;
        } else {
            this.level = 6;
            this.guessChance = 16 + sizeToCoefficient(preferredTableSize);
            this.tipChance = 6 + sizeToCoefficient(preferredTableSize);
            this.HW_profit_percentage = 95;
            this.extraWordsProfit_coefficient = 12;
            this.extraGuessingChanceProfit_coefficient = 20;
        }

    }

    public static int sizeToCoefficient(String preferredTableSize) {
        int preferredTableSizeCoefficient = 0;
        if (preferredTableSize.equals("XS")) {
            preferredTableSizeCoefficient = -2;
        } else if (preferredTableSize.equals("S")) {
            preferredTableSizeCoefficient = -1;
        } else if (preferredTableSize.equals("M")) {
            preferredTableSizeCoefficient = 0;
        } else if (preferredTableSize.equals("L")) {
            preferredTableSizeCoefficient = 1;
        } else if (preferredTableSize.equals("XL")) {
            preferredTableSizeCoefficient = 2;
        }
        return preferredTableSizeCoefficient;
    }

    public void setLevelHardship(String hiddenWord, String preferredTableSize) {
        this.levelHardship = hiddenWord.length() + sizeToCoefficient(preferredTableSize);
    }


    public static ArrayList<String> pickRandomLetters(int num) {
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        ArrayList<String> lett = new ArrayList<>();
        ArrayList<Integer> rng = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(alphabet.length);
            rng.add(randomIndex);
        }
        for (int i = 0; i < rng.size(); i++) {
            lett.add(alphabet[rng.get(i)]);
        }
        return lett;
    }

    public static int occurrences(List<String> list, String letter){
        return Collections.frequency(list,letter);
    }

    public static List<String> returnTable(String hiddenWord, String preferredTableSize, boolean displayTable) {

        int preferredTableSizeCoefficient = sizeToCoefficient(preferredTableSize);

        int tableSize = (hiddenWord.length() + preferredTableSizeCoefficient) * (hiddenWord.length() + preferredTableSizeCoefficient);
        int size_others = tableSize - hiddenWord.length();
        ArrayList<String> lettersHW = new ArrayList<>();
        for (int i = 0; i < hiddenWord.length(); i++) {
            lettersHW.add(hiddenWord.split("")[i]);
        }
        ArrayList<String> allLetters = lettersHW;
        for (int i = 0; i < pickRandomLetters(size_others).size(); i++) {
            allLetters.add(pickRandomLetters(size_others).get(i));
        }
        List<String> orderedLetters = new ArrayList<>();
        ArrayList<Integer> range = new ArrayList<>();
        for (int i = 0; i < allLetters.size(); i++) {
            range.add(i);
        }
        for (int i = 0; i < allLetters.size(); i++) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(range.size());
            int j = range.get(randomIndex);
            range.remove(range.indexOf(j));
            orderedLetters.add(allLetters.get(j));
        }

        if(displayTable) {
            for (int i = 0; i < hiddenWord.length() + preferredTableSizeCoefficient; i++) {
                String line = "";
                for (int j = 0; j < hiddenWord.length() + preferredTableSizeCoefficient; j++) {
                    String letter = orderedLetters.get(0);
                    line = line + "   " + letter;
                    orderedLetters.remove(letter);
                }
                System.out.println(line);
            }
        }

        return allLetters;

    }

    public static ArrayList<String> copyLettersAsArray(String hiddenWord){

        ArrayList<String> hiddenWordCopy = new ArrayList<>();
        for (int i = 0; i < hiddenWord.length(); i++) {
            hiddenWordCopy.add(hiddenWord.split("")[i]);
        }

        return hiddenWordCopy;

    }

    public static String genRandomWord() {
        String text = "chance survival sincere smitten " +
                "lie fact woman feigning casual " +
                "detachment circumstances sadness simple " +
                "observation accusation man " +
                "contest law imagine disobey " +
                "make weak two final " +
                "turn oppress torture " +
                "kill evil intention pleasure " +
                "complete indifference men normal call love ";
        ArrayList<String> randomWords = new ArrayList<>(Arrays.asList(text.split(" ")));
        Random rand = new Random();
        int random = rand.nextInt(randomWords.size());
        return randomWords.get(random);
    }

    public static void gameIntro() {
        System.out.println("\nHidden Words Game\n\n" +
                "Welcome to the Hidden Words Game!\n" +
                "Aim of the game is finding the words which are created by the players and hidden in the letter tables.\n" +
                "To start the game each of two players should type their names and budgets. Game consists of 3 rounds.\n" +
                "In the first two rounds one player will type a word and other will try to find it, respectively.\n" +
                "In the final round both players will try to find a hidden word which is selected by the Casino.\n\n" +
                "The length of the hidden words and players' choice on the size of the table specifies the hardship of the level. The harder the level, the greater the profit or loss.\n" +
                "Also, the number of guessing and tip chances depends on the hardship of the level. To avoid inferring the length of the hidden word,\n" +
                "Players will not know how many chances there are, but will be notified in the last 3 chances.\n" +
                "Also, extra guessing chances gets converted to extra profit depending on the hardship of the level as well as the amount of the bets.\n" +
                "In case a player make a guess other than the hidden word, if the other player accepts the typed word valid, the player will gain from that word too.\n\n" +
                "In the first two rounds, depending on the level, certain amount of the bet of the opponent will be taken by the winner in the end of the round.\n" +
                "In the final round the winner takes the whole bet of the opponent, if both player lose, they both loses their whole bet.\n"

        );
    }

    public static void displayFinalResults(Player Player1, Player Player2, Player Casino) {

        double diff1 = (float) (Player1.budget - Player1.initialBudget);
        double diff2 = (float) (Player2.budget - Player2.initialBudget);

        if(diff1 >= 0) {
            System.out.println("\n\n\n" + Player1.name + "\nTotal Profit: " + diff1 + "$");
        } else {
            System.out.println(Player1.name + "\nTotal Loss: " + diff1 + "$");
        }
        System.out.println("Total Budget: " + (float)Player1.budget + "$\n");

        if(diff2 >= 0) {
            System.out.println(Player2.name + "\nTotal Profit: " + diff2 + "$");
        } else {
            System.out.println(Player2.name + "\nTotal Loss: " + diff2 + "$");
        }
        System.out.println("Total Budget: " + (float)Player2.budget + "$\n");

        double CasinoProfit = Casino.budget - Casino.initialBudget;
        System.out.println(Casino.name + "\nTotal Profit: " + (float)CasinoProfit + "$\n");

        System.out.println("Game is Over");

    }

}
