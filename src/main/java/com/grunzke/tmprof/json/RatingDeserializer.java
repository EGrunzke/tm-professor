package com.grunzke.tmprof.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class RatingDeserializer implements JsonDeserializer<Map<String, Integer>>
{
  public RatingDeserializer()
  {
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Integer> read()
  {
    try
    {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Map.class, this);
      Gson gson = builder.create();
      BufferedReader br = new BufferedReader(new FileReader(new File("src/main/resources/ratings.json")));
      return gson.fromJson(br, Map.class);
    }
    catch (FileNotFoundException fe)
    {
      throw new RuntimeException(fe);
    }
  }
  
  @Override
  public Map<String, Integer> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    Map<String, Integer> map = new HashMap<String, Integer>();
    JsonObject obj = json.getAsJsonObject();
    JsonObject players = obj.getAsJsonObject("players");
    for (Entry<String, JsonElement> entry : players.entrySet())
    {
      JsonObject player = entry.getValue().getAsJsonObject();
      String user = player.get("username").getAsString();
      int score = player.get("score").getAsInt();
      map.put(user, score);
    }
    return map;
  }
  
}
