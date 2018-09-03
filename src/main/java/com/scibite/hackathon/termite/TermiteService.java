package com.scibite.hackathon.termite;

import com.scibite.termitej.common.hit.THit;

import java.io.File;
import java.util.List;

/**
 * This class was created by simon on 03/09/2018.
 */
public interface TermiteService {
    List<THit> termite(String inputData) throws Exception;
    List<THit> termite(File file) throws Exception;
}
