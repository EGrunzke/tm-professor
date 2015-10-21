package com.grunzke.tmprof.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.grunzke.tmprof.Faction;
import com.grunzke.tmprof.Game;
import com.grunzke.tmprof.Options;
import com.grunzke.tmprof.Race;

public class GameDeserializer implements JsonDeserializer<Game>
{
  private Game game;
  
  @Override
  public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    game = new Game();
    JsonObject gameObj = json.getAsJsonObject();
    game.setName(gameObj.get("game").getAsString());
    game.setPlayerCount(gameObj.get("player_count").getAsInt());
    game.setLastUpdate(gameObj.get("last_update").getAsString());
    Faction[] factions = context.deserialize(gameObj.get("factions"), Faction[].class);
    
    JsonObject events = gameObj.get("events").getAsJsonObject();
    
    
    parseEvents(events, factions);
    return game;
  }
  
  private void parseEvents(JsonObject events, Faction[] factions) throws JsonParseException
  {
    JsonObject factionJson = events.get("faction").getAsJsonObject();
    for (Faction faction : factions)
    {
      if (faction.getFaction()==Race.UNKNOWN)
      {
        continue;
      }
      JsonObject fo = factionJson.get(faction.getFaction().toString().toLowerCase()).getAsJsonObject();
      JsonObject vp = fo.get("vp").getAsJsonObject();
      JsonObject round = vp.get("round").getAsJsonObject();
      int rawScore = round.get("all").getAsInt();
      faction.setScore(rawScore+20);
    }
    game.setFactions(factions);
    
    JsonObject global = events.getAsJsonObject("global");
    Options options = new Options();
    
    options.setCultistPower(global.has("option-errata-cultist-power"));
    options.setMiniExpansion1(global.has("option-mini-expansion-1"));
    options.setShippingBonus(global.has("option-shipping-bonus"));
    options.setVariableTurnOrder(global.has("option-variable-turn-order"));
    options.setFireAndIceScoring(global.has("option-fire-and-ice-final-scoring"));
    options.setMap(global.has("map"));
    options.setDrops(global.has("drop-faction"));
    
    scoringRounds(options, global);
    bonusTiles(options, factionJson.get("all").getAsJsonObject());
    game.setOptions(options);
  }

  private void bonusTiles(Options options, JsonObject all)
  {
    options.setBonus1(all.has("pass:BON1"));
    options.setBonus2(all.has("pass:BON2"));
    options.setBonus3(all.has("pass:BON3"));
    options.setBonus4(all.has("pass:BON4"));
    options.setBonus5(all.has("pass:BON5"));
    options.setBonus6(all.has("pass:BON6"));
    options.setBonus7(all.has("pass:BON7"));
    options.setBonus8(all.has("pass:BON8"));
    options.setBonus9(all.has("pass:BON9"));
    options.setBonus10(all.has("pass:BON10"));
  }

  private void scoringRounds(Options options, JsonObject global)
  {
    options.setScore1(getRound("SCORE1", global));
    options.setScore2(getRound("SCORE2", global));
    options.setScore3(getRound("SCORE3", global));
    options.setScore4(getRound("SCORE4", global));
    options.setScore5(getRound("SCORE5", global));
    options.setScore6(getRound("SCORE6", global));
    options.setScore7(getRound("SCORE7", global));
    options.setScore8(getRound("SCORE8", global));
  }
  
  private int getRound(String score, JsonObject global)
  {
    JsonObject obj = global.getAsJsonObject(score);
    if (obj == null)
    {
      return 0;
    }
    
    JsonObject round = obj.getAsJsonObject("round");
    if (round.has("1")) return 1;
    if (round.has("2")) return 2;
    if (round.has("3")) return 3;
    if (round.has("4")) return 4;
    if (round.has("5")) return 5;
    if (round.has("6")) return 6;
    
    throw new IllegalStateException("Illegal round format: " + round.toString());
  }
}
