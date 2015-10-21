package com.grunzke.tmprof.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.grunzke.tmprof.Options;

public class OptionsDeserializer implements JsonDeserializer<Options>
{
  private JsonObject global;
  private Options options;

  @Override
  public Options deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    JsonObject events = json.getAsJsonObject();
    global = events.getAsJsonObject("global");
    options = new Options();
    
    options.setCultistPower(global.has("option-errata-cultist-power"));
    options.setMiniExpansion1(global.has("option-mini-expansion-1"));
    options.setShippingBonus(global.has("option-shipping-bonus"));
    options.setVariableTurnOrder(global.has("option-variable-turn-order"));
    options.setFireAndIceScoring(global.has("option-fire-and-ice-final-scoring"));
    options.setMap(global.has("map"));
    options.setDrops(global.has("drop-faction"));
    
    scoringRounds();
    
    return options;
  }

  private void scoringRounds()
  {
    options.setScore1(getRound("SCORE1"));
    options.setScore2(getRound("SCORE2"));
    options.setScore3(getRound("SCORE3"));
    options.setScore4(getRound("SCORE4"));
    options.setScore5(getRound("SCORE5"));
    options.setScore6(getRound("SCORE6"));
    options.setScore7(getRound("SCORE7"));
    options.setScore8(getRound("SCORE8"));
  }
  
  private int getRound(String score)
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
