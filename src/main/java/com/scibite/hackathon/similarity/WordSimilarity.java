package com.scibite.hackathon.similarity;

import java.util.Objects;

/**
 * This class was created by simon on 03/09/2018.
 */
public class WordSimilarity implements Comparable<WordSimilarity> {
    private String word;
    private double similarity;

    public WordSimilarity(String word, double similarity) {
        this.word = word;
        this.similarity = similarity;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordSimilarity that = (WordSimilarity) o;
        return Double.compare(that.similarity, similarity) == 0 &&
                Objects.equals(word, that.word);
    }

    @Override public int hashCode() {
        return Objects.hash(word, similarity);
    }

    @Override public int compareTo(WordSimilarity o) {
        double otherSimilarity = o.getSimilarity();
        return Double.compare(otherSimilarity, similarity);
    }

    @Override public String toString() {
        return "WordSimilarity{" +
                "word='" + word + '\'' +
                ", similarity=" + similarity +
                '}';
    }
}
