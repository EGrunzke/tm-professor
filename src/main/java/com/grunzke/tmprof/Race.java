package com.grunzke.tmprof;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum Race
{
  SWARMLINGS(false),
  MERMAIDS(false),
  WITCHES(false),
  AUREN(false),
  ENGINEERS(false),
  DWARVES(false),
  GIANTS(false),
  CHAOSMAGICIANS(false),
  NOMADS(false),
  FAKIRS(false),
  HALFLINGS(false),
  CULTISTS(false),
  DARKLINGS(false),
  ALCHEMISTS(false),
  RIVERWALKERS(true),
  SHAPESHIFTERS(true),
  ACOLYTES(true),
  DRAGONLORDS(true),
  YETIS(true),
  ICEMAIDENS(true),
  UNKNOWN(false);
  
  private List<Stat> stats;
  
  private boolean expansion;
  
  private Race(boolean ex)
  {
    expansion = ex;
    stats = new ArrayList<Stat>();
  }
  
  public void addStat(String description, Predicate<Game> pred)
  {
    stats.add(new Stat(description, pred));
  }
  
  public List<Stat> getStats()
  {
    return stats;
  }
  
  public boolean isExpansion()
  {
    return expansion;
  }

  public void eval(Game g)
  {
    Faction f = g.getFactionByRace(this);
    if (f==null)
    {
      return;
    }
    
    stats.forEach(s -> s.eval(g, f));
  }
  
  public String getStatRow(String delimiter)
  {
    Stream<String> pValues = stats.stream().map(s -> Double.toString(s.getPValue()));
    String row = pValues.reduce((s, t) -> s + delimiter + t).get();
    return this.toString() + delimiter + row;
  }
  
  public static List<Race> getBaseRaces()
  {
    return Arrays.asList(new Race[]{SWARMLINGS, MERMAIDS, WITCHES, AUREN, ENGINEERS, DWARVES, GIANTS, CHAOSMAGICIANS, NOMADS, FAKIRS, HALFLINGS, CULTISTS, DARKLINGS, ALCHEMISTS });
  }
}
