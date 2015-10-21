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

  @Override
  public String toString()
  {
    return getName() + ": " + Arrays.toString(factions) + " map=" + options.isMap();
  }
}
