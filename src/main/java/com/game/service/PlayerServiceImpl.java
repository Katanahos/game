package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;


    @Override
    public List<Player> readPlayers(PlayersDTO playersDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> playerCriteria = cb.createQuery(Player.class);
        Root<Player> rootPlayer = playerCriteria.from(Player.class);
        playerCriteria.select(rootPlayer).distinct(true);
        Predicate criteria = cb.conjunction();

        if(playersDTO.getName().isPresent() && isValidName(playersDTO.getName().get())){
            Predicate p = cb.like(rootPlayer.get("name"), "%" + playersDTO.getName().get() + "%");
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getTitle().isPresent() && isValidTitle(playersDTO.getTitle().get())){
            Predicate p = cb.like(rootPlayer.get("title"), "%" + playersDTO.getTitle().get() + "%");
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getRace().isPresent()){
            Predicate p = cb.equal(rootPlayer.get("race"), playersDTO.getRace().get());
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getProfession().isPresent()) {
            Predicate p = cb.equal(rootPlayer.get("profession"), playersDTO.getProfession().get());
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getAfter().isPresent() && isValidBirthday(new Date(playersDTO.getAfter().get()))
                && playersDTO.getBefore().isPresent() && isValidBirthday(new Date(playersDTO.getBefore().get()))){
            Predicate p = cb.between(rootPlayer.get("birthday"), new Date(playersDTO.getAfter().get()), new Date(playersDTO.getBefore().get()));
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getAfter().isPresent() && isValidBirthday(new Date(playersDTO.getAfter().get()))){
            Predicate p = cb.greaterThanOrEqualTo(rootPlayer.get("birthday"), new Date(playersDTO.getAfter().get()));
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getBefore().isPresent() && isValidBirthday(new Date(playersDTO.getBefore().get()))){
            Predicate p = cb.lessThanOrEqualTo(rootPlayer.get("birthday"), new Date(playersDTO.getBefore().get()));
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getBanned().isPresent()){
            Predicate p = cb.equal(rootPlayer.get("banned"), playersDTO.getBanned().get() ? 1 : 0);
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getMinExperience().isPresent()&& isValidExperience(playersDTO.getMinExperience().get())
                && playersDTO.getMaxExperience().isPresent() && isValidExperience(playersDTO.getMaxExperience().get())){
            Predicate p = cb.between(rootPlayer.get("experience"), playersDTO.getMinExperience().get(), playersDTO.getMaxExperience().get());
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getMinExperience().isPresent() && isValidExperience(playersDTO.getMinExperience().get())){
            Predicate p = cb.ge(rootPlayer.get("experience"), playersDTO.getMinExperience().get());
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getMaxExperience().isPresent() && isValidExperience(playersDTO.getMaxExperience().get())){
            Predicate p = cb.le(rootPlayer.get("experience"), playersDTO.getMaxExperience().get());
            criteria = cb.and(criteria, p);
        }

        if(playersDTO.getMinLevel().isPresent() && playersDTO.getMaxLevel().isPresent()){
            Predicate p = cb.between(rootPlayer.get("level"), playersDTO.getMinLevel().get(), playersDTO.getMaxLevel().get());
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getMinLevel().isPresent()){
            Predicate p = cb.ge(rootPlayer.get("level"), playersDTO.getMinLevel().get());
            criteria = cb.and(criteria, p);
        }else if(playersDTO.getMaxLevel().isPresent() ){
            Predicate p = cb.le(rootPlayer.get("level"), playersDTO.getMaxLevel().get());
            criteria = cb.and(criteria, p);
        }

        playerCriteria.where(criteria);

        if(playersDTO.getPlayerOrder().isPresent() && isValidOrder(playersDTO.getPlayerOrder().get())){
            playerCriteria.orderBy(cb.asc(rootPlayer.get(PlayerOrder.valueOf(playersDTO.getPlayerOrder().get()).getFieldName())));
        }

        if(playersDTO.getPageSize().isPresent()&& playersDTO.getPageNumber().isPresent()&&playersDTO.getPageNumber().get() == 0){
            return entityManager.createQuery(playerCriteria).setFirstResult(playersDTO.getPageNumber().get()).
                    setMaxResults(playersDTO.getPageSize().get()).getResultList();
        }else if(playersDTO.getPageSize().isPresent() && playersDTO.getPageNumber().isPresent()){
            return entityManager.createQuery(playerCriteria).setFirstResult(playersDTO.getPageNumber().get() *
                    playersDTO.getPageSize().get()).setMaxResults(playersDTO.getPageSize().get()).getResultList();
        }else return entityManager.createQuery(playerCriteria).getResultList();
    }


    @Override
    public Player createPlayer(Player player) {
        if(isValidPlayer(player)){
            if(player.getBanned()==null)
                player.setBanned(false);
            calculate(player, player.getExperience());
            return playerRepository.saveAndFlush(player);
        }
        return null;
    }
    @Override
    public Player updatePlayer(long id, Player reqPlayer) {
        Optional<Player> player = playerRepository.findById(id);
        Player oldPlayer = player.get();

            if (reqPlayer.getName() != null) {
                if (!isValidName(reqPlayer.getName()))
                    return null;
                oldPlayer.setName(reqPlayer.getName());
            }

            if (reqPlayer.getTitle() != null) {
                if (!isValidTitle(reqPlayer.getTitle()))
                    return null;
                oldPlayer.setTitle(reqPlayer.getTitle());
            }

            if (reqPlayer.getRace() != null) {
                if (!isValidRace(reqPlayer.getRace()))
                    return null;
                oldPlayer.setRace(reqPlayer.getRace());
            }

            if (reqPlayer.getProfession() != null) {
                if (!isValidProfession(reqPlayer.getProfession()))
                    return null;
                oldPlayer.setProfession(reqPlayer.getProfession());
            }

            if (reqPlayer.getBirthday() != null) {
                if (!isValidBirthday(reqPlayer.getBirthday()))
                    return null;
                oldPlayer.setBirthday(reqPlayer.getBirthday());
            }

            if (reqPlayer.getBanned() != null) {
                oldPlayer.setBanned(reqPlayer.getBanned());
            }

            if (reqPlayer.getExperience() != null) {
                if (!isValidExperience(reqPlayer.getExperience()))
                    return null;
                oldPlayer.setExperience(reqPlayer.getExperience());
                calculate(oldPlayer, reqPlayer.getExperience());
            }

            return playerRepository.saveAndFlush(oldPlayer);

    }

    @Override
    public boolean deletePlayer(long id) {
        if(playerRepository.existsById(id))
            {playerRepository.deleteById(id);
            return true;}
        else return false;
    }

    @Override
    public Player readPlayer(long id) {
        if(playerRepository.existsById(id)){
            return playerRepository.findById(id).get();
        }
        return null;
    }

    public boolean isValidOrder(String order){
        return !order.equals("");
    }

    public boolean isValidPlayer(Player player){
        return isValidName(player.getName())&&isValidTitle(player.getTitle())&&isValidExperience(player.getExperience())
                &&isValidBirthday(player.getBirthday());
    }

    public boolean isValidName(String name){
        return name!= null&&!name.equals("")&&name.length()<=12;
    }

    public boolean isValidTitle(String title){
        return title!=null&&!title.equals("")&&title.length()<=30;
    }

    public boolean isValidExperience(int exp){
        return exp>0&&exp<10000000;
    }

    public boolean isValidBirthday(Date date){
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            return year>=2000&&year<=3000;
    }

    public void calculate(Player player,int exp){
        player.setLevel((int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100));
        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - exp);
    }

    public boolean isValidRace(Race race){
        boolean b = false;
        for (Race r : Race.values()){
            if(r == race){
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean isValidProfession(Profession profession){
        boolean b = false;
        for (Profession p : Profession.values()){
            if(p == profession){
                b = true;
                break;
            }
        }
        return b;
    }

}
