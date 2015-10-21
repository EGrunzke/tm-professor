package com.grunzke.tmprof;

import java.util.function.Predicate;

public class Stat
{
  private int with;
  private int withCount;
  private int without;
  private int withoutCount;
  private String description;
  
  private Predicate<Game> pred;
  
  public Stat(String description, Predicate<Game> pred)
  {
    this.pred = pred;
    this.description = description;
  }
  
  public void eval(Game g, Faction f)
  {
    int score = f.getScore();
    if (pred.test(g))
    {
      with += score;
      withCount++;
    }
    else
    {
      without += score;
      withoutCount++;
    }
  }
  
  public double getAverage()
  {
    return (with*1.0)/withCount;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public String toString()
  {
    double withAverage = (with*1.0)/withCount;
    double withoutAverage = (without*1.0)/withoutCount;
//    return String.format("%s: With(%d)=%f Without(%d)=%f", description, withCount, withAverage, withoutCount, withoutAverage);
    return String.format("%s,%f,%f", description, withAverage, withoutAverage);
  }
}
