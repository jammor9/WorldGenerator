package org.jammor9.worldsim;

import org.jammor9.worldsim.buildings.Building;
import org.jammor9.worldsim.buildings.ConstructionOffice;
import org.jammor9.worldsim.buildings.Farm;
import org.jammor9.worldsim.time.SimListener;
import org.jammor9.worldsim.time.TimeDuration;

import java.util.*;

public class Province implements SimListener {

    //Static Data
    private static final int BASE_INCOME = 30;


    //Object Data
    private record Town(int x, int y, Building building) {};
    private HashSet<WorldTile> provinceTiles = new HashSet<>();
    private Town capital;
    private int regionColor;
    private Random gameRng;

    private int income;
    private int expenses;

    //Building data
    private HashSet<Building> activeBuildings = new HashSet<>();
    private HashMap<ConstructionOffice, Building> constructionOffices = new HashMap<>();
    private HashSet<Building> inactiveBuildings = new HashSet<>();
    private PriorityQueue<Building> constructionQueue = new PriorityQueue<>();

    //Local community data, towns generally grow around exploitation deposits
    private HashSet<Town> provinceTowns = new HashSet<>();

    public Province(int regionColor, Random gameRng) {
        this.regionColor = regionColor;
//        this.income = BASE_INCOME;
        this.gameRng = gameRng;
//        this.capital = createCapital();
//        provinceTowns.add(capital);
//        activeBuildings.add(capital.building);
//        constructionOffices.put(new ConstructionOffice(), null);
//        this.expenses = 0;
    }

    public void addTile(WorldTile t) {
        provinceTiles.add(t);
    }

    public void removeTile(WorldTile t) {
        provinceTiles.remove(t);
    }

    public HashSet<WorldTile> getProvinceTiles() {
        return this.provinceTiles;
    }

    public int getColor() {
        return this.regionColor;
    }

    //Handles logic of advancing days, months, and years
    @Override
    public void timePassed(TimeDuration type) {
        switch(type) {
            case DAY -> dayPassed();
            case MONTH -> monthPassed();
            case YEAR -> yearPassed();
        }
    }

    /*
    Checks construction queues
    Checks for emergency decrees from the state
     */
    public void dayPassed() {
        for (ConstructionOffice c : constructionOffices.keySet()) {
            if(!c.isActive()) {
                Building newProject = constructionQueue.poll();
                if (newProject == null) continue; //No need to try working an empty office
                c.newProject(newProject);
                constructionOffices.put(c, newProject);
            };
            if (c.work()) {
                activeBuildings.add(constructionOffices.get(c));
                constructionOffices.put(c, null);
            }
        }
    }

    /*
    Receive output from each active building
     */
    public void monthPassed() {

    }

    public void yearPassed() {

    }

    //Important for calculating behaviours of class
    public int getProfit() {
        return this.income - this.expenses;
    }

    //Finds highest fertility tile to settle as capital
    private Town createCapital() {
       int maxFertility = Integer.MIN_VALUE;
       WorldTile ct = null;
       for (WorldTile wt : provinceTiles) if (wt.getFertility() > maxFertility) ct = wt;
       if (ct == null) return null;
       else return new Town(ct.x, ct.y, new Farm(ct.getFertility()));
    }

}
