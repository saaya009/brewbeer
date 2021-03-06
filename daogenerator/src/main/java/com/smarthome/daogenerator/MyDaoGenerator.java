package com.smarthome.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1006, "com.ltbrew.brewbeer.persistence.greendao");

        addRecipe(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addRecipe(Schema schema) {

        Entity recipe = schema.addEntity("DBRecipe");
        recipe.addIdProperty();
        recipe.addIntProperty("formulaId").notNull().unique();
        recipe.addStringProperty("id_type");
        recipe.addStringProperty("name");
        recipe.addStringProperty("idForFn").unique();
        recipe.addStringProperty("ref");
        recipe.addStringProperty("cus");
        recipe.addStringProperty("ndrops");
        recipe.addIntProperty("wr");
        recipe.addIntProperty("wq");

        Entity brewStep = schema.addEntity("DBBrewStep");
        brewStep.addIdProperty();
        brewStep.addStringProperty("stepId").notNull().unique();
        brewStep.addStringProperty("act");
        brewStep.addIntProperty("f");
        brewStep.addIntProperty("pid");
        brewStep.addStringProperty("i");
        brewStep.addIntProperty("k");
        brewStep.addIntProperty("t");
        brewStep.addIntProperty("drn");
        brewStep.addIntProperty("slot");
        Property recipeId = brewStep.addLongProperty("recipeId").notNull().getProperty();
        brewStep.addToOne(recipe, recipeId);

        Entity dbSlot = schema.addEntity("DBSlot");
        dbSlot.addIdProperty();
        dbSlot.addStringProperty("slotStepId").notNull().unique();
        dbSlot.addIntProperty("slotId");
        dbSlot.addStringProperty("name");
        Property recipeId1 = dbSlot.addLongProperty("recipeId").notNull().getProperty();
        dbSlot.addToOne(recipe, recipeId1);

        ToMany recipeToBrewStep = recipe.addToMany(brewStep, recipeId);
        recipeToBrewStep.setName("brewSteps");

        ToMany recipeToSlot = recipe.addToMany(dbSlot, recipeId1);
        recipeToSlot.setName("slots");

        Entity dBBrewHistory = schema.addEntity("DBBrewHistory");
        dBBrewHistory.addIdProperty();
        dBBrewHistory.addLongProperty("formula_id").notNull().unique();
        dBBrewHistory.addLongProperty("package_id").notNull().unique();
        dBBrewHistory.addStringProperty("begin_time");
        dBBrewHistory.addStringProperty("end_time");
        dBBrewHistory.addStringProperty("pid");
        dBBrewHistory.addIntProperty("state");
        dBBrewHistory.addIntProperty("ratio");
        dBBrewHistory.addIntProperty("si");
        dBBrewHistory.addStringProperty("brewingState");
        dBBrewHistory.addStringProperty("st");
        dBBrewHistory.addIntProperty("ms");
        dBBrewHistory.addStringProperty("brewingCmnMsg");
        dBBrewHistory.addBooleanProperty("showStepInfo");
        dBBrewHistory.addStringProperty("brewingStageInfo");
        Property recipeIdHistory = dBBrewHistory.addLongProperty("recipeId").notNull().getProperty();
        dBBrewHistory.addToOne(recipe, recipeIdHistory);


    }
//    Long formula_id;
//    String begin_time;
//    String end_time;
//    Long package_id;
//    Long pid;
//    Integer state;
//    Integer ratio;
//    Integer si;
//    String brewingState;
//    String st;
//    int ms;
//    String brewingCmnMsg;
//    DBRecipe dbRecipe;
//    boolean showStepInfo;
//    String brewingStageInfo

}
