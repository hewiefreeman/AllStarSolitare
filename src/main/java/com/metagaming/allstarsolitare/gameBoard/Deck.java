package com.metagaming.allstarsolitare.gameBoard;

import android.annotation.SuppressLint;

import com.metagaming.allstarsolitare.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

class Deck {
    //int [] index;
    HashMap<String, Object[]> deck;
    private HashMap<Integer, String> cardIdToName;

    //CARDS GAME ORDER
    List<String> cardsGameOrder;

    //DECK STACK
    List<String> deckStack;

    //field stacks
    List<String> fieldStack1;
    List<String> fieldStack2;
    List<String> fieldStack3;
    List<String> fieldStack4;
    List<String> fieldStack5;
    List<String> fieldStack6;
    List<String> fieldStack7;

    //ace stacks
    List<String> clubStack;
    List<String> spadeStack;
    List<String> heartStack;
    List<String> diamondStack;

    @SuppressLint("UseSparseArrays")
    void whipOutTheDeck(){
        fieldStack1 = new ArrayList<>();
        fieldStack2 = new ArrayList<>();
        fieldStack3 = new ArrayList<>();
        fieldStack4 = new ArrayList<>();
        fieldStack5 = new ArrayList<>();
        fieldStack6 = new ArrayList<>();
        fieldStack7 = new ArrayList<>();

        deckStack = new ArrayList<>();
        clubStack = new ArrayList<>();
        spadeStack = new ArrayList<>();
        heartStack = new ArrayList<>();
        diamondStack = new ArrayList<>();

        deck = new HashMap<>();
        //DATA = cardName : [ drawableId, isFacingUp, color, cardId, cardImageId ]
        deck.put("2_c", new Object[]{R.drawable.card_2_c, false, "black", null, null});
        deck.put("2_s", new Object[]{R.drawable.card_2_s, false, "black", null, null});
        deck.put("3_c", new Object[]{R.drawable.card_3_c, false, "black", null, null});
        deck.put("3_s", new Object[]{R.drawable.card_3_s, false, "black", null, null});
        deck.put("4_c", new Object[]{R.drawable.card_4_c, false, "black", null, null});
        deck.put("4_s", new Object[]{R.drawable.card_4_s, false, "black", null, null});
        deck.put("5_c", new Object[]{R.drawable.card_5_c, false, "black", null, null});
        deck.put("5_s", new Object[]{R.drawable.card_5_s, false, "black", null, null});
        deck.put("6_c", new Object[]{R.drawable.card_6_c, false, "black", null, null});
        deck.put("6_s", new Object[]{R.drawable.card_6_s, false, "black", null, null});
        deck.put("7_c", new Object[]{R.drawable.card_7_c, false, "black", null, null});
        deck.put("7_s", new Object[]{R.drawable.card_7_s, false, "black", null, null});
        deck.put("8_c", new Object[]{R.drawable.card_8_c, false, "black", null, null});
        deck.put("8_s", new Object[]{R.drawable.card_8_s, false, "black", null, null});
        deck.put("9_c", new Object[]{R.drawable.card_9_c, false, "black", null, null});
        deck.put("9_s", new Object[]{R.drawable.card_9_s, false, "black", null, null});
        deck.put("10_c", new Object[]{R.drawable.card_10_c, false, "black", null, null});
        deck.put("10_s", new Object[]{R.drawable.card_10_s, false, "black", null, null});
        deck.put("j_c", new Object[]{R.drawable.j_c, false, "black", null, null});
        deck.put("j_s", new Object[]{R.drawable.j_s, false, "black", null, null});
        deck.put("k_c", new Object[]{R.drawable.k_c, false, "black", null, null});
        deck.put("k_s", new Object[]{R.drawable.k_s, false, "black", null, null});
        deck.put("q_c", new Object[]{R.drawable.q_c, false, "black", null, null});
        deck.put("q_s", new Object[]{R.drawable.q_s, false, "black", null, null});
        deck.put("a_c", new Object[]{R.drawable.a_c, false, "black", null, null});
        deck.put("a_s", new Object[]{R.drawable.a_s, false, "black", null, null});
        deck.put("2_h", new Object[]{R.drawable.card_2_h, false, "red", null, null});
        deck.put("2_d", new Object[]{R.drawable.card_2_d, false, "red", null, null});
        deck.put("3_h", new Object[]{R.drawable.card_3_h, false, "red", null, null});
        deck.put("3_d", new Object[]{R.drawable.card_3_d, false, "red", null, null});
        deck.put("4_h", new Object[]{R.drawable.card_4_h, false, "red", null, null});
        deck.put("4_d", new Object[]{R.drawable.card_4_d, false, "red", null, null});
        deck.put("5_h", new Object[]{R.drawable.card_5_h, false, "red", null, null});
        deck.put("5_d", new Object[]{R.drawable.card_5_d, false, "red", null, null});
        deck.put("6_h", new Object[]{R.drawable.card_6_h, false, "red", null, null});
        deck.put("6_d", new Object[]{R.drawable.card_6_d, false, "red", null, null});
        deck.put("7_h", new Object[]{R.drawable.card_7_h, false, "red", null, null});
        deck.put("7_d", new Object[]{R.drawable.card_7_d, false, "red", null, null});
        deck.put("8_h", new Object[]{R.drawable.card_8_h, false, "red", null, null});
        deck.put("8_d", new Object[]{R.drawable.card_8_d, false, "red", null, null});
        deck.put("9_h", new Object[]{R.drawable.card_9_h, false, "red", null, null});
        deck.put("9_d", new Object[]{R.drawable.card_9_d, false, "red", null, null});
        deck.put("10_h", new Object[]{R.drawable.card_10_h, false, "red", null, null});
        deck.put("10_d", new Object[]{R.drawable.card_10_d, false, "red", null, null});
        deck.put("j_h", new Object[]{R.drawable.j_h, false, "red", null, null});
        deck.put("j_d", new Object[]{R.drawable.j_d, false, "red", null, null});
        deck.put("k_h", new Object[]{R.drawable.k_h, false, "red", null, null});
        deck.put("k_d", new Object[]{R.drawable.k_d, false, "red", null, null});
        deck.put("q_h", new Object[]{R.drawable.q_h, false, "red", null, null});
        deck.put("q_d", new Object[]{R.drawable.q_d, false, "red", null, null});
        deck.put("a_h", new Object[]{R.drawable.a_h, false, "red", null, null});
        deck.put("a_d", new Object[]{R.drawable.a_d, false, "red", null, null});

        //
        cardsGameOrder = new ArrayList<>();
        cardsGameOrder.add("a");
        cardsGameOrder.add("2");
        cardsGameOrder.add("3");
        cardsGameOrder.add("4");
        cardsGameOrder.add("5");
        cardsGameOrder.add("6");
        cardsGameOrder.add("7");
        cardsGameOrder.add("8");
        cardsGameOrder.add("9");
        cardsGameOrder.add("10");
        cardsGameOrder.add("j");
        cardsGameOrder.add("q");
        cardsGameOrder.add("k");

        //
        cardIdToName = new HashMap<>();
    }

    void shuffleTheDeck(){
        List<String> unshuffled = new ArrayList<>();
        deckStack = new ArrayList<>();

        //PARSE UNSHUFFLED DECK
        for(int i = 0; i < deck.keySet().size(); i++){
            unshuffled.add(deck.keySet().toArray()[i]+"");
        }

        //SHUFFLE ONCE
        for(int i = 0; i < deck.keySet().size(); i++){
            //grab random card
            int randomCard = new Random().nextInt((deck.keySet().size()-i));
            deckStack.add(unshuffled.get(randomCard));
            unshuffled.remove(randomCard);
        }

        unshuffled = new ArrayList<>();

        //SHUFFLE TWICE
        for(int i = 0; i < deck.keySet().size(); i++){
            //grab random card
            int randomCard = new Random().nextInt((deck.keySet().size()-i));
            unshuffled.add(deckStack.get(randomCard));
            deckStack.remove(randomCard);
        }

        deckStack = new ArrayList<>();

        //SHUFFLE ONE LAST TIME
        for(int i = 0; i < deck.keySet().size(); i++){
            //grab random card
            int randomCard = new Random().nextInt((deck.keySet().size()-i));
            deckStack.add(unshuffled.get(randomCard));
            unshuffled.remove(randomCard);
        }

        // Yah, cause it seemed like the deck was never shuffled well
        // until I shuffled it a few times over.
    }

    void setId(List<Integer> ids, String cardName){
        Object[] cardParams = deck.get(cardName);
        cardParams[3] = ids.get(0);
        cardParams[4] = ids.get(1);
        deck.put(cardName, cardParams);
        cardIdToName.put(ids.get(0), cardName);
    }

    void setCardFacingUp(String cardName, Boolean isUp){
        Object[] cardParams = deck.get(cardName);
        cardParams[1] = isUp;
        deck.put(cardName, cardParams);
    }

    int getCardId(String cardName){
        return (int)deck.get(cardName)[3];
    }

    int getImageId(String cardName){
        return (int)deck.get(cardName)[4];
    }

    int getResourceId(String cardName){
        return (int)deck.get(cardName)[0];
    }

    boolean getIsFacingUp(String cardName){
        return (boolean) deck.get(cardName)[1];
    }

    String getCardColor(String cardName){
        return (String) deck.get(cardName)[2];
    }

    String getNameFromId(int cardId){
        return cardIdToName.get(cardId);
    }

    int getCardStackIndex(String cardName, List<String> inStack){
        for(int i = 0; i < inStack.size(); i++){
            if(inStack.get(i).equals(cardName)){
                return i;
            }
        }

        return 0;
    }

    List<Integer> getNumberOfFacingUpAndDownCardsInFieldStack(List<String> stack){
        List<Integer> upAndDown = new ArrayList<>();
        int facingUp = 0;
        int facingDown;
        for(int i = 0; i < stack.size(); i++){
            if(getIsFacingUp(stack.get(i))){
                facingUp++;
            }
        }
        facingDown = stack.size()-facingUp;

        //
        upAndDown.add(facingUp);
        upAndDown.add(facingDown);

        //
        return upAndDown;
    }

    String getCardStackLocation(String cardName){
        for(int i = 0; i < fieldStack1.size(); i++){
            if(fieldStack1.get(i).equals(cardName)){
                return "1";
            }
        }
        for(int i = 0; i < fieldStack2.size(); i++){
            if(fieldStack2.get(i).equals(cardName)){
                return "2";
            }
        }
        for(int i = 0; i < fieldStack3.size(); i++){
            if(fieldStack3.get(i).equals(cardName)){
                return "3";
            }
        }
        for(int i = 0; i < fieldStack4.size(); i++){
            if(fieldStack4.get(i).equals(cardName)){
                return "4";
            }
        }
        for(int i = 0; i < fieldStack5.size(); i++){
            if(fieldStack5.get(i).equals(cardName)){
                return "5";
            }
        }
        for(int i = 0; i < fieldStack6.size(); i++){
            if(fieldStack6.get(i).equals(cardName)){
                return "6";
            }
        }
        for(int i = 0; i < fieldStack7.size(); i++){
            if(fieldStack7.get(i).equals(cardName)){
                return "7";
            }
        }
        for(int i = 0; i < clubStack.size(); i++){
            if(clubStack.get(i).equals(cardName)){
                return "club";
            }
        }
        for(int i = 0; i < heartStack.size(); i++){
            if(heartStack.get(i).equals(cardName)){
                return "heart";
            }
        }
        for(int i = 0; i < spadeStack.size(); i++){
            if(spadeStack.get(i).equals(cardName)){
                return "spade";
            }
        }
        for(int i = 0; i < diamondStack.size(); i++){
            if(diamondStack.get(i).equals(cardName)){
                return "diamond";
            }
        }
        for(int i = 0; i < deckStack.size(); i++){
            if(deckStack.get(i).equals(cardName)){
                return "deck";
            }
        }

        //
        return "";
    }
}
