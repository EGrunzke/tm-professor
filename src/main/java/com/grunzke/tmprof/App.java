package com.grunzke.tmprof;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grunzke.tmprof.json.OptionsDeserializer;
import com.grunzke.tmprof.json.PlayerDeserializer;
import com.grunzke.tmprof.json.RaceDeserializer;

/**
 * Hello world!
 *
 */
public class App
{
  public static void main(String[] args) throws Exception
  {
//    BufferedReader br = new BufferedReader(new InputStreamReader(App.class.getResourceAsStream("/2015-09.json")));
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Player.class, new PlayerDeserializer());
    builder.registerTypeAdapter(Race.class, new RaceDeserializer());
    builder.registerTypeAdapter(Options.class, new OptionsDeserializer());
    Gson gson = builder.create();
    
    for (File file : new File("src/main/resources").listFiles())
    {
      System.out.println("== Reading " + file.getName());
      BufferedReader br = new BufferedReader(new FileReader(file));
      List<Game> gameList = Arrays.asList(gson.fromJson(br, Game[].class));
      gameList.stream().filter(g -> {
        try {
          return g.isTournamentCompatible() && g.hasMinimumSkill(1200) && g.getPlayer_count()==4;
        }
        catch (Exception e) {
          System.err.println(g.getGame() + ": " + e);
          return false;
        }
      }).forEach(g -> System.out.println(g));
      br.close();
    }
  }
}
