package com.cold.util;

import com.cold.match.FindMatches;
import com.cold.match.NearString;

/**
 * @Auther: ohj
 * @Date: 2019/8/13 14:02
 * @Description:
 */
public class StringUtil {
    public static float similarityScore(String source, String target) {
        float score = 0;
        FindMatches findMatches = new FindMatches(source);//source:原句
        try {
            NearString nearString = findMatches.search(target);
            score = nearString.score / 100f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
}