package com.grunzke.tmprof;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
//      System.out.println("== Reading " + file.getName());
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
    
//    System.out.println(Game.getDelimitedDataHeader("\t"));
//    collected.forEach(g -> System.out.println(g.getDelimitedData("\t")));
//    System.out.println(collected.size());
    
//    createStats();
//    collected.forEach(g -> {
//      for (Race r : Race.values()) {
//        r.eval(g);
//      }
//    });
//    printStats();
    
    printPValues(collected);
  }

  private static void printPValues(List<Game> collected)
  {
    for (Race r : Race.values())
    {
      r.addStat("Dig 2Coins", g -> g.getOptions().isBonus1());
      r.addStat("Cult 4Coins", g -> g.getOptions().isBonus2());
      r.addStat("6Coins", g -> g.getOptions().isBonus3());
      r.addStat("3Power 1Ship", g -> g.getOptions().isBonus4());
      r.addStat("3Power 1Worker", g -> g.getOptions().isBonus5());
      r.addStat("SH/SA->4vp 2Worker", g -> g.getOptions().isBonus6());
      r.addStat("TP->2vp 1Worker", g -> g.getOptions().isBonus7());
      r.addStat("1Priest", g -> g.getOptions().isBonus8());
      r.addStat("D->1vp 2Coins", g -> g.getOptions().isBonus9());
      r.addStat("Ship->3vp 3Power", g -> g.getOptions().isBonus10());
    }
    
    collected.forEach(g -> {
      for (Race r : Race.values()) {
        r.eval(g);
      }
    });
    
    Race.ENGINEERS.getStats().forEach(s -> System.out.print("\t" +s.getDescription()));
    System.out.println();
    Race.getBaseRaces().forEach(r -> System.out.println(r.getStatRow("\t")));
    
//    Race.ENGINEERS.addStat("BON1", g -> g.getOptions().isBonus1());
//    Race.ENGINEERS.addStat("BON3", g -> g.getOptions().isBonus3());
//    collected.forEach(g -> Race.ENGINEERS.eval(g));
//    Race.ENGINEERS.getStats().forEach(System.out::println);
  }
  
  private static void createStats()
  {
    for (Race r : Race.values())
    {
      r.addStat("Dig 2Coins", g -> g.getOptions().isBonus1());
      r.addStat("No: Dig 2Coins", g -> !g.getOptions().isBonus1());
      r.addStat("Cult 4Coins", g -> g.getOptions().isBonus2());
      r.addStat("No: Cult 4Coins", g -> !g.getOptions().isBonus2());
      r.addStat("6Coins", g -> g.getOptions().isBonus3());
      r.addStat("No: 6Coins", g -> !g.getOptions().isBonus3());
      r.addStat("3Power 1Ship", g -> g.getOptions().isBonus4());
      r.addStat("No: 3Power 1Ship", g -> !g.getOptions().isBonus4());
      r.addStat("3Power 1Worker", g -> g.getOptions().isBonus5());
      r.addStat("No: 3Power 1Worker", g -> !g.getOptions().isBonus5());
      r.addStat("SH/SA->4vp 2Worker", g -> g.getOptions().isBonus6());
      r.addStat("No: SH/SA->4vp 2Worker", g -> !g.getOptions().isBonus6());
      r.addStat("TP->2vp 1Worker", g -> g.getOptions().isBonus7());
      r.addStat("No: TP->2vp 1Worker", g -> !g.getOptions().isBonus7());
      r.addStat("1Priest", g -> g.getOptions().isBonus8());
      r.addStat("No: 1Priest", g -> !g.getOptions().isBonus8());
      r.addStat("D->1vp 2Coins", g -> g.getOptions().isBonus9());
      r.addStat("No: D->1vp 2Coins", g -> !g.getOptions().isBonus9());
      r.addStat("Ship->3vp 3Power", g -> g.getOptions().isBonus10());
      r.addStat("No: Ship->3vp 3Power", g -> !g.getOptions().isBonus10());
      
      int[] rounds = new int[]{0,1,2,3,4,5,6};
      for (int round : rounds)
      {
        String prefix = round==0 ? "No" : "R"+round;
        r.addStat(prefix+":Dig->2vp 1Earth->Coin", g -> g.getOptions().getScore1()==round);
        r.addStat(prefix+":Dwell->2 4Water->Priest", g -> g.getOptions().getScore3()==round);
        r.addStat(prefix+":Dwell->2vp 4Fire->4Power", g -> g.getOptions().getScore5()==round);
        r.addStat(prefix+":TP->3vp 4Water->Dig", g -> g.getOptions().getScore6()==round);
        r.addStat(prefix+":TP->3vp 4Air->Dig", g -> g.getOptions().getScore8()==round);
        r.addStat(prefix+":SA/SH->5vp 2Fire->Worker", g -> g.getOptions().getScore4()==round);
        r.addStat(prefix+":SA/SH->5vp 2Air->Worker", g -> g.getOptions().getScore7()==round);
        r.addStat(prefix+":Town->5vp 4Earth->Dig", g -> g.getOptions().getScore2()==round);
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
      r.addStat("BON10", g -> !g.getOptions().isBonus10());
      r.addStat("NoBON10", g -> g.getOptions().isBonus10());
      
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
      races.forEach(r -> System.out.print(r.getStats().get(j).getAverageWith() + ","));
      System.out.println();
    }
  }
}
