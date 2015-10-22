package com.grunzke.tmprof;

import java.util.Arrays;

public class Game
{
  private String name;
  private int playerCount;
  private String lastUpdate;
  private Faction[] factions;
  private Options options;
  
  public Game()
  {
  }
  
  public boolean isTournamentCompatible()
  {
    if (options.hasDrops() ||
        options.isFireAndIceScoring() ||
        !options.isMiniExpansion1() ||
        !options.isCultistPower() ||
        !options.isShippingBonus() ||
        !options.isVariableTurnOrder())
    {
      return false;
    }
    
    for (Faction f : factions)
    {
      if (f.getFaction().isExpansion())
      {
        return false;
      }
    }
    
    return playerCount == 4;
  }
  
  public boolean hasMinimumSkill(int minimum)
  {
    for (Faction f : factions)
    {
      Player player = f.getPlayer();
      Integer rating = player.getRating();
      if (rating < minimum)
      {
        return false;
      }
    }
    
    return true;
  }
  
  public Faction getFactionByRace(Race r)
  {
    for (Faction f : factions)
    {
      if (f.getFaction()==r)
      {
        return f;
      }
    }
    return null;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getPlayerCount()
  {
    return playerCount;
  }

  public void setPlayerCount(int playerCount)
  {
    this.playerCount = playerCount;
  }

  public String getLastUpdate()
  {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate)
  {
    this.lastUpdate = lastUpdate;
  }
  
  public Options getOptions()
  {
    return options;
  }

  public void setOptions(Options options)
  {
    this.options = options;
  }
  
  public void setFactions(Faction[] factions)
  {
    this.factions = factions;
  }
  
  public static String getDelimitedDataHeader(String delimiter)
  {
    StringBuilder str = new StringBuilder();
    Race.getBaseRaces().forEach(r -> {
      str.append(r);
      str.append(delimiter);
    });
    
    str.append("Dig 2Coins");
    str.append(delimiter);
    str.append("Cult 4Coins");
    str.append(delimiter);
    str.append("6Coins");
    str.append(delimiter);
    str.append("3Power 1Ship");
    str.append(delimiter);
    str.append("3Power 1Worker");
    str.append(delimiter);
    str.append("SH/SA->4vp 2Worker");
    str.append(delimiter);
    str.append("TP->2vp 1Worker");
    str.append(delimiter);
    str.append("1Priest");
    str.append(delimiter);
    str.append("D->1vp 2Coins");
    str.append(delimiter);
    str.append("Ship->3vp 3Power");
    str.append(delimiter);
    str.append("Dig->2vp 1Earth->Coin");
    str.append(delimiter);
    str.append("Dwell->2 4Water->Priest");
    str.append(delimiter);
    str.append("Dwell->2vp 4Fire->4Power");
    str.append(delimiter);
    str.append("TP->3vp 4Water->Dig");
    str.append(delimiter);
    str.append("TP->3vp 4Air->Dig");
    str.append(delimiter);
    str.append("SA/SH->5vp 2Fire->Worker");
    str.append(delimiter);
    str.append("SA/SH->5vp 2Air->Worker");
    str.append(delimiter);
    str.append("Town->5vp 4Earth->Dig");
    return str.toString();
  }
  
  public String getDelimitedData(String delimiter)
  {
    StringBuilder str = new StringBuilder();
    Race.getBaseRaces().forEach(r -> {
      Faction faction = getFactionByRace(r);
      str.append(faction==null ? " " : faction.getScore());
      str.append(delimiter);
    });
    
    str.append(options.isBonus1() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus2() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus3() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus4() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus5() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus6() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus7() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus8() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus9() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.isBonus10() ? "1" : "-1");
    str.append(delimiter);
    str.append(options.getScore1());
    str.append(delimiter);
    str.append(options.getScore3());
    str.append(delimiter);
    str.append(options.getScore5());
    str.append(delimiter);
    str.append(options.getScore6());
    str.append(delimiter);
    str.append(options.getScore8());
    str.append(delimiter);
    str.append(options.getScore4());
    str.append(delimiter);
    str.append(options.getScore7());
    str.append(delimiter);
    str.append(options.getScore2());
    return str.toString();
  }

  @Override
  public String toString()
  {
    return getName() + ": " + Arrays.toString(factions) + " map=" + options.isMap();
  }
}
