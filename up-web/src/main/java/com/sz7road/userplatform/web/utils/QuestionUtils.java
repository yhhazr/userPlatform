package com.sz7road.userplatform.web.utils;

import com.sz7road.userplatform.pojos.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuestionUtils {

    public static Map<String, String> getMap(List<Question> list){
        Map<String, String> map = new HashMap<String, String>();
        for(Question q : list){
            map.put(q.getQuestion(), q.getAnswer());
        }
        return map;
    }

    public static Map<String, String> getMap(Question... questions){
        if(null == questions)
            return null;

        Map<String, String> map = new HashMap<String, String>();
        for(Question q : questions){
            map.put(q.getQuestion(), q.getAnswer());
        }
        return map;
    }
}
