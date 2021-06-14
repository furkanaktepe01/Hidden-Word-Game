package HiddenWordGame;

import java.util.ArrayList;
import java.util.Scanner;

public class HWGame {


    public static void main(String[] args) {

        Level.gameIntro();

        Scanner scanner = new Scanner(System.in);

        ArrayList<Player> Casino_Players = Player.createCasinoP1P2(scanner);
        Player Casino = Casino_Players.get(0);
        Player Player1 = Casino_Players.get(1);
        Player Player2 = Casino_Players.get(2);


        for (int round_num = 1; round_num <= 3; round_num++) {

            Player.displayRoundIntro(round_num, Player1, Player2);

            String hiddenWord = Player.acceptHiddenWord(scanner, Player2, round_num);

            String sizeTable = Player.acceptTableSize(scanner, round_num);

            Level Round = new Level(hiddenWord, sizeTable);
            Round.setLevelHardship(hiddenWord, sizeTable);

            Round.Table = Level.returnTable(hiddenWord, sizeTable, true);

            ArrayList<Double> Bets = Player.acceptBets(scanner, Player1, Player2, round_num);
            double P1Bet = Bets.get(0);
            double P2Bet = Bets.get(1);


            ArrayList<String> hiddenWordCopy = Level.copyLettersAsArray(hiddenWord);
            ArrayList<String> typedWords = new ArrayList<>();

            boolean R1orR2 = round_num == 1 || round_num == 2 ;

            if (R1orR2) {

                // Guess Cycles
                while (Player1.guessNum < Round.guessChance) {

                    Player.guessCycle(Round, Casino, Player1, Player2, hiddenWord, hiddenWordCopy, P1Bet, P2Bet, round_num, scanner, typedWords);

                }

                // Swap Players for the next round:
                Player CopyPlayer1 = new Player(Player1.name, Player1.budget, Player1.initialBudget);
                Player2.guessNum = 0;
                Player2.tipNum = 0;
                Player1 = Player2;
                Player2 = CopyPlayer1;


            } else {

                int swapNum = 0;
                boolean shouldBeSwapped = false;

                while (Player1.guessNum < Round.guessChance) {

                    shouldBeSwapped = Player.finalRoundGuessCycle(Round, Casino, Player1, Player2, hiddenWord,
                            hiddenWordCopy, P1Bet, P2Bet, scanner, shouldBeSwapped, swapNum, typedWords);

                    if(shouldBeSwapped){
                        Player CopyPlayer2 = new Player(Player2.name, Player2.budget, Player2.initialBudget);
                        Player2 = Player1;
                        Player1 = CopyPlayer2;
                        Player1.guessNum = Player2.guessNum;
                        Player1.tipNum = Player2.tipNum;
                        swapNum++;
                    }

                }

                if (swapNum % 2 != 0) {
                    Player CopyPlayer1 = new Player(Player1.name, Player1.budget, Player1.initialBudget);
                    Player1 = Player2;
                    Player2 = CopyPlayer1;
                }

                Level.displayFinalResults(Player1, Player2, Casino);

            }

        }

    }



}
