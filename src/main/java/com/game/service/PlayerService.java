package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    List<Player> readPlayers(PlayersDTO playersDTO);
    Player createPlayer(Player player);
    Player updatePlayer(long id,Player player);
    boolean deletePlayer(long id);
    Player readPlayer(long id);
    boolean isValidPlayer(Player player);

}
