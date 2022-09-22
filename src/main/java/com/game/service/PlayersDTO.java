package com.game.service;


import com.game.controller.PlayerOrder;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.Optional;

public class PlayersDTO {
    private Optional<String> name;
    private Optional<String> title;
    private Optional<Race> race;
    private Optional<Profession> profession;
    private Optional<Long> after;
    private Optional<Long> before;
    private Optional<Boolean> banned;
    private Optional<Integer> minExperience;
    private Optional<Integer> maxExperience;
    private Optional<Integer> minLevel;
    private Optional<Integer> maxLevel;
    private Optional<String> playerOrder;
    private Optional<Integer> pageNumber;
    private Optional<Integer> pageSize;

    public PlayersDTO(Optional<String> name, Optional<String> title, Optional<Race> race, Optional<Profession> profession,
                      Optional<Long> after, Optional<Long> before, Optional<Boolean> banned, Optional<Integer> minExperience,
                      Optional<Integer> maxExperience, Optional<Integer> minLevel, Optional<Integer> maxLevel,
                      Optional<String> playerOrder, Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.after = after;
        this.before = before;
        this.banned = banned;
        this.minExperience = minExperience;
        this.maxExperience = maxExperience;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.playerOrder = playerOrder;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public void setTitle(Optional<String> title) {
        this.title = title;
    }

    public Optional<Race> getRace() {
        return race;
    }

    public void setRace(Optional<Race> race) {
        this.race = race;
    }

    public Optional<Profession> getProfession() {
        return profession;
    }

    public void setProfession(Optional<Profession> profession) {
        this.profession = profession;
    }

    public Optional<Long> getAfter() {
        return after;
    }

    public void setAfter(Optional<Long> after) {
        this.after = after;
    }

    public Optional<Long> getBefore() {
        return before;
    }

    public void setBefore(Optional<Long> before) {
        this.before = before;
    }

    public Optional<Boolean> getBanned() {
        return banned;
    }

    public void setBanned(Optional<Boolean> banned) {
        this.banned = banned;
    }

    public Optional<Integer> getMinExperience() {
        return minExperience;
    }

    public void setMinExperience(Optional<Integer> minExperience) {
        this.minExperience = minExperience;
    }

    public Optional<Integer> getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Optional<Integer> maxExperience) {
        this.maxExperience = maxExperience;
    }

    public Optional<Integer> getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Optional<Integer> minLevel) {
        this.minLevel = minLevel;
    }

    public Optional<Integer> getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Optional<Integer> maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Optional<String> getPlayerOrder() {
        return playerOrder;
    }

    public void setPlayerOrder(Optional<String> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public Optional<Integer> getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Optional<Integer> pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Optional<Integer> getPageSize() {
        return pageSize;
    }

    public void setPageSize(Optional<Integer> pageSize) {
        this.pageSize = pageSize;
    }
}
