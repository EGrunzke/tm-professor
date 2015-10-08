package com.grunzke.tmprof;


public class Player
{
  private String name;
  private Integer rating;
  
  public Player()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Integer getRating()
  {
    return rating;
  }

  public void setRating(Integer rating)
  {
    this.rating = rating;
  }
  
  @Override
  public String toString()
  {
    return name + "(" + rating + ")";
  }
}
