package coursepath.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
	
	
	class TrieNode {
		Map<Character, TrieNode> childrens;
		char ch;
		boolean isEndOfWord;
		TrieNode() {
			this.ch = ' ';
			childrens = new HashMap<>();
		}
		
		TrieNode(char ch) {
			this.ch = ch;
			childrens = new HashMap<>();
		}
	}
	
	TrieNode root;
	
	Trie() {
		root = new TrieNode();
	}
	
	public void insert(String str) {
		char[] cArr = str.toCharArray();
		
		TrieNode root1 = root;
		for(int i = 0; i < cArr.length; i++) {
			
			char ch = cArr[i] == ' ' ? '#' : cArr[i];
			
			if (!root1.childrens.containsKey(ch)) {
				root1.childrens.put(ch, new TrieNode(ch));
			}
			
			root1 = root1.childrens.get(ch);
		}
		root1.isEndOfWord = true;
		root1.childrens.put('#', new TrieNode('#'));
	}
	
	public List<String> search(String sentence) {
		char[] cArr = sentence.toCharArray();
		
		TrieNode root1 = root;
		for(int i = 0; i < cArr.length; i++) {
			char ch = cArr[i] == ' ' ? '#' : cArr[i];
			if (!root1.childrens.containsKey(ch)) return new ArrayList<>();
			
			root1 = root1.childrens.get(ch);
		}
		
		List<String> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append(sentence);
		dfs(root1, list, sb);
		
		return list;
	}

	private void dfs(TrieNode root, List<String> list, StringBuilder sb) {
		if (root.childrens == null || root.childrens.size() == 0) {
			list.add(sb.toString());
			return;
		}
		
		Map<Character, TrieNode> childrens = root.childrens;
		for(Character key : childrens.keySet()) {
			TrieNode children = childrens.get(key);
			sb.append(children.ch);
			dfs(children, list, sb);
			sb.delete(sb.length() - 1, sb.length());
		}
	}
	
	
	
}
