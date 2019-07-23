package coursepath.org;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class AliceInWonderLand {

	private static final String CHAPTER = "CHAPTER";
	
	
	private static void buildTrieWithAvailableWord(Trie trie) {
		trie.insert("Alice could see this, as she was near enough to look over their slates");
		trie.insert("Alice got up and ran off, thinking while she ran, as well she might");
		trie.insert("Alice was beginning to get very tired of sitting by her sister on the\n" + 
				"bank, and of having nothing to do: once or twice she had peeped into the\n" + 
				"book her sister was reading, but it had no pictures or conversations in\n" + 
				"it, ‘and what is the use of a book,’ thought Alice ‘without pictures or\n" + 
				"conversations");
	}
	
	private static void getAutocompleteSentence(String string, Trie trie) {
		List<String> completeSentences = trie.search(string);
		for(String str : completeSentences) {
			System.out.println(str.replace('#', ' '));
		}
	}

	

	private static List<String> removeInvalidWordsAndCharacters(String line) {
		List<String> list = new ArrayList<>();
		String[] words = line.split(" ");
		for (String groupOfWord : words) {
			
			if (groupOfWord.contains("’m") 
					|| groupOfWord.contains("’ve") 
					|| groupOfWord.contains("’d") 
					|| groupOfWord.contains("’re")
					|| groupOfWord.contains("’ve")
					|| groupOfWord.contains("’ll")
					|| groupOfWord.contains("’s")
					|| groupOfWord.contains("’t")) {
				
				filterWord(groupOfWord, list);
				continue;
			}
			for (String word : groupOfWord.split("\\W")) {
				if (!word.isEmpty()) {
					list.add(word);
				}
			}
		}

		return list;
	}

	private static void filterWord(String groupOfWord, List<String> list) {
		
		char[] cArr = groupOfWord.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < cArr.length; i++) {
			if (Character.isLetter(cArr[i])) {
				sb.append(cArr[i]);
			} else if (cArr[i] == '’' && i != 0 &&  i + 1 < cArr.length && Character.isLetter(cArr[i+1])) {
				sb.append(cArr[i]);
			} else {
				if (sb.length() > 0)
				list.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		if (sb.length() > 0) list.add(sb.toString());
	}

	public static int getTotalNumberOfWords(List<String> words) {
		System.out.println("Total Number Of Words: " + words.size());
		return words.size();
	}

	public static int getTotalUniqueWords(List<String> words) {
		Set<String> uniqueWords = new HashSet<>();
		for (String word : words) {
			word = word.toLowerCase();
			uniqueWords.add(word);
		}

		System.out.println("Total Unique Of Words: " + uniqueWords.size());
		return uniqueWords.size();
	}

	public static Map<String, Integer> get20MostFrequentWords(List<String> words, Set<String> top100EnglishWord) {
		Map<String, Integer> wordOccurMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordOccurMap.put(word, wordOccurMap.getOrDefault(word, 0) + 1);
		}

		TreeMap<Integer, List<String>> top20MostOccurTimeMap = new TreeMap<>((a, b) -> (b - a));
		for (String word : wordOccurMap.keySet()) {
			int occur = wordOccurMap.get(word);

			List<String> wordsBelongWithCurrentOccur = top20MostOccurTimeMap.getOrDefault(occur, new ArrayList<>());
			wordsBelongWithCurrentOccur.add(word);
			top20MostOccurTimeMap.put(occur, wordsBelongWithCurrentOccur);
		}

		Map<String, Integer> top20MostFrequentWordMap = new HashMap<>();
		for (int occur : top20MostOccurTimeMap.keySet()) {
			for (String word : top20MostOccurTimeMap.get(occur)) {
				if (top20MostFrequentWordMap.size() == 20)
					break;
				
				if (top100EnglishWord != null && top100EnglishWord.contains(word.toLowerCase())) continue;

				System.out.println(word + " " + occur);
				top20MostFrequentWordMap.put(word, occur);
			}
		}

		return top20MostFrequentWordMap;

	}
	
	public static Map<String, Integer> get20LeastFrequentWords(List<String> words) {
		Map<String, Integer> wordOccurMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordOccurMap.put(word, wordOccurMap.getOrDefault(word, 0) + 1);
		}

		TreeMap<Integer, List<String>> top20LeastFrequentNumberMap = new TreeMap<>();
		for (String word : wordOccurMap.keySet()) {
			int occur = wordOccurMap.get(word);

			List<String> wordsBelongWithFrequent = top20LeastFrequentNumberMap.getOrDefault(occur, new ArrayList<>());
			wordsBelongWithFrequent.add(word);
			top20LeastFrequentNumberMap.put(occur, wordsBelongWithFrequent);
		}

		Map<String, Integer> top20LeastFrequentWordMap = new HashMap<>();
		for (int occur : top20LeastFrequentNumberMap.keySet()) {
			for (String word : top20LeastFrequentNumberMap.get(occur)) {
				if (top20LeastFrequentWordMap.size() == 20)
					break;
				
				System.out.println(word + " " + occur);
				top20LeastFrequentWordMap.put(word, occur);
			}
		}

		return top20LeastFrequentWordMap;
	}

	public static Map<String, Integer> get20MostInterestingFrequentWords(List<String> words) throws FileNotFoundException {
		Set<String> top100EnglishWord = new HashSet<>();
		Scanner sc = new Scanner(new File("most100EnglishWord.txt"));
		while(sc.hasNext()) top100EnglishWord.add(sc.next());
		
		get20MostFrequentWords(words, top100EnglishWord);
		sc.close();
		return null;
		
	}
	
	private static void getFrequencyOfWord(String str, List<String> lines) {
		System.out.println("\nFrequency Of Word " + str + " is: ");
		List<Integer> frequencies = new ArrayList<>();
		
		for(String line : lines) {
			if (line.contains(CHAPTER)) {
				frequencies.add(0);
				continue;
			}
			int count = frequencies.get(frequencies.size() - 1);
			List<String> words = removeInvalidWordsAndCharacters(line);
			for(String word : words) {
				if (word.equalsIgnoreCase(str)) {
					count++;
				}
			}
			frequencies.set(frequencies.size() - 1, count);
		}
		int total = 0;
		for(int frequency : frequencies) {
			total += frequency;
			System.out.print(frequency + " ");
		}
		
		System.out.println("\nTotal is: " + total);
		
	}
	
	private static int getChapterQuoteAppears(String str, List<String> lines) {
		int count = 0;
		String[] strArr = str.split(" ");
		for(int l = 0; l < lines.size(); l++) {
			String line = lines.get(l);
			if (line.contains(CHAPTER)) count++;
			
			int idx = line.indexOf(strArr[0]);
			if (idx < 0) continue;
			
			int i = 0, j = idx;
			boolean isEqual = true;
			int lTemp = l;
			while(true) {
				
				while(i < str.length() && j < line.length()) {
					if (str.charAt(i) != line.charAt(j)) {
						isEqual = false;
						break;
					}
					i++;
					j++;
				}
				
				if (!isEqual) break;
				else if (i == str.length() && isEqual) return count;
				
				while(++lTemp < lines.size() && lines.get(lTemp).trim().isEmpty());
				line = lines.get(lTemp).trim();
				j = 0;
				i++;
			}
			if (i == str.length()) return count;
			
			
		}
		
		return 0;
	}
	
	private static String generateSentence(String beginWord, List<String> lines, List<String> words) {
		Map<String, Integer> wordOccurMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordOccurMap.put(word, wordOccurMap.getOrDefault(word, 0) + 1);
		}
		
		String beginWordTmp = beginWord;
		beginWord = beginWord.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append(beginWord);
		Set<String> visited = new HashSet<>();
		visited.add(beginWord);
		for(int i = 0; i < 20; i++) {
			String newWord = generateNewWord(beginWord, lines, wordOccurMap, visited);
			if (newWord != null && newWord.length() > 0) {
				sb.append(" ").append(newWord);
				beginWord = newWord;
			} else break;
		}
		System.out.println("\nGenerate Sentence from " + "'" + beginWordTmp + "'" + " is: " + sb.toString());
		return sb.toString();
	}
	
	

	private static String generateNewWord(String curWord, List<String> lines, Map<String, Integer> wordOccurMap, Set<String> visited) {
		
		Set<String> wordsFollowCurWord = new HashSet<>();
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.trim().isEmpty() || line.contains(CHAPTER)) continue;
			
			List<String> words = removeInvalidWordsAndCharacters(line);
			boolean isCurWordExist = false;
			int curWordIdx = 0;
			for(int j = 0; j < words.size(); j++) {
				String word = words.get(j).toLowerCase();
				words.set(j, word);
				if (word.equals(curWord)) {
					isCurWordExist = true;
					curWordIdx = j;
				}
			}
			
			if (!isCurWordExist) continue;
			if (curWordIdx == words.size() - 1) {
				// find in next row
				int tmp = i;
				String nextLine = null;
				while(tmp + 1 < lines.size()) {
					nextLine = lines.get(tmp + 1);
					nextLine = nextLine.trim();
					if (!nextLine.isEmpty() && !nextLine.contains(CHAPTER)) break;
					tmp++;
				}
				if (nextLine.isEmpty()) continue;
				String wordFollowCurWord = removeInvalidWordsAndCharacters(nextLine).get(0);
				wordsFollowCurWord.add(wordFollowCurWord.toLowerCase());
			} else wordsFollowCurWord.add(words.get(curWordIdx + 1).toLowerCase());
			
		}
		
		/// Now we have a set of following word after curWord
		int max = 0;
		String newWord = null;
		for(String word : wordsFollowCurWord) {
			if (!visited.contains(word) && wordOccurMap.get(word) > max) {
				max = wordOccurMap.get(word);
				newWord = word;
			}
		}
		
		visited.add(newWord);
		
		return newWord;
	}

	public static void main(String[] args) {
		List<String> words = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		try (BufferedReader bf = new BufferedReader(new FileReader(new File("11-0.txt")))) {
			String line = null;
			while ((line = bf.readLine()) != null) {
				lines.add(line);
				if (line.trim().isEmpty() || line.contains(CHAPTER))
					continue;
				words.addAll(removeInvalidWordsAndCharacters(line));
			}
			getTotalNumberOfWords(words);
			getTotalUniqueWords(words);
			System.out.println("\n20 Most Frequent Words: \n");
			get20MostFrequentWords(words, null);
			System.out.println("\n20 Most Interesting Frequent Words: \n");
			get20MostInterestingFrequentWords(words);
			System.out.println("\n20 Least Interesting Frequent Words: \n");
			get20LeastFrequentWords(words);
			getFrequencyOfWord("Queen", lines);
			int chapter = getChapterQuoteAppears("“Such a trial, dear Sir, With no jury or judge, would be wasting our breath.”", lines);
			System.out.println("chapter " + chapter);
			generateSentence("Alice", lines, words);
			
			Trie trie = new Trie();
			buildTrieWithAvailableWord(trie);
			
			getAutocompleteSentence("Alice g", trie);
			
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex);

		}
	}


	
	

	

	
}

//Total Number Of Words: 26615
//Total Unique Of Words: 2669
//
//20 Most Frequent Words: 
//
//the 1525
//and 800
//to 725
//a 611
//she 502
//of 499
//it 485
//said 456
//I 410
//Alice 386
//in 355
//was 352
//you 312
//that 260
//as 246
//her 243
//at 202
//on 189
//had 177
//with 176
//
//20 Most Interesting Frequent Words: 
//
//I 410
//Alice 386
//not 129
//very 126
//little 125
//went 83
//again 83
//herself 83
//thought 74
//Queen 67
//into 67
//off 62
//me 61
//King 59
//began 58
//Turtle 57
//its 56
//Gryphon 55
//Mock 55
//Hatter 54
//
//20 Least Interesting Frequent Words: 
//
//Twenty 1
//England 1
//Therefore 1
//NEAR 1
//pretend 1
//kettle 1
//introduced 1
//flapper 1
//prison 1
//solid 1
//frames 1
//meanwhile 1
//chimneys 1
//spreading 1
//Game 1
//baked 1
//say--that’s 1
//humble 1
//Coils 1
//C 1




//Total Number Of Words: 26615
//Total Unique Of Words: 2669
//
//20 Most Frequent Words: 
//
//the 1636
//and 870
//to 729
//a 626
//she 542
//it 530
//of 513
//said 462
//i 410
//alice 388
//in 367
//you 366
//was 357
//that 283
//as 263
//her 248
//at 212
//on 193
//all 182
//with 181
//
//20 Most Interesting Frequent Words: 
//
//i 410
//alice 388
//not 145
//very 144
//little 127
//went 83
//again 83
//herself 83
//thought 74
//off 73
//queen 68
//me 68
//into 67
//well 63
//king 61
//began 58
//turtle 57
//its 57
//quite 55
//hatter 55
//
//20 Least Interesting Frequent Words: 
//
//pretend 1
//kettle 1
//introduced 1
//flapper 1
//prison 1
//solid 1
//frames 1
//meanwhile 1
//a--i’m 1
//chimneys 1
//spreading 1
//baked 1
//say--that’s 1
//humble 1
//airs 1
//balanced 1
//result 1
//arrived 1
//corners 1
//bowing 1
//



