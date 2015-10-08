package com.grunzke.tmprof.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.grunzke.tmprof.Player;
import com.grunzke.tmprof.Ratings;

public class PlayerDeserializer implements JsonDeserializer<Player>
{
  @Override
  public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    String s = json.getAsString();
    Player p = new Player();
    p.setName(s);
    p.setRating(Ratings.INSTANCE.getRating(s));
    return p;
  }
}
