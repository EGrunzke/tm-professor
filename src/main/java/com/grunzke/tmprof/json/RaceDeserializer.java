package com.grunzke.tmprof.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.grunzke.tmprof.Race;

public class RaceDeserializer implements JsonDeserializer<Race>
{
  @Override
  public Race deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
  {
    String s = json.getAsString();
    try
    {
      return Race.valueOf(s.toUpperCase());
    }
    catch (IllegalArgumentException e)
    {
      return Race.UNKNOWN;
    }
  }
}
