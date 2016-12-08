package com.dyd.sisbr.service.impl;

import com.dyd.sisbr.service.LematizadorService;

public class LematizadorServiceImpl implements LematizadorService{

	private boolean is_vowel(char c) {
		return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
				|| c == '�' || c == '�' || c == '�' || c == '�' || c == '�');
	}

	/**
	 * 
	 * @param word
	 * @param start
	 * @return
	 */
	private int getNextVowelPos(String word, int start) {
		int len = strlen(word);
		for (int i = start; i < len; i++) {
			if (is_vowel(word.charAt(i))) {
				return i;
			}
		}
		return len;
	}

	/**
	 * 
	 * @param word
	 * @param start
	 * @return
	 */
	private int getNextConsonantPos(String word, int start) {
		int len = strlen(word);
		for (int i = start; i < len; i++) {
			if (!is_vowel(word.charAt(i))) {
				return i;
			}
		}
		return len;
	}

	/**
	 * 
	 * @param word
	 * @param suffix
	 * @return
	 */
	private boolean endsin(String word, String suffix) {
		if (strlen(word) < strlen(suffix)) {
			return false;
		}
		return (substr(word, -strlen(suffix)).equals(suffix));
	}

	/**
	 * 
	 * @param word
	 * @param suffixes
	 * @return
	 */
	private String endsinArr(String word, String[] suffixes) {
		for (String suff : suffixes) {
			if (endsin(word, suff)) {
				return suff;
			}
		}
		return "";
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	private String removeAccent(String word) {
		word = word.replace('�', 'a');
		word = word.replace('�', 'e');
		word = word.replace('�', 'i');
		word = word.replace('�', 'o');
		word = word.replace('�', 'u');
		return word;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	@Override
	public String stemm(String word) {

		int len = strlen(word);
		if (len <= 2) {
			return word;
		}

		word = word.toLowerCase();

		int r1, r2, rv;
		r1 = r2 = rv = len;

		// R1 is the region after the first non-vowel following a vowel, or is
		// the null region at the end of the word if there is no such non-vowel.
		for (int i = 0; i < (len - 1) && r1 == len; i++) {
			if (is_vowel(word.charAt(i)) && !is_vowel(word.charAt(i + 1))) {
				r1 = i + 2;
			}
		}

		// R2 is the region after the first non-vowel following a vowel in R1,
		// or is the null region at the end of the word if there is no such
		// non-vowel.
		for (int i = r1; i < (len - 1) && r2 == len; i++) {
			if (is_vowel(word.charAt(i)) && !is_vowel(word.charAt(i + 1))) {
				r2 = i + 2;
			}
		}

		if (len > 3) {
			if (!is_vowel(word.charAt(1))) {
				// If the second letter is a consonant, RV is the region after
				// the next following vowel
				rv = getNextVowelPos(word, 2) + 1;
			} else if (is_vowel(word.charAt(0)) && is_vowel(word.charAt(1))) {
				// or if the first two letters are vowels, RV is the region
				// after the next consonant
				rv = getNextConsonantPos(word, 2) + 1;
			} else {
				// otherwise (consonant-vowel case) RV is the region after the
				// third letter. But RV is the end of the word if these
				// positions cannot be found.
				rv = 3;
			}
		}

		String r1_txt = substr(word, r1);
		String r2_txt = substr(word, r2);
		String rv_txt = substr(word, rv);

		String word_orig = word;

		// Step 0: Attached pronoun
		String[] pronoun_suf = array("me", "se", "sela", "selo", "selas",
				"selos", "la", "le", "lo", "las", "les", "los", "nos");
		String[] pronoun_suf_pre1 = array("�ndo", "�ndo", "�r", "�r", "�r");
		String[] pronoun_suf_pre2 = array("ando", "iendo", "ar", "er", "ir");
		String suf = endsinArr(word, pronoun_suf);

		if (!suf.equals("")) {
			String pre_suff = endsinArr(substr(rv_txt, 0, -strlen(suf)),
					pronoun_suf_pre1);
			if (!pre_suff.equals("")) {
				word = removeAccent(substr(word, 0, -strlen(suf)));
			} else {
				pre_suff = endsinArr(substr(rv_txt, 0, -strlen(suf)),
						pronoun_suf_pre2);
				if (!pre_suff.equals("")
						|| (endsin(word, "yendo") && (substr(word,
								-strlen(suf) - 6, 1).equals("u")))) {
					word = substr(word, 0, -strlen(suf));
				}
			}
		}

		if (!word.equals(word_orig)) {
			r1_txt = substr(word, r1);
			r2_txt = substr(word, r2);
			rv_txt = substr(word, rv);
		}
		String word_after0 = word;

		if (!(suf = endsinArr(
				r2_txt,
				array("anza", "anzas", "ico", "ica", "icos", "icas", "ismo",
						"ismos", "able", "ables", "ible", "ibles", "ista",
						"istas", "oso", "osa", "osos", "osas", "amiento",
						"amientos", "imiento", "imientos"))).equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(
				r2_txt,
				array("icadora", "icador", "icaci�n", "icadoras", "icadores",
						"icaciones", "icante", "icantes", "icancia",
						"icancias", "adora", "ador", "aci�n", "adoras",
						"adores", "aciones", "ante", "antes", "ancia", "ancias")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(r2_txt, array("log�a", "log�as")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf)) + "log";
		} else if (!(suf = endsinArr(r2_txt, array("uci�n", "uciones")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf)) + "u";
		} else if (!(suf = endsinArr(r2_txt, array("encia", "encias")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf)) + "ente";
		} else if (!(suf = endsinArr(
				r2_txt,
				array("ativamente", "ivamente", "osamente", "icamente",
						"adamente"))).equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(r1_txt, array("amente"))).equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(r2_txt,
				array("antemente", "ablemente", "iblemente", "mente")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(
				r2_txt,
				array("abilidad", "abilidades", "icidad", "icidades", "ividad",
						"ividades", "idad", "idades"))).equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(
				r2_txt,
				array("ativa", "ativo", "ativas", "ativos", "iva", "ivo",
						"ivas", "ivos"))).equals("")) {
			word = substr(word, 0, -strlen(suf));
		}

		if (!word.equals(word_after0)) {
			r1_txt = substr(word, r1);
			r2_txt = substr(word, r2);
			rv_txt = substr(word, rv);
		}
		String word_after1 = word;

		if (word_after0.equals(word_after1)) {
			// Do step 2a if no ending was removed by step 1.
			if (!(suf = endsinArr(
					rv_txt,
					array("ya", "ye", "yan", "yen", "yeron", "yendo", "yo",
							"y�", "yas", "yes", "yais", "yamos"))).equals("")
					&& !(substr(word, -strlen(suf) - 1, 1).equals("u"))) {
				word = substr(word, 0, -strlen(suf));
			}

			if (!word.equals(word_after1)) {
				r1_txt = substr(word, r1);
				r2_txt = substr(word, r2);
				rv_txt = substr(word, rv);
			}
			String word_after2a = word;

			// Do Step 2b if step 2a was done, but failed to remove a suffix.
			if (word_after2a.equals(word_after1)) {
				if (!(suf = endsinArr(rv_txt, array("en", "es", "�is", "emos")))
						.equals("")) {
					word = substr(word, 0, -strlen(suf));
					if (endsin(word, "gu")) {
						word = substr(word, 0, -1);
					}
				} else if (!(suf = endsinArr(
						rv_txt,
						array("ar�an", "ar�as", "ar�n", "ar�s", "ar�ais",
								"ar�a", "ar�is", "ar�amos", "aremos", "ar�",
								"ar�", "er�an", "er�as", "er�n", "er�s",
								"er�ais", "er�a", "er�is", "er�amos", "eremos",
								"er�", "er�", "ir�an", "ir�as", "ir�n", "ir�s",
								"ir�ais", "ir�a", "ir�is", "ir�amos", "iremos",
								"ir�", "ir�", "aba", "ada", "ida", "�a", "ara",
								"iera", "ad", "ed", "id", "ase", "iese",
								"aste", "iste", "an", "aban", "�an", "aran",
								"ieran", "asen", "iesen", "aron", "ieron",
								"ado", "ido", "ando", "iendo", "i�", "ar",
								"er", "ir", "as", "abas", "adas", "idas",
								"�as", "aras", "ieras", "ases", "ieses", "�s",
								"�is", "abais", "�ais", "arais", "ierais",
								"aseis", "ieseis", "asteis", "isteis", "ados",
								"idos", "amos", "�bamos", "�amos", "imos",
								"�ramos", "i�ramos", "i�semos", "�semos")))
						.equals("")) {
					word = substr(word, 0, -strlen(suf));
				}
			}
		}

		// Always do step 3.
		r1_txt = substr(word, r1);
		r2_txt = substr(word, r2);
		rv_txt = substr(word, rv);

		if (!(suf = endsinArr(rv_txt, array("os", "a", "o", "�", "�", "�")))
				.equals("")) {
			word = substr(word, 0, -strlen(suf));
		} else if (!(suf = endsinArr(rv_txt, array("e", "�"))).equals("")) {
			word = substr(word, 0, -1);
			rv_txt = substr(word, rv);
			if (endsin(rv_txt, "u") && endsin(word, "gu")) {
				word = substr(word, 0, -1);
			}
		}

		return removeAccent(word);
	}

	/**
	 * 
	 * @param word
	 * @param i
	 * @param j
	 * @return
	 */
	@Override
	public String substr(String word, int start, int length) {

		if (start == length) {
			return "";

		} else {

			if ((start >= 0)) {

				if ((length >= 0)) { // longitud positiva
					int endIndex = start + length;
					if (endIndex > word.length()) {
						word = word.substring(start, word.length());
						return word;
					} else {
						word = word.substring(start, endIndex);
						return word;
					}
				} else { // longitud negativa
					int endIndex = word.length() + length;

					try {
						word = word.substring(start, endIndex);
					} catch (StringIndexOutOfBoundsException e) {
						word = "";
					}

					return word;
				}

			}// end if
			else {
				if ((length >= 0)) { // longitud positiva

					int inicio = word.length() + start;
					int endIndex = inicio + length;

					// System.out.println(inicio +" "+ endIndex);

					if (endIndex > word.length()) {
						word = word.substring(inicio, word.length());
						return word;
					} else {
						word = word.substring(inicio, endIndex);
						return word;
					}
				} else { // longitud negativa

					int inicio = word.length() + start;
					int endIndex = word.length() + length;

					try {
						word = word.substring(inicio, endIndex);
					} catch (StringIndexOutOfBoundsException e) {
						word = "";
					}

					return word;
				}
			}// end else

		}// final else

	}// en funcion

	/**
	 * 
	 * @param word
	 * @param i
	 * @return
	 */
	@Override
	public String substr(String word, int i) {
		if (Math.abs(i) > word.length()) {
			return word;
		} else if (i >= 0) {
			return word.substring(i, word.length());
		} else {
			// en este punto i es negativo
			return word.substring(word.length() + i, word.length());
		}
	}

	/**
	 * 
	 * @param strings
	 * @return
	 */
	private String[] array(String... strings) {
		return strings;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	private int strlen(String word) {
		return word.length();
	}

}
