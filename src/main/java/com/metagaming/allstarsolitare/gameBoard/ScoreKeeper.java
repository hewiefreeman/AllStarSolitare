package com.metagaming.allstarsolitare.gameBoard;

import java.util.ArrayList;
import java.util.List;

public class ScoreKeeper {

    Game game;
    List<String> moveList;

    void init(Game tempGame){
        game = tempGame;
        moveList = new ArrayList<>();
    }


}
