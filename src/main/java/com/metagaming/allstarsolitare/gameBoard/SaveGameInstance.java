package com.metagaming.allstarsolitare.gameBoard;

import android.os.Bundle;

import java.util.ArrayList;

class SaveGameInstance {

    Bundle saveGame(Bundle bundle, Game game) {
        //
        bundle.putInt("gameType", game.gameType);

        //THE DECK
        bundle.putStringArrayList("deckStack", (ArrayList<String>) game.deck.deckStack);

        //FIELD STACKS
        bundle.putStringArrayList("fieldStack1", (ArrayList<String>) game.deck.fieldStack1);
        bundle.putStringArrayList("fieldStack2", (ArrayList<String>) game.deck.fieldStack2);
        bundle.putStringArrayList("fieldStack3", (ArrayList<String>) game.deck.fieldStack3);
        bundle.putStringArrayList("fieldStack4", (ArrayList<String>) game.deck.fieldStack4);
        bundle.putStringArrayList("fieldStack5", (ArrayList<String>) game.deck.fieldStack5);
        bundle.putStringArrayList("fieldStack6", (ArrayList<String>) game.deck.fieldStack6);
        bundle.putStringArrayList("fieldStack7", (ArrayList<String>) game.deck.fieldStack7);

        if(game.gameInited){
            //
            bundle.putBoolean("fromSetUp", false);

            //YOUR HAND
            bundle.putInt("deckCardOn", game.logicHelper.deckCardOn);
            bundle.putInt("cardsInHandUsed", game.logicHelper.cardsInHandUsed);

            //SUITE STACKS
            bundle.putStringArrayList("clubStack", (ArrayList<String>) game.deck.clubStack);
            bundle.putStringArrayList("spadeStack", (ArrayList<String>) game.deck.spadeStack);
            bundle.putStringArrayList("heartStack", (ArrayList<String>) game.deck.heartStack);
            bundle.putStringArrayList("diamondStack", (ArrayList<String>) game.deck.diamondStack);

            //GAME CARDS
            Object[] cardsKeySet = game.deck.deck.keySet().toArray();
            for(int i = 0; i < cardsKeySet.length; i++){
                String cardOn = (String) cardsKeySet[i];
                bundle.putString("deckCardIsFacingUp" + i, cardOn + ":" + game.deck.getIsFacingUp(cardOn));
            }


        }else{
            //WAS SETTING UP BOARD
            bundle.putBoolean("fromSetUp", true);
            bundle.putInt("setupCardOn", game.setupCardOn);
            bundle.putInt("setupStackOn", game.setupStackOn);
            bundle.putInt("setupStacksCompleted", game.setupStacksCompleted);
            bundle.putInt("setupTopOffset", game.setupTopOffset);
            bundle.putInt("setupRestores", game.setupRestores);
        }

        //TIME AND SCORE
        bundle.putLong("timeElapsed", game.gameBoardTimer.timeElapsed);

        //
        return bundle;
    }
}
