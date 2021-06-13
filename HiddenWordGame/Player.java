package HiddenWordGame;

import java.util.*;

public class Player {

    String name;
    double budget;
    double initialBudget;
    int guessNum;
    int tipNum;
    String displayedTip;

    public Player(String name, double budget, double initialBudget) {
        this.name = name;
        this.budget = budget;
        this.guessNum = 0;
        this.tipNum = 0;
        this.initialBudget = initialBudget;
        this.displayedTip = "";
    }
        
    public static void guessCycle(Level Round, Player Casino, Player Player1, Player Player2,
                                  String hiddenWord, ArrayList<String> hiddenWordCopy, double P1Bet, double P2Bet, int round_num, Scanner scanner) {

        System.out.println("Make your guess " + Player1.name + ":");
        String guess = scanner.nextLine();
        boolean Player1wins = false;

        if (guess.toLowerCase().equals(hiddenWord)) {
            Player1.guessNum++;

            double P1HWProfit = (Round.HW_profit_percentage / 100.0) * P2Bet;
            double Casinos_profit = P2Bet - P1HWProfit;
            Casino.budget = Casino.budget + Casinos_profit;
            Player2.budget = Player2.budget - P2Bet;
            Player1.budget = Player1.budget + P1HWProfit;
            Player1wins = true;
            System.out.println("\n" + Player1.name + " won the Round " + round_num + " !\n" +
                        "Hidden word was: " + hiddenWord + "\n" +
                        Player1.name + " gained " + (int) Round.HW_profit_percentage + "% of " + Player2.name + "'s bet, which is " + (float) P1HWProfit + "$\n" +
                        Player2.name + " has lost the whole bet.");
            if (Player1.guessNum < Round.guessChance) {
                int extraGuesses = Round.guessChance - Player1.guessNum;
                double extraGuessesProfit = Round.extraGuessingChanceProfit_coefficient * extraGuesses;
                if (extraGuessesProfit >= Casino.budget/8){
                    extraGuessesProfit = Casino.budget/8;
                }
                Casino.budget = Casino.budget - extraGuessesProfit;
                Player1.budget = Player1.budget + extraGuessesProfit;
                if (extraGuesses == 1) {
                     System.out.println(Player1.name + " also gained " + (float)extraGuessesProfit + "$" +
                                " for the remaining 1 guessing chance.");
                } else {
                    System.out.println(Player1.name + " also gained " + (float)extraGuessesProfit + "$" +
                             " because of the remaining " + extraGuesses + " guessing chances.");
                }
            }

            Player1.guessNum = Round.guessChance;

            System.out.println("\nTotal Profit of " + Player1.name + ": " + (float) (Player1.budget - Player1.initialBudget) + "$");
            System.out.println("Total Budget of " + Player1.name + ": " + (float) Player1.budget + "$");
            System.out.println("Total Budget of " + Player2.name + ": " + (float) Player2.budget + "$");

        } else {

            List<String> Table = Round.Table;

            if(guess.length() > Table.size()){
                System.out.println("Length of your guess cannot be greater than the size of the table.");
            }

            else if (guess.length() < 2 ) {

                if (guess.equals("T")) {
                    boolean noRequestChance = false;
                    if (Player1.tipNum < Round.tipChance && Player1.tipNum < hiddenWord.length() / 2) {
                        Player1.guessNum = Player1.guessNum + 2;
                        Player1.displayedTip = Player1.displayedTip + hiddenWordCopy.get(0);
                        hiddenWordCopy.remove(0);
                        Player1.tipNum++;
                        System.out.println("Tip: " + Player1.displayedTip);

                    } else {
                        System.out.println("You have no chances to request a tip.");
                        noRequestChance = true;
                    }
                    int remainingChances = Round.guessChance - Player1.guessNum;
                    if (0 < remainingChances && remainingChances <= 3 && !noRequestChance) {
                        System.out.println(Player1.name + " has last " + remainingChances + " chances to guess.");
                    }

                }  else {
                    if(hiddenWord.length() == 0){
                        System.out.println("Hidden word cannot be empty space.");
                    } else {
                        System.out.println("Hidden word cannot be a single letter.");
                    }

                }

            } else {

                boolean occurrenceError = false;
                List<String> guessLetterList = Level.copyLettersAsArray(guess.toLowerCase());
                for(int i=0; i < guess.length(); i++){
                    String letter = guessLetterList.get(i);
                    int occurGuess = Level.occurrences(guessLetterList, letter);
                    int occurTable = Level.occurrences(Table, letter);
                    if(occurGuess > occurTable){
                        occurrenceError = true;
                        if(occurTable == 0){
                            System.out.println("There is no '" + letter + "' in the table.");
                        } else {
                            System.out.println("The letter '" + letter + "' occurs only " + occurTable + " times in the table.");
                        }
                        System.out.println("So your guess cannot be " + guess + ".");
                        break;
                    }
                }

                if (!occurrenceError) {

                    Player1.guessNum++;

                    String P2sValidation = "";
                    System.out.println(guess + " is not the hidden word.\nDo you accept " + guess + " as a valid word " + Player2.name + " ?\nType yes or no:");
                    while (!P2sValidation.equals("yes") && !P2sValidation.equals("no")) {
                        P2sValidation = scanner.nextLine();
                        if (!P2sValidation.equals("yes") && !P2sValidation.equals("no")) {
                            System.out.println("Please type only yes or no:");
                        }
                    }

                    if (P2sValidation.equals("yes")) {
                        double wordExtra = Round.extraWordsProfit_coefficient;
                        if (wordExtra >= Casino.budget/8){
                            wordExtra = Casino.budget/8;
                        }
                        Casino.budget = Casino.budget - wordExtra;
                        Player1.budget = Player1.budget + wordExtra;
                        System.out.println(Player1.name + " gained extra " + (float)wordExtra + "$");
                    } else {
                        System.out.println(Player2.name + " did not validate the word '" + guess + "'\n" + Player1.name + " did not gain any extra money for that guess.");
                    }

                    int remainingChances = Round.guessChance - Player1.guessNum;
                    if (0 < remainingChances && remainingChances <= 3) {
                        System.out.println(Player1.name + " has last " + remainingChances + " chances to guess.");
                    }


                }
            }
        }

        if (Player1.guessNum == Round.guessChance && !Player1wins) {

            double P2HWProfit = (Round.HW_profit_percentage / 100.0) * P1Bet;
            double Casinos_profit = P1Bet - P2HWProfit;
            Casino.budget = Casino.budget + Casinos_profit;
            Player1.budget = Player1.budget - P1Bet;
            Player2.budget = Player2.budget + P2HWProfit;
            System.out.println("\n" + Player1.name + " has no more chance to guess, so lost the Round " + round_num + " and the whole bet.\n" +
                    Player2.name + " gained " + (int) Round.HW_profit_percentage + "% of " + Player1.name + "'s bet, which is " + (float) P2HWProfit);
            System.out.println("\nTotal Budget of " + Player1.name + ": " + (float) Player1.budget + "$");
            System.out.println("Total Profit of " + Player2.name + ": " + (float) (Player2.budget - Player2.initialBudget) + "$");
            System.out.println("Total Budget of " + Player2.name + ": " + (float) Player2.budget + "$");
        }

    }

    public static boolean finalRoundGuessCycle(Level Round, Player Casino, Player Player1, Player Player2,
                                       String hiddenWord, ArrayList<String> hiddenWordCopy, double P1Bet, double P2Bet, Scanner scanner, boolean shouldBeSwapped, int swapNum){
        int round_num = 3;
        shouldBeSwapped = false;
        boolean Player1wins = false;

        double B1;
        double B2;
        if (swapNum % 2 == 0) {
            B1 = P1Bet;
            B2 = P2Bet;
        } else {
            B1 = P2Bet;
            B2 = P1Bet;
        }

        System.out.println("Make your guess " + Player1.name + ":");
        String guess = scanner.nextLine();


        if (guess.toLowerCase().equals(hiddenWord)) {

            Player1.guessNum++;

            Player2.budget = Player2.budget - B2;
            Player1.budget = Player1.budget + B2;
            Player1wins = true;
            System.out.println("\n" + Player1.name + " won the Round " + round_num + " !\n" +
                    "Hidden word was: " + hiddenWord + "\n" +
                    Player1.name + " gained " + Player2.name + "'s whole bet, which is " + B2 + "$\n" );

            if (Player1.guessNum < Round.guessChance) {
                int extraGuesses = Round.guessChance - Player1.guessNum;
                double extraGuessesProfit = Round.extraGuessingChanceProfit_coefficient * extraGuesses;
                if (extraGuessesProfit >= Casino.budget/8){
                    extraGuessesProfit = Casino.budget/8;
                }
                Casino.budget = Casino.budget - extraGuessesProfit;
                Player1.budget = Player1.budget + extraGuessesProfit;
                if (extraGuesses == 1) {
                    System.out.println(Player1.name + " also gained " + (float)extraGuessesProfit + "$" +
                            " for the remaining 1 guessing chance.");
                } else {
                    System.out.println(Player1.name + " also gained " + (float)extraGuessesProfit + "$" +
                            " because of the remaining " + extraGuesses + " guessing chances.");
                }

            }

            Player1.guessNum = Round.guessChance;


        } else {

            List<String> Table = Round.Table;

            if(guess.length() > Table.size()){
                System.out.println("Length of your guess cannot be greater than the size of the table.");
            }

            else if (guess.length() < 2 ) {

                if (guess.equals("T")) {

                    boolean noRequestChance = false;
                    if (Player1.tipNum < Round.tipChance && Player1.tipNum < hiddenWord.length() / 2) {
                        Player1.guessNum = Player1.guessNum + 2;
                        Player1.displayedTip = Player1.displayedTip + hiddenWordCopy.get(0);
                        hiddenWordCopy.remove(0);
                        Player1.tipNum++;
                        System.out.println("Tip: " + Player1.displayedTip);

                    } else {
                        System.out.println("You have no chances to request a tip.");
                        noRequestChance = true;
                    }
                    int remainingChances = Round.guessChance - Player1.guessNum;
                    if (0 < remainingChances && remainingChances <= 3 && !noRequestChance) {
                        System.out.println("You have last " + remainingChances + " chances to guess.");
                    }

                }  else {
                    if(hiddenWord.length() == 0){
                        System.out.println("Hidden word cannot be empty space.");
                    } else {
                        System.out.println("Hidden word cannot be a single letter.");
                    }

                }

            } else {

                boolean occurrenceError = false;
                List<String> guessLetterList = Level.copyLettersAsArray(guess.toLowerCase());
                for(int i=0; i < guess.length(); i++){
                    String letter = guessLetterList.get(i);
                    int occurGuess = Level.occurrences(guessLetterList, letter);
                    int occurTable = Level.occurrences(Table, letter);
                    if(occurGuess > occurTable){
                        occurrenceError = true;
                        if(occurTable == 0){
                            System.out.println("There is no '" + letter + "' in the table.");
                        } else {
                            System.out.println("The letter '" + letter + "' occurs only " + occurTable + " times in the table.");
                        }
                        System.out.println("So your guess cannot be " + guess + ".");
                        break;
                    }
                }

                if (!occurrenceError) {

                    Player1.guessNum++;

                    String P2sValidation = "";
                    System.out.println(guess + " is not the hidden word.\nDo you accept " + guess + " as a valid word " + Player2.name + " ?\nType yes or no:");
                    while (!P2sValidation.equals("yes") && !P2sValidation.equals("no")) {
                        P2sValidation = scanner.nextLine();
                        if (!P2sValidation.equals("yes") && !P2sValidation.equals("no")) {
                            System.out.println("Please type only yes or no:");
                        }
                    }

                    if (P2sValidation.equals("yes")) {
                        double wordExtra = Round.extraWordsProfit_coefficient;
                        if (wordExtra >= Casino.budget / 8) {
                            wordExtra = Casino.budget / 8;
                        }
                        Casino.budget = Casino.budget - wordExtra;
                        Player1.budget = Player1.budget + wordExtra;
                        System.out.println(Player1.name + " gained extra " + (float)wordExtra + "$");
                    } else {
                        System.out.println(Player2.name + " did not validate the word '" + guess + "'\n" + Player1.name + " did not gain any extra money for that guess.");
                    }

                    int remainingChances = Round.guessChance - Player1.guessNum;
                    if (0 < remainingChances && remainingChances <= 3) {
                        System.out.println("You have last " + remainingChances + " chances to guess.");
                    }

                    // Swap Players for the next guess cycle:
                    shouldBeSwapped = true;

                }
            }
        }

        if (Player1.guessNum == Round.guessChance && !Player1wins) {

            Player1.budget = Player1.budget - B1;
            Player2.budget = Player2.budget - B2;
            Casino.budget = Casino.budget + B1 + B2;
            System.out.println("\nBoth " + Player1.name + " and " + Player2.name + " lost the Final Round !\n" +
                    "Casino takes the bets of both players.\n");
        }

        return shouldBeSwapped;
    }




    public static ArrayList<Player> createCasinoP1P2(Scanner scanner) {


        System.out.println("Name of the first player:");
        String Name1 = scanner.nextLine();
        double Budget1 = 0;
        while (Budget1 < 15) {
            System.out.println("What is your budget, " + Name1 + " ?");
            Budget1 = scanner.nextDouble();
            scanner.nextLine();
            if (Budget1 < 15) {
                System.out.println("Since the least amount of bet acceptable at each round is 5$,\n" +
                        "you have to have a minimum amount of 15$ budget to start the game.");
            }
        }
        System.out.println("Name of the second player:");
        String Name2 = scanner.nextLine();
        double Budget2 = 0;
        while (Budget2 < 15) {
            System.out.println("What is your budget, " + Name2 + " ?");
            Budget2 = scanner.nextDouble();
            scanner.nextLine();
            if (Budget2 < 15) {
                System.out.println("Since the least amount of bet acceptable at each round is 5$,\n" +
                        "you have to have a minimum amount of 15$ budget to start the game.");
            }
        }


        Player Casino = new Player("Casino", 0, 0);
        Player Player1 = new Player(Name1, Budget1, Budget1);
        Player Player2 = new Player(Name2, Budget2, Budget2);

        ArrayList<Player> Casino_Players = new ArrayList<>();
        Casino_Players.add(Casino);
        Casino_Players.add(Player1);
        Casino_Players.add(Player2);

        return Casino_Players;

    }


    public static void displayRoundIntro(int round_num, Player Player1, Player Player2){
        System.out.println("\n\nRound " + round_num + "\n");

        switch (round_num) {
            case 1:
                System.out.println("In this round " + Player1.name + " will type a word and decide the size of the letter table in which the letters of the word will be hidden.\n" +
                        "Then, " + Player2.name + " will make guesses to find that hidden word by typing words using the letters on the table.\n" +
                        "After table created both players will make their bets.\n" +
                        Player1.name + ", you should be aware that if you type a long word or make the table larger, even though it will be hard to find the word,\n" +
                        "if " + Player2.name + " make the correct guess, you will lose more.\n" +
                        Player2.name + ", type 'T' to request a tip. Each tip costs 2 guessing chances. Independent of the level after you reach the half of the hidden word you cannot request for more tips.\n");
                break;
            case 2:
                System.out.println("In this round " + Player2.name + " will hide a word and " + Player1.name + " will try to find it.");
                break;
            case 3:
                System.out.println("Welcome to the Final Round\n" +
                        "In this round Casino will hide a random word and both of you guess successively to find it.\n" +
                        "The winner will take the bet of the opponent, if both of you lose, you both will lose your bets.");
                break;

        }
    }

    public static String acceptHiddenWord(Scanner scanner, Player Player2, int round_num) {
        String hiddenWord = "0";

        if (round_num == 1 || round_num == 2) {
            while (hiddenWord.length() < 2) {
                System.out.println("Type your hidden word " + Player2.name + ":");
                hiddenWord = scanner.nextLine();
                if (hiddenWord.length() < 2) {
                    System.out.println("Hidden word cannot be a single letter.");
                }
            }
        } else {
            //hiddenWord = Level.genRandomWord();
            hiddenWord = "mehmet";
        }


        return  hiddenWord.toLowerCase();
    }

    public static String acceptTableSize(Scanner scanner, int round_num) {
        String sizeTable = "";
        ArrayList<String> sizeOptions = new ArrayList<>(Arrays.asList("XS","S","M","L","XL"));
        if (round_num == 1 || round_num == 2) {
            while (!sizeOptions.contains(sizeTable)) {
                System.out.println("To declare the size of the letter table, type one of the following options: XS - S - M - L - XL");
                sizeTable = scanner.nextLine();
            }
        } else {
            Random rand = new Random();
            int random = rand.nextInt(2);
            sizeTable = sizeOptions.get(random);
        }
        return sizeTable;
    }

    public static ArrayList<Double> acceptBets(Scanner scanner, Player Player1, Player Player2) {

        double P1Bet = Player1.budget + 1;
        double P2Bet = Player2.budget + 1;

        while (P1Bet >= Player1.budget || P1Bet < 5) {
            System.out.println("Make your bet " + Player1.name + ":");
            P1Bet = scanner.nextDouble();
            scanner.nextLine();
            if (P1Bet >= Player1.budget) {
                if (P1Bet == Player1.budget) {
                    System.out.println("You cannot bet your whole money until the Final Round.");
                } else {
                    System.out.println("Your bet cannot exceed your budget.");
                }
            }

            if (P1Bet < 5) {
                System.out.println("You cannot bet less than 5$");
            }
        }

        while (P2Bet >= Player2.budget || P2Bet < 5) {
            System.out.println("Make your bet " + Player2.name + ":");
            P2Bet = scanner.nextDouble();
            scanner.nextLine();
            if (P2Bet >= Player2.budget) {
                if (P2Bet == Player2.budget) {
                    System.out.println("You cannot bet your whole money until the Final Round.");
                } else {
                    System.out.println("Your bet cannot exceed your budget.");
                }
            }
            if (P2Bet < 5) {
                System.out.println("You cannot bet less than 5$");
            }
        }

        ArrayList<Double> Bets = new ArrayList<>();
        Bets.add(P1Bet);
        Bets.add(P2Bet);

        return Bets;

    }


}






