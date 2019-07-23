package coursepath.org;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author vu_leenguyen
 *
 */
public class AliceInWonderLand {

	private static final String BLANK = " ";
	private static final String FILE_INPUT = "AliceInWonderLand.txt";
	private static final String CHAPTER = "CHAPTER";

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
		Map<String, Integer> wordMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
		}

		TreeMap<Integer, List<String>> top20MostFrequentMap = new TreeMap<>((a, b) -> (b - a));
		for (String word : wordMap.keySet()) {
			int wordCount = wordMap.get(word);

			List<String> wordsBelongWithCurrentOccur = top20MostFrequentMap.getOrDefault(wordCount, new ArrayList<>());
			wordsBelongWithCurrentOccur.add(word);
			top20MostFrequentMap.put(wordCount, wordsBelongWithCurrentOccur);
		}

		Map<String, Integer> top20MostFrequentWordMap = new HashMap<>();
		for (int wordFrequent : top20MostFrequentMap.keySet()) {
			for (String word : top20MostFrequentMap.get(wordFrequent)) {
				if (top20MostFrequentWordMap.size() == 20)
					break;

				if (top100EnglishWord != null && top100EnglishWord.contains(word.toLowerCase()))
					continue;

				System.out.println(new StringBuilder().append(word).append(BLANK).append(wordFrequent).toString());
				top20MostFrequentWordMap.put(word, wordFrequent);
			}
		}

		return top20MostFrequentWordMap;

	}

	public static Map<String, Integer> get20MostInterestingFrequentWords(List<String> words) {
		Set<String> top100EnglishWord = new HashSet<>();
		try (Scanner sc = new Scanner(new File("most100EnglishWord.txt"))) {
			while (sc.hasNext())
				top100EnglishWord.add(sc.next());
			return get20MostFrequentWords(words, top100EnglishWord);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Map<String, Integer> get20LeastFrequentWords(List<String> words) {
		Map<String, Integer> wordMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
		}

		TreeMap<Integer, List<String>> top20LeastFrequentMap = new TreeMap<>();
		for (String word : wordMap.keySet()) {
			int wordCount = wordMap.get(word);

			List<String> wordsBelongWithFrequent = top20LeastFrequentMap.getOrDefault(wordCount, new ArrayList<>());
			wordsBelongWithFrequent.add(word);
			top20LeastFrequentMap.put(wordCount, wordsBelongWithFrequent);
		}

		Map<String, Integer> top20LeastFrequentWordMap = new HashMap<>();
		for (int wordCount : top20LeastFrequentMap.keySet()) {
			for (String word : top20LeastFrequentMap.get(wordCount)) {
				if (top20LeastFrequentWordMap.size() == 20)
					break;

				System.out.println(new StringBuilder().append(word).append(BLANK).append(wordCount).toString());
				top20LeastFrequentWordMap.put(word, wordCount);
			}
		}

		return top20LeastFrequentWordMap;
	}
	
	public static void getFrequencyOfWord(String str, List<String> lines) {
		System.out.println(String.format("\nFrequency Of Word '%s' is", str));
		List<Integer> frequencies = new ArrayList<>();

		for (String line : lines) {
			if (line.contains(CHAPTER)) {
				frequencies.add(0);
				continue;
			}
			int count = frequencies.get(frequencies.size() - 1);
			List<String> words = removeInvalidWordsAndCharacters(line);
			for (String word : words) {
				if (word.equalsIgnoreCase(str)) {
					count++;
				}
			}
			frequencies.set(frequencies.size() - 1, count);
		}
		int total = 0;
		for (int frequency : frequencies) {
			total += frequency;
			System.out.print(frequency + BLANK);
		}

		System.out.println("\nTotal is: " + total);

	}
	
	public static int getChapterQuoteAppears(String str, List<String> lines) {
		int count = 0;
		String[] strArr = str.split(BLANK);
		for (int l = 0; l < lines.size(); l++) {
			String line = lines.get(l);
			if (line.contains(CHAPTER))
				count++;

			int idx = line.indexOf(strArr[0]);
			if (idx < 0) continue;

			int i = 0, j = idx;
			boolean isEqual = true;
			int lTemp = l;
			while (true) {

				while (i < str.length() && j < line.length()) {
					if (str.charAt(i) != line.charAt(j)) {
						isEqual = false;
						break;
					}
					i++;
					j++;
				}

				if (!isEqual) break;
				else if (i == str.length() && isEqual) return count;

				while (++lTemp < lines.size() && lines.get(lTemp).trim().isEmpty());
				line = lines.get(lTemp).trim();
				j = 0;
				i++;
			}
			if (i == str.length()) return count;
		}

		return -1;
	}
	
	public static String generateSentence(String beginWord, List<String> lines, List<String> words) {
		Map<String, Integer> wordMap = new HashMap<>();
		for (String word : words) {
			word = word.toLowerCase();
			wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
		}

		String beginWordTmp = beginWord;
		beginWord = beginWord.toLowerCase();
		StringBuilder sb = new StringBuilder();
		sb.append(beginWord);
		Set<String> visited = new HashSet<>();
		visited.add(beginWord);
		for (int i = 0; i < 20; i++) {
			String newWord = generateNextWord(beginWord, lines, wordMap, visited);
			if (newWord != null && newWord.length() > 0) {
				sb.append(BLANK).append(newWord);
				beginWord = newWord;
			} else break;
		}
		System.out.println("\nGenerate Sentence from " + "'" + beginWordTmp + "'" + " is: " + sb.toString());
		return sb.toString();
	}

	private static String generateNextWord(String curWord, List<String> lines, Map<String, Integer> wordMap,
			Set<String> visited) {

		Set<String> nextWords = new HashSet<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.trim().isEmpty() || line.contains(CHAPTER))
				continue;

			List<String> words = removeInvalidWordsAndCharacters(line);
			boolean isWordExistInCurrentLine = false;
			int wordIdxInCurrentLine = 0;
			for (int j = 0; j < words.size(); j++) {
				String word = words.get(j).toLowerCase();
				words.set(j, word);
				if (word.equals(curWord)) {
					isWordExistInCurrentLine = true;
					wordIdxInCurrentLine = j;
				}
			}

			if (!isWordExistInCurrentLine) continue;
			if (wordIdxInCurrentLine == words.size() - 1) {
				int tmp = i;
				String nextLine = null;
				while (tmp + 1 < lines.size()) {
					nextLine = lines.get(tmp + 1);
					nextLine = nextLine.trim();
					if (!nextLine.isEmpty() && !nextLine.contains(CHAPTER))
						break;
					tmp++;
				}
				if (nextLine.isEmpty()) continue;
				String wordFollowCurWord = removeInvalidWordsAndCharacters(nextLine).get(0);
				nextWords.add(wordFollowCurWord.toLowerCase());
			} else
				nextWords.add(words.get(wordIdxInCurrentLine + 1).toLowerCase());

		}

		int max = 0;
		String nextWord = null;
		for (String word : nextWords) {
			if (!visited.contains(word) && wordMap.get(word) > max) {
				max = wordMap.get(word);
				nextWord = word;
			}
		}

		visited.add(nextWord);

		return nextWord;
	}

	private static void buildTrieWithAvailableWord(Trie trie) {
		trie.insert("Alice could see this, as she was near enough to look over their slates");
		trie.insert("Alice got up and ran off, thinking while she ran, as well she might");
		trie.insert("Alice was beginning to get very tired of sitting by her sister on the\n"
				+ "bank, and of having nothing to do: once or twice she had peeped into the\n"
				+ "book her sister was reading, but it had no pictures or conversations in\n"
				+ "it, ‘and what is the use of a book,’ thought Alice ‘without pictures or\n" + "conversations");
	}

	private static void getAutocompleteSentence(String string, Trie trie) {
		List<String> completeSentences = trie.search(string);
		for (String str : completeSentences) {
			System.out.println(str.replace('#', ' '));
		}
	}

	private static List<String> removeInvalidWordsAndCharacters(String line) {
		List<String> list = new ArrayList<>();
		String[] words = line.split(BLANK);
		for (String groupOfWord : words) {

			if (groupOfWord.contains("’m") || groupOfWord.contains("’ve") || groupOfWord.contains("’d")
					|| groupOfWord.contains("’re") || groupOfWord.contains("’ve") || groupOfWord.contains("’ll")
					|| groupOfWord.contains("’s") || groupOfWord.contains("’t")) {

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
		for (int i = 0; i < cArr.length; i++) {
			if (Character.isLetter(cArr[i])) {
				sb.append(cArr[i]);
			} else if (cArr[i] == '’' && i != 0 && i + 1 < cArr.length && Character.isLetter(cArr[i + 1])) {
				sb.append(cArr[i]);
			} else {
				if (sb.length() > 0)
					list.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		if (sb.length() > 0)
			list.add(sb.toString());
	}

	

	public static void main(String[] args) {
		List<String> words = new ArrayList<>();
		List<String> lines = new ArrayList<>();
		try (BufferedReader bf = new BufferedReader(new FileReader(new File(FILE_INPUT)))) {
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

			int chapter = getChapterQuoteAppears(
					"“Such a trial, dear Sir, With no jury or judge, would be wasting our breath.”", lines);

			System.out.println("chapter " + chapter);
			
			generateSentence("Alice", lines, words);

			Trie trie = new Trie();
			buildTrieWithAvailableWord(trie);

			System.out.println("\nGet Auto Complete Sentence with 'Alice'\n");
			getAutocompleteSentence("Alice", trie);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
