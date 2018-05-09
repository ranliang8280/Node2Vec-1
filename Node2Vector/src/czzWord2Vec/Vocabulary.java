package czzWord2Vec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 �ʵ䣬1.ͳ�������еĵ��ʣ�2.�ʵ����˵�Ƶ�ʣ�3.ͳ�ƴ�Ƶ��4.������Ҫ������ƵHuffman��
 @author CZZ*/
public class Vocabulary<T> implements IVocabulary{

	/**
	 �ʵ�*/
	private ArrayList<HWord<T> > _vocabulary;
	
	/**
	 �ʵ��дʵ����������Լ�¼�ʵ����Ƿ����ĳ�����ʣ�Ҳ���Լ�¼��������ڴʵ��еĸ�λ�ñ��*/
	private HashMap<T, Integer> _wordIndex;
	
	/**
	 �ʵ䳤�ȣ���Ϊ�п��ܹ��˵���Ƶ�ʣ�����չʾ���ȿ��ܻ����*/
	private int _vocabularyLength;
	
	/**
	 ��С��Ƶ��С����С��Ƶ�Ĵʻᱻɾ��*/
	private int _lessFrequency;
	
	/**
	 ��һ���ʣ����ʵ䴦������״̬��StartPointer���ǵ�һ�����ڵ���Lessfrequency�Ĵʵ������ţ������ַƫ������,���δ���ˣ�Ϊ0*/
	private int _startPointer;
	
	/**
	 �Ƚ�����
	 * @param <E> ���ʵ�����*/
	class WordFrequencyComparer<E> implements Comparator<Word<E>>  {

		public int compare(Word<E> o1, Word<E> o2) {
			int ret = o1.wordFrequency - o2.wordFrequency;
			if(ret > 0) ret = 1;
			else if(ret <0) ret = -1;
			return ret;
		}
	}
	
	private boolean _isSorted;
	
	/*================================���� methods================================*/
	
	/**
	 * �ղ������췽��*/
	public Vocabulary() {
		this._vocabulary = new ArrayList<HWord<T> >();
		this._wordIndex = new HashMap<T, Integer>();
		this._isSorted = false;
		this._lessFrequency = 0;				//�����˵�Ƶ��
		this._startPointer = 0;
		this._vocabularyLength = 0;
	}
	
	/**
	 * ���½����ʵ������*/
	private void reIndex() {
		_wordIndex.clear();
		for(int i = 0; i < _vocabulary.size(); i++) {
			_wordIndex.put(_vocabulary.get(i).word, i);			//��������
		}
	}
	
	/**
	 * @return �ʵ��Ƿ��մ�Ƶ����*/
	public boolean isSorted() {
		return _isSorted;
	}

	/**
	 * @return �ʵ��ܳ��ȣ������˵�Ƶ���ܳ���*/
	public int getVocabularyFullSize() {
		return _vocabulary.size();
	}
	
	/**
	 * @param word �����Ҵ�
	 * @return �ʵ����Ƿ���ڴ���word*/
	public boolean hasWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) ret = true;
		return ret;
	}
	
	/**
	 * @param word �����Ҵ�
	 * @return ���Ҵ���word�ڴʵ��е��������*/
	public int getWordIndex(T word) {
		int ret = -1;
		if(hasWord(word)) ret = _wordIndex.get(word);
		return ret;
	}
	
	/**
	 * @param word ����
	 * @return word�ڴʵ��д洢��ʵ��*/
	public HWord<T> getWord(T word) {
		HWord<T> ret = null;
		if(hasWord(word)) ret = _vocabulary.get(_wordIndex.get(word));
		return ret;
	}
	
	/**
	 * @param word ����
	 * @return word�ڴʵ��д洢��������*/
	public HWord<T> getWordByIndex(int index) {
		HWord<T> ret = null;
		if(_vocabulary != null && index < _vocabulary.size()) ret = _vocabulary.get(index);
		return ret;
	}
	
	/**
	 * ������װ�شʵ䣬���Ҽ����Ƶ
	 * @param words ��һά�Ǿ��Ӽ��ϣ��ڶ�ά�Ǿ����е�ÿ����*/
	public void loadVocabulary(T[][] words) {
		int i, j;
		for(i = 0; i < words.length; i++) {
			for(j = 0; j < words[i].length; j++) {
				addWord(words[i][j]);
			}
		}
		_isSorted = false;
	}
	
	/**
	 * �Ӿ����б�װ�شʵ䣬���Ҽ����Ƶ
	 * @param words ��һά�Ǿ��Ӽ��ϣ��ڶ�ά�Ǿ����е�ÿ����*/
	public void loadVocabulary(ArrayList<T[]> words) {
		T[] arr;
		for(int i = 0; i < words.size(); i++) {
			arr = words.get(i);
			for(int j = 0; j < arr.length; j++) {
				addWord(arr[j]);
			}
		}
		_isSorted = false;
	}
	
	/**
	 * ��ʵ�������1���������Ѿ��������Ƶ+1
	 * @param word �����ӵĴ���
	 * @return ���ӽ��*/
	public boolean addWord(T word) {
		boolean ret = false;
		if(_wordIndex.containsKey(word)) {
			_vocabulary.get(_wordIndex.get(word)).wordFrequency++;		//��Ƶ+1
		}
		else {
			_vocabulary.add(new HWord<T>(word));					//����ʵ�
			_wordIndex.put(word, _vocabulary.size() - 1);		//��������
		}
		_isSorted = false;					//��Ϊδ����״̬
		return ret;
	}
	
	/**
	 * ��ʵ�������һ�������е����д���
	 * @param word �����Ӵ���ľ���
	 * @return ���ӽ��*/
	public boolean addWord(T[] words) {
		for(int i = 0; i < words.length; i++) {
			addWord(words[i]);
		}
		_isSorted = false;
		return true;
	}
	
	/**
	 * �Ƴ�ĳ����
	 * @param word ת��ɾ���Ĵ���
	 * @return �Ƴ��ɹ�*/
	public boolean removeWord(T word) {
		boolean ret = false;
		if(hasWord(word)) {
			int index = _wordIndex.get(word);
			_wordIndex.remove(word);
			_vocabulary.remove(index);
			reIndex();			//��Ҫ���½�������
			ret = true;
		}
		//_isSorted = false;			//���ı�����ʽ
		return ret;
	}
	
	/**
	 * ���ݴʵ��д�Ƶ���򣬲����ؽ�����*/
	public void sortVocabulary() {
		_vocabulary.sort(new WordFrequencyComparer<T>());
		reIndex();
		_isSorted = true;
	}

	/**
	 * ��ôʵ���ÿ�������Huffman����
	 * @return �������*/
	public boolean getHuffmanCode() {
		boolean ret = false;
		if(this.getVocabularyLength() < 2) return ret;		//û�нڵ���ߣ�ֻ��һ���ڵ㣬�����룬����false
		if(_isSorted) {				//�Ѿ������
			int i, j;		//i����Ҷ�ӽڵ㣬j�����м�ڵ�
			HWord<T> min1, min2, tempmin;			//��С�������ڵ������
			int min1n, min2n;				//��û�в���Huffman������С�������ڵ������
			HWord<T> parentNode;
			ArrayList<HWord<T> > nodeList = new ArrayList<HWord<T> >();				//Huffman���м�ڵ㣬n-1��
			HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();		//��¼ĳ���ڵ�ĸ��ڵ�
			int vLength = this.getVocabularyLength();						//�ʵ䳤��
			min1 = _vocabulary.get(_startPointer + 0);
			min2 = _vocabulary.get(_startPointer + 1);
			min1.code.clear();					//��ձ���
			min2.code.clear();
			min1.code.add(new Byte((byte) 0));			//��С�����Һ��ӣ����0
			min2.code.add(new Byte((byte) 1));			//��С�������ӣ����1
			parentNode = new HWord<T>();				//���ɵ�һ���м�ڵ�
			parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//���ڵ�=���Һ���ֵ�ĺ�
			nodeList.add(parentNode);
			parentMap.put(_startPointer + 0, vLength);			//��0���롰1���ĸ��ڵ㶼��nodeList��0��Ԫ��
			parentMap.put(_startPointer + 1, vLength);
			i = _startPointer + 2;							//Ҷ�ӽڵ�ָ��λ��
			j = 0;											//�м�ڵ�ָ��λ��
			int k;
			for(k = _startPointer + 1; k < vLength - 1; k++) {
				tempmin = nodeList.get(j);
				if (i < vLength && (min1 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //�Ƚ�Ҷ�ӽڵ����м�ڵ�
					min1n = i;
					i++;
				}
				else {		
					min1 = tempmin;
					min1n = vLength + j;
					j++;
				}
				tempmin = nodeList.get(j);			//Ѱ�Ҵ���С�ڵ�Ĺ���
				if (i < vLength && (min2 = _vocabulary.get(i)).wordFrequency < tempmin.wordFrequency) { //�Ƚ�Ҷ�ӽڵ����м�ڵ�
					min2n = i;
				    i++;
				}
				else {		
					min2 = tempmin;
					min2n = vLength + j;
					j++;
				}
				min1.code.clear();					//��ձ���
				min2.code.clear();
				min1.code.add(new Byte((byte) 0));			//��С�����Һ��ӣ����0
				min2.code.add(new Byte((byte) 1));			//��С�������ӣ����1
				parentNode = new HWord<T>();
				parentNode.wordFrequency = min1.wordFrequency + min2.wordFrequency;		//���ڵ�=���Һ���ֵ�ĺ�
				nodeList.add(parentNode);
				parentMap.put(min1n, vLength + k);			//min1��min2�ĸ��ڵ㶼��nodeList��0��Ԫ��
				parentMap.put(min2n, vLength + k);			//Ҳ����j����vLength���߼��ϰ�Ҷ�ӽڵ����м�ڵ������һ��ͳһ����
			}
			HWord<T> leafNode;			//Ҷ�ӽڵ�
			int parentIndex = -1;			//˫�׽ڵ�������
			for(k = 0; k < vLength; k++) {				//����Ҷ�ӽڵ㣬��Ҷ�ӽڵ����
				leafNode = _vocabulary.get(k);			//��ǰҶ�ӽڵ�
				parentIndex = parentMap.get(k);			//��ǰҶ�ӽڵ�ĸ��ڵ�
				while(parentIndex != vLength * 2 - 2) {
					parentNode = nodeList.get(parentIndex - vLength);		//��ȡ���ڵ�
					leafNode.code.add(0, parentNode.code.get(0));	//���ڵ�;
					leafNode.point.add(0, parentIndex - vLength);
					parentIndex = parentMap.get(parentIndex);			//���ڵ�ĸ��ڵ�
				}
				leafNode.point.add(0, vLength - 2);
			}
			nodeList.clear();
			parentMap.clear();
			ret = true;						//�����ɹ�
		}
		
		return ret;
	}

	/**
	 * @return �ʵ䳤��*/
	public int getVocabularyLength() {
		this._vocabularyLength = this._vocabulary.size() - this._startPointer;
		return _vocabularyLength;
	}

	/**
	 * @return ���õ���С��Ƶ*/
	public int getLessFrequency() {
		return _lessFrequency;
	}

	/**
	 * @return �ʵ������У����ڵ�����С��Ƶ����ʼƫ����*/
	public int getStartPointer() {
		return _startPointer;
	}
	
	/**
	 * ���˵�Ƶ�ʣ���������lessFrequency��֮���������δ���򣩣�֮�����ÿ�ʼָ��
	 * @param lessFrequency ������Ȼ��lessFrequency���˵�Ƶ��
	 * @return true���˳ɹ�������falseʧ��*/
	public boolean frequencyFilter(int lessFrequency) {
		boolean ret = false;
		if(lessFrequency < 0) lessFrequency = 0;			//Ӧ�ô��ڵ�����
		this._lessFrequency = lessFrequency;
		if(!this._isSorted) {			//���û������
			this.sortVocabulary();		//���ʵ�����
		}
		if(lessFrequency == 0) {						//������
			this._startPointer = -1;
			this._vocabularyLength = _vocabulary.size();
			ret = true;
		}
		else if(_vocabulary.size() > 0 && lessFrequency > _vocabulary.get(_vocabulary.size() - 1).wordFrequency) {		//ȫ�����˵�����������
			this._startPointer = _vocabulary.size() - 1;
			this._vocabularyLength = 0;
		}
		else {
			Iterator<HWord<T>> iter = this._vocabulary.iterator();
			this._startPointer = 0;
			while(iter.hasNext()) {
				if(iter.next().wordFrequency >= lessFrequency) break;			//���ڣ����ڣ���С��Ƶ���ᱻ����
				this._startPointer++;
			}
			this._vocabularyLength = this._vocabulary.size() - this._startPointer;			//�����ʵ䳤��
			ret = true;
		}
		return ret;
	}
}