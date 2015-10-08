package com.grunzke.tmprof;

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
  
  private boolean expansion;
  
  private Race(boolean ex)
  {
    expansion = ex;
  }
  
  public boolean isExpansion()
  {
    return expansion;
  }
}
