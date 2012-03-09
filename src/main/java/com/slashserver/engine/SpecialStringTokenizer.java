package com.slashserver.engine;

import java.util.ArrayList;
import java.util.List;


public class SpecialStringTokenizer {

	public static class Tokens{
		public List<String> nonMatches=new ArrayList<String>();
		public List<String> matches = new ArrayList<String>();
	}
	
	public Tokens match(String toSplit, String startMatch,String endMatch,boolean includeMatchStringInMatch){
		return match( toSplit,  startMatch, endMatch,new Tokens(), includeMatchStringInMatch);
	}
	
	/**
	 * Given a string to use as start for a match and a string to use
	 *  for end of the match, will return a list of the strings that do not fall within match,
	 *  and another list that is the contents of the matches.
	 */
	public Tokens match(String toSplit, String startMatch,String endMatch,Tokens ret,boolean includeMatchStringInMatch){
		int startMatchLength = includeMatchStringInMatch?0:startMatch.length();
		int endMatchLength = includeMatchStringInMatch?endMatch.length():0;
		int index=-1;
		if(toSplit!=null){
			index = toSplit.indexOf(startMatch);
		}
		
		if(index>-1){
			String nonMatch = toSplit.substring(0,index);
			ret.nonMatches.add(nonMatch);
			
			toSplit = toSplit.substring(nonMatch.length()+startMatchLength);
			int endIndex=toSplit.indexOf(endMatch);
			if(endIndex>-1){
				String match = toSplit.substring(0,endIndex+endMatchLength);
				ret.matches.add(match);
				toSplit = toSplit.substring(endIndex+endMatch.length());
			}else{
				ret.matches.add(toSplit);
				return ret;
			}
			
			return match(toSplit,startMatch,endMatch,ret,includeMatchStringInMatch);
			
		}else{
			ret.nonMatches.add(toSplit);
			return ret;
		}
		
	}
	
	public String concatenate(List<String> strings){
		StringBuilder ret = new StringBuilder();
		for(String string:strings){
			ret.append(string);
		}
		return ret.toString();
	}
	
	public static void main(String[] args){
		
		String initial = "allo allo<what> have we got ere</what> then? <what> is going on?";
		Tokens match = new SpecialStringTokenizer().match(initial,"<what>", "</what>",true);
		System.out.println("true done");
		Tokens matchNot = new SpecialStringTokenizer().match(initial,"<what>", "</what>",false);
		System.out.println("all done");
	}
	
	
	
}
