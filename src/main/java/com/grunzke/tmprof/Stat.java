package com.grunzke.tmprof;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.TTest;

public class Stat
{
  private List<Integer> with;
  private List<Integer> without;
  private String description;
  
  private Predicate<Game> pred;
  
  public Stat(String description, Predicate<Game> pred)
  {
    this.pred = pred;
    this.description = description;
    this.with = new ArrayList<>();
    this.without = new ArrayList<>();
  }
  
  public void eval(Game g, Faction f)
  {
    int score = f.getScore();
    if (pred.test(g))
    {
      with.add(score);
    }
    else
    {
      without.add(score);
    }
  }
  
  public double getAverageWith()
  {
    Integer sum = with.stream().reduce(0, Integer::sum);
    return sum.doubleValue()/with.size();
  }
  
  public double getAverageWithout()
  {
    Integer sum = without.stream().reduce(0, Integer::sum);
    return sum.doubleValue()/without.size();
  }
  
  public double getPValue()
  {
    double[] withArray = with.stream().mapToDouble(Integer::doubleValue).toArray();
    double[] withoutArray = without.stream().mapToDouble(Integer::doubleValue).toArray();
    if (withArray.length<2 || withoutArray.length<2)
    {
      return Double.NaN;
    }
    else
    {
      return new TTest().homoscedasticTTest(withArray, withoutArray);
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public String toString()
  {
    return String.format("%s,%f,%f,%f", description, getAverageWith(), getAverageWithout(), getPValue());
  }
}
