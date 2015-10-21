package com.grunzke.tmprof;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grunzke.tmprof.json.GameDeserializer;
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
    builder.registerTypeAdapter(Game.class, new GameDeserializer());
    builder.registerTypeAdapter(Player.class, new PlayerDeserializer());
    builder.registerTypeAdapter(Race.class, new RaceDeserializer());
    builder.registerTypeAdapter(Options.class, new OptionsDeserializer());
    Gson gson = builder.create();
    
    FilenameFilter filter = new FilenameFilter()
    {
      @Override
      public boolean accept(File dir, String name)
      {
        return name.matches("\\d{4}-\\d{2}.json");
      }
    };
    
    List<Game> collected = new LinkedList<Game>();
    for (File file : new File("src/main/resources").listFiles(filter))
    {
      System.out.println("== Reading " + file.getName());
      BufferedReader br = new BufferedReader(new FileReader(file));
      List<Game> allGames = Arrays.asList(gson.fromJson(br, Game[].class));
      Stream<Game> stream = allGames.stream().filter(g -> {
        try {
          return g.isTournamentCompatible() && g.hasMinimumSkill(1150) && g.getPlayerCount()==4;
        }
        catch (Exception e) {
          System.err.println(g.getName() + ": " + e);
          return false;
        }
      });
      stream.forEach(g -> collected.add(g));
      br.close();
    }
//    collected.forEach(g -> System.out.println(g));
    System.out.println(collected.size());
    
    customStats();
    collected.forEach(g -> {
      for (Race r : Race.values()) {
        r.eval(g);
      }
    });
    printStats();
  }
  
  private static void createStats()
  {
    for (Race r : Race.values())
    {
      r.addStat("BON1", g -> g.getOptions().isBonus1());
      r.addStat("BON2", g -> g.getOptions().isBonus2());
      r.addStat("BON3", g -> g.getOptions().isBonus3());
      r.addStat("BON4", g -> g.getOptions().isBonus4());
      r.addStat("BON5", g -> g.getOptions().isBonus5());
      r.addStat("BON6", g -> g.getOptions().isBonus6());
      r.addStat("BON7", g -> g.getOptions().isBonus7());
      r.addStat("BON8", g -> g.getOptions().isBonus8());
      r.addStat("BON9", g -> g.getOptions().isBonus9());
      r.addStat("BON10", g -> g.getOptions().isBonus10());
      
      int[] rounds = new int[]{0,1,2,3,4,5,6};
      for (int round : rounds)
      {
        r.addStat("SCORE1-R"+round, g -> g.getOptions().getScore1()==round);
        r.addStat("SCORE2-R"+round, g -> g.getOptions().getScore2()==round);
        r.addStat("SCORE3-R"+round, g -> g.getOptions().getScore3()==round);
        r.addStat("SCORE4-R"+round, g -> g.getOptions().getScore4()==round);
        r.addStat("SCORE5-R"+round, g -> g.getOptions().getScore5()==round);
        r.addStat("SCORE6-R"+round, g -> g.getOptions().getScore6()==round);
        r.addStat("SCORE7-R"+round, g -> g.getOptions().getScore7()==round);
        r.addStat("SCORE8-R"+round, g -> g.getOptions().getScore8()==round);
      }
    }
  }
  
  private static void customStats()
  {
    for (Race r : Race.values())
    {
      r.addStat("NoBON1", g -> !g.getOptions().isBonus1());
      r.addStat("BON2", g -> g.getOptions().isBonus2());
      r.addStat("BON3", g -> g.getOptions().isBonus3());
      r.addStat("BON4", g -> g.getOptions().isBonus4());
      r.addStat("BON5", g -> g.getOptions().isBonus5());
      r.addStat("BON6", g -> g.getOptions().isBonus6());
      r.addStat("NoBON7", g -> !g.getOptions().isBonus7());
      r.addStat("BON8", g -> g.getOptions().isBonus8());
      r.addStat("BON9", g -> g.getOptions().isBonus9());
      r.addStat("NoBON10", g -> !g.getOptions().isBonus10());
      
      r.addStat("SCORE2-R1", g -> g.getOptions().getScore2()==1);
      r.addStat("SCORE6-R3", g -> g.getOptions().getScore6()==3);
      r.addStat("SCORE5-R4", g -> g.getOptions().getScore5()==4);
      r.addStat("SCORE8-R5", g -> g.getOptions().getScore8()==5);
      r.addStat("SCORE3-R6", g -> g.getOptions().getScore3()==6);
      r.addStat("SCORE1-NA", g -> g.getOptions().getScore1()==0);
      r.addStat("SCORE4-NA", g -> g.getOptions().getScore4()==0);
      r.addStat("SCORE7-NA", g -> g.getOptions().getScore7()==0);
      
      r.addStat("All", g -> true);
    }
  }
  
  private static void printStats()
  {
    List<Race> races = Arrays.asList(Race.values());
    int size = Race.ALCHEMISTS.getStats().size();
    races.forEach(r -> System.out.print("," + r));
    System.out.println();
    
    for (int i=0; i<size; i++)
    {
      final int j = i;
      System.out.print(Race.ALCHEMISTS.getStats().get(i).getDescription() + ",");
      races.forEach(r -> System.out.print(r.getStats().get(j).getAverage() + ","));
      System.out.println();
    }
  }
}
