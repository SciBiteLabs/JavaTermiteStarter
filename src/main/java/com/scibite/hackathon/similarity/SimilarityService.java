package com.scibite.hackathon.similarity;

import java.io.IOException;
import java.util.List;

/**
 * This class was created by simon on 03/09/2018.
 */
public interface SimilarityService {

    public List<WordSimilarity> similar(String word) throws IOException;
}
