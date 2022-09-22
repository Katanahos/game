package com.game.controller;

import com.game.entity.*;

import com.game.service.PlayerService;
import com.game.service.PlayersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {


    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Player player){
        if(!playerService.isValidPlayer(player)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Player newPlayer = playerService.createPlayer(player);
        return newPlayer != null
                ? new ResponseEntity<>(newPlayer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Player> read(@PathVariable(name = "id") long id){
        if(id<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Player player = playerService.readPlayer(id);
        return player != null? new ResponseEntity<>(player,HttpStatus.OK):
                               new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<Player>> readPlayers(@RequestParam(name = "name") Optional<String> name,
                                                    @RequestParam(name = "title") Optional<String> title,
                                                    @RequestParam(name = "race") Optional<Race> race,
                                                    @RequestParam(name = "profession") Optional<Profession> profession,
                                                    @RequestParam(name = "after") Optional<Long> after,
                                                    @RequestParam(name = "before") Optional<Long> before,
                                                    @RequestParam(name = "banned") Optional<Boolean> banned,
                                                    @RequestParam(name = "minExperience") Optional<Integer> minExperience,
                                                    @RequestParam(name = "maxExperience") Optional<Integer> maxExperience,
                                                    @RequestParam(name = "minLevel") Optional<Integer> minLevel,
                                                    @RequestParam(name = "maxLevel") Optional<Integer> maxLevel,
                                                    @RequestParam(name = "order", defaultValue = "ID") Optional<String> order,
                                                    @RequestParam(name = "pageNumber", defaultValue = "0") Optional<Integer> pageNumber,
                                                    @RequestParam(name = "pageSize", defaultValue = "3") Optional<Integer> pageSize){

        PlayersDTO playersDTO = new PlayersDTO(name, title, race, profession , after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, order, pageNumber, pageSize);


        final List<Player> players = playerService.readPlayers(playersDTO);

        return players !=null?
                new ResponseEntity<>(players, HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Integer> count(@RequestParam(name = "name") Optional<String> name,
                                         @RequestParam(name = "title") Optional<String> title,
                                         @RequestParam(name = "race") Optional<Race> race,
                                         @RequestParam(name = "profession") Optional<Profession> profession,
                                         @RequestParam(name = "after") Optional<Long> after,
                                         @RequestParam(name = "before") Optional<Long> before,
                                         @RequestParam(name = "banned") Optional<Boolean> banned,
                                         @RequestParam(name = "minExperience") Optional<Integer> minExperience,
                                         @RequestParam(name = "maxExperience") Optional<Integer> maxExperience,
                                         @RequestParam(name = "minLevel") Optional<Integer> minLevel,
                                         @RequestParam(name = "maxLevel") Optional<Integer> maxLevel)
    {
        Optional<String> order = Optional.of((""));
        Optional<Integer> pageNumber = Optional.of(0);
        Optional<Integer> pageSize = Optional.of(Integer.MAX_VALUE);

        PlayersDTO playersDTO = new PlayersDTO(name, title, race, profession, after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel,order,pageNumber,pageSize);

        {

            final List<Player> players = playerService.readPlayers(playersDTO);

            return new ResponseEntity<>(players.size(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Player> update(@Validated @PathVariable(name = "id") long id,
                                         @RequestBody Player player){
        if(id<=0){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(playerService.readPlayer(id)==null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Player newPlayer = playerService.updatePlayer(id,player);

        return newPlayer!=null? new ResponseEntity<>(newPlayer,HttpStatus.OK):
                                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id ){
        if(id<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final boolean deleted = playerService.deletePlayer(id);

        return deleted?
                new ResponseEntity<>(HttpStatus.OK):
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
