package com.dyd.sisbr.util;

import java.io.File;

public class test {

	public static void main(String[] args) {
		String[] lista = Utils.extraerPalabrasJSON(new File("D:/stopword.json"));
		for(String dd: lista){
			System.out.println(dd);
		}
	}

}
