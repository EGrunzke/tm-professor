package com.grunzke.tmprof;

import java.util.Arrays;

public class Game
{
  private String game;
  private int player_count;
  private String last_update;
  private Faction[] factions;
  private Options events;
  
  public Game()
  {
  }
  
  public boolean isTournamentCompatible()
  {
    if (events.hasDrops() ||
        events.isFireAndIceScoring() ||
        !events.isMiniExpansion1() ||
        !events.isCultistPower() ||
        !events.isShippingBonus() ||
        !events.isVariableTurnOrder())
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
    
    return player_count == 4;
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

  public String getGame()
  {
    return game;
  }

  public void setGame(String game)
  {
    this.game = game;
  }

  public int getPlayer_count()
  {
    return player_count;
  }

  public void setPlayer_count(int player_count)
  {
    this.player_count = player_count;
  }

  public String getLast_update()
  {
    return last_update;
  }

  public void setLast_update(String last_update)
  {
    this.last_update = last_update;
  }
  
  public Options getEvents()
  {
    return events;
  }

  public void setEvents(Options options)
  {
    this.events = options;
  }

  @Override
  public String toString()
  {
    return getGame() + ": " + Arrays.toString(factions) + " map=" + events.isMap();
  }
}
