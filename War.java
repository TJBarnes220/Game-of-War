import java.util.Random;

/**
 * This object will be used to represent the card game war
 * Two Players are given equal halves of a deck and must battle
 * to see who keeps the cards used. This is determined by their
 * values. If there is a tie a war begins and more cards are 
 * wagered for the next battle. Currently the games can range from
 * 200 rounds to thousands. In some cases there may be no victor 
 * and the game runs infinitely. This seems to be due to the design
 * of the game itself as the version I've programed doesn't
 * reshuffle the player decks.
 */
public class War{
    public static void main(String[] args){
        int round = 1;
        int[] player1Stack = new int[52];
        int[] player2Stack = new int[52];
        for(int i = 0; i < 52; i++){
            player1Stack[i] = -1;
            player2Stack[i] = -1;
        }
        dealCards(player1Stack, player2Stack);
        System.out.print("Player 1: ");
        printStack(player1Stack);
        System.out.println();

        System.out.print("Player 2: ");
        printStack(player2Stack);

        boolean loop = true;
        while(loop){
            System.out.println();
            System.out.println("Round " + round + ":");
            //shuffle cards every 500 rounds???
            battle(player1Stack, player2Stack, null);
            
            System.out.print("Player 1: ");
            printStack(player1Stack);
            System.out.println();

            System.out.print("Player 2: ");
            printStack(player2Stack);
            System.out.println();

            if(isStackEmpty(player1Stack)){
                loop = false;
                System.out.print("Player 2 has won the game!");
            }
            else if(isStackEmpty(player2Stack)){
                loop = false;
                System.out.print("Player 1 has won the game!");
            }
            round++;
        }
    }
    
    /**
     * Here is where the game takes place. Each pile has a card
     * drawn from the top. These cards are compared with one another and a 
     * winner is decided. In the event of a tie a war will begin. We save the 
     * cards gathered and pass them onto the war method. All the cards used get
     * placed at the bottom of the winner's pile.
     * @param stack1 first of the two player card piles 
     * @param stack2 second of the two player card piles
     * @param extra extra cards to give the winner of this battle
     */
    public static void battle(int[]stack1, int[] stack2, int[] extra){
        int[] winnerPile = new int[2];

        //handles player 1 interaction
        //draws card and displays it
        //adds it to winner pile
        int card1 = dealFromStack(stack1);
        winnerPile[0] = card1;
        System.out.print("Player 1 drew: ");
        printCard(card1);

        System.out.println();

        //handles player 1 interaction
        //draws card and displays it
        //adds it to winner pile
        int card2 = dealFromStack(stack2);
        winnerPile[1] = card2;
        System.out.print("Player 2 drew: ");
        printCard(card2);
        System.out.println();

        //if there are cards that were carried over combine them
        if(extra != null){
            winnerPile = combine(winnerPile, extra);
        }

        //make a comparision method and winner will get the stack
        int verdict = compareCards(card1, card2);
        if(verdict == 0){
            addToStack(stack1, winnerPile);
            System.out.println("Player 1 won this round.");
        }
        else if(verdict == 1){
            addToStack(stack2, winnerPile);
            System.out.println("Player 2 won this round.");
        }
        else if(verdict == 2){
            System.out.println("Now there is a war!");
            war(stack1, stack2, winnerPile);
            
        }
    }

    /**
     * After a tie the players begin a war. Each player must place down three
     * cards and battle once again. If any player runs out of cards before 
     * the war is over they automatically lose 
     * @param stack1 player 1 stack of cards 
     * @param stack2 player 2 stack of cards
     * @param extra array of cards gathered from previous battles and wars
     */
    public static void war(int[] stack1, int[] stack2, int[] extra){
        int[] cards = new int[6];
        if(isStackEmpty(stack1)){
            System.out.println("Player 1 has run out of cards.");
            return;
        }
        cards[0] = dealFromStack(stack1);
        if(isStackEmpty(stack1)){
            System.out.println("Player 1 has run out of cards.");
            return;
        }
        cards[1] = dealFromStack(stack1);
        if(isStackEmpty(stack1)){
            System.out.println("Player 1 has run out of cards.");
            return;
        }
        cards[2] = dealFromStack(stack1);
        if(isStackEmpty(stack1)){
            System.out.println("Player 1 has run out of cards.");
            return;
        }



        if(isStackEmpty(stack2)){
            System.out.println("Player 2 has run out of cards.");
            return;
        }
        cards[3] = dealFromStack(stack2);
        if(isStackEmpty(stack2)){
            System.out.println("Player 2 has run out of cards.");
            return;
        }
        cards[4] = dealFromStack(stack2);
        if(isStackEmpty(stack2)){
            System.out.println("Player 2 has run out of cards.");
            return;
        }
        cards[5] = dealFromStack(stack2);
        if(isStackEmpty(stack2)){
            System.out.println("Player 2 has run out of cards.");
            return;
        }
        
        if(extra != null){
            cards = combine(extra, cards);
        }

        System.out.println("Cards have been stacked now we battle again.");
        battle(stack1, stack2, cards);
    }

    /**
     * We use this method to combine stacks of cards into a bigger one
     * @param pile1 first pile to combine 
     * @param pile2 second pule to combine
     * @return an array with the combined contents of the given arrays
     */
    public static int[] combine(int[] pile1, int[] pile2){
        //Make variables to hold length of given arrays and new combined one
        int length1 = pile1.length;
        int length2 = pile2.length;
        int bigLength = length1 + length2;
        //creates new array
        int[] result = new int[bigLength];
        //Makes an index to mark the placement in big array
        int index = 0;
        //moves through both arrays and places the data inside big array
        for(int i = 0; i < length1; i++){
            result[index] = pile1[i];
            index++;
        }

        for(int i = 0; i < length2; i++){
            result[index] = pile2[i];
            index++;
        }
        
        return result;
    }
   
    /**
     * Checks to see if the given array of integers has any cards left 
     * @param stack the array to examine
     * @return true/false depending if there are no cards
     */
    public static boolean isStackEmpty(int[] stack){
        int stackLength = stack.length;
        for(int i = 0; i < stackLength; i++){
            if(stack[i] != -1){
                return false;
            }
        }
        return true;
    }
    /**
     * We decide which card holds a greater value by checking their modulo
     * values. We will call this method in battles and the winner is chosen
     * through the return value
     * @param card1 the first of two cards to compare
     * @param card2 the second of two cards to compare
     * @return there are multiple ways this interaction can end
     * 0 - means card 1 is greater
     * 1 - means card 2 is greater
     * 2 - tie
     */
    public static int compareCards(int card1, int card2){
        //Reduces card value to card strength 
        //aces are typically represented by 0 so we flip them
        //to 13 to make direct comparison easier
        int value1 = card1 % 13;
        if(value1 == 0){
            value1 = 13;
        }
        int value2 = card2 % 13;
        if(value2 == 0){
            value2 = 13;
        }

        if(value1 > value2){
            return 0;
        }
        else if(value2 > value1){
            return 1;
        }
        return 2;
    }

    /** 
     * Players are given equal amounts of cards from a standard 52 card deck 
     * The cards will be shuffled will be split amounst the players 
     * @param stack1 array that will hold the cards given to one player 
     * @param stack2 array that will hold the cards given to the other player
    */
    public static void dealCards(int[] stack1, int[] stack2){
        int[] deck = new int[52];
        int deck_length = deck.length;
        //filling in the deck with values 0-51
        for(int i = 0; i < deck_length; i++){
            deck[i] = i;
        }

        //Shuffles cards by taking current index and swapping it with a random position
        Random rand = new Random();
        for(int j = 0; j < deck_length; j++){
            int position = rand.nextInt(deck_length); //random position 0-51
            //swapping process
            int temp = deck[j];
            deck[j] = deck[position];
            deck[position] = temp;
        }

        //move through deck another time and place cards in the piles
        int hand_position = 0;
        for(int i = 0; i < deck_length; i++){
            //Back and forth place cards in each pile
            if(i % 2 == 0){
                stack1[hand_position] = deck[i];
            }
            else if(i % 2 == 1){
                stack2[hand_position] = deck[i];
                hand_position++;
            }
        }
    }

    /**
     * This method will be our way of displaying all the cards in a player's pile
     * Cards will be shown in their card values rather than their number representations
     * Example: [2][4][K][A][10].......
     * @param stack the array of cards to display
     */
    public static void printStack(int[] stack){
        int loopLength = stack.length;
        for(int i = 0; i < loopLength; i++){
            if(stack[i] != -1){
                System.out.print("[");
                int value = stack[i] % 13;
                switch(value){
                    case 0:
                        System.out.print("A");
                        break;
                    case 1:
                        System.out.print("2");
                        break;
                    case 2:
                        System.out.print("3");
                        break;
                    case 3:
                        System.out.print("4");
                        break;
                    case 4:
                        System.out.print("5");
                        break;
                    case 5:
                        System.out.print("6");
                        break;
                    case 6:
                        System.out.print("7");
                        break;
                    case 7:
                        System.out.print("8");
                        break;
                    case 8:
                        System.out.print("9");
                        break;
                    case 9:
                        System.out.print("10");
                        break;
                    case 10:
                        System.out.print("J");
                        break;
                    case 11:
                        System.out.print("Q");
                        break;
                    case 12:
                        System.out.print("K");
                        break;
                }
                System.out.print("]");
            }
            else{
                break;
            }
        }
    }

    /**
     * We print out both the card value and suit 
     * Since we are using integers we will get whole numbers 
     * after a division. This allows us to break up our deck into
     * four groups.
     * 0-12 Hearts
     * 13-25 Diamonds
     * 26-38 Clubs
     * 39-51 Spades
     * 
     * The modulo expression will get us the card value as
     * number % 13 will always get us a number between 0 and 13
     * which we can use as a way to determine their value.
     * 
     * @param card number representation of a card to print out
     */
    public static void printCard(int card){
        String suit = "";
        int suitVal = card / 13;
        switch(suitVal){
            case 0:
                suit = "Hearts";
                break;
            case 1:
                suit = "Diamonds";
                break;
            case 2:
                suit = "Clubs";
                break;
            case 3:
                suit = "Spades";
                break;
        }

        String strength = "";
        int value = card % 13;
        switch(value){
            case 0:
                strength = "Ace";
                break;
            case 1:
                strength = "2";
                break;
            case 2:
                strength = "3";
                break;
            case 3:
                strength = "4";
                break;
            case 4:
                strength = "5";
                break;
            case 5:
                strength = "6";
                break;
            case 6:
                strength = "7";
                break;
            case 7:
                strength = "8";
                break;
            case 8:
                strength = "9";
                break;
            case 9:
                strength = "10";
                break;
            case 10:
                strength = "Jack";
                break;
            case 11:
                strength = "Queen";
                break;
            case 12:
                strength = "King";
                break;
        }
        System.out.print(strength + " of " + suit);
    }

    /**
     * We need to be able to place cards at the bottom of the player's pile.
     * Here we will take an array of cards and append them to the given player
     * pile. 
     * @param stack array representing the player's stack of cards
     * @param cards array of cards to append to the stack 
     */
    public static void addToStack(int[] stack, int[] cards){
        //first we must find the bottom of the pile which is represented
        //by the first -1 found in the array of cards
        int stackLength = stack.length;
        int startIndex = -1;
        for(int i = 0; i < stackLength; i++){
            if(stack[i] == -1){
                startIndex = i;
                break;
            }
        }

        //once having that index we start there and append the cards
        //in the given cards array 
        int cardLength = cards.length;
        for(int i = 0; i < cardLength; i++){
            //in case we somehow have more cards than allow we need to escape;
            if(startIndex >= stackLength){
                break;
            }
            stack[startIndex] = cards[i];
            startIndex++;
        }
    }

    /**
     * We need the ability to draw a card from the top of a player pile.
     * Here we hold the first value of the given stack and then
     * we shift every value to the left effectively erasing the
     * drawn card from the stack
     * @param stack array of cards that we will take from
     * @return we will return the card drawn from the pile
     */
    public static int dealFromStack(int[] stack){
        //we always draw from the top of the deck
        int card = stack[0];
        int stackLength = stack.length;
        for(int i = 1; i < stackLength; i++){
            stack[i - 1] = stack[i];
            //We stop once we've reached the bottom of the pile 
            //this is always denoted by -1
            if(stack[i] == -1){
                break;
            }
        }
        return card;
    }
}