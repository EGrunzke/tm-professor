package com.grunzke.tmprof;

public class Faction
{
  private Race faction;
  private Player player;
  private int score;
  
  public Faction()
  {
  }

  public Race getFaction()
  {
    return faction;
  }

  public void setFaction(Race faction)
  {
    this.faction = faction;
  }

  public Player getPlayer()
  {
    return player;
  }

  public void setPlayer(Player player)
  {
    this.player = player;
  }
  
  public int getScore()
  {
    return score;
  }

  public void setScore(int score)
  {
    this.score = score;
  }
  
  @Override
  public String toString()
  {
    return faction + ": " + player + "=" + score + "vp";
  }
}
