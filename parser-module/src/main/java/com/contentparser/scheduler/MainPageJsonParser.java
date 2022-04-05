package com.contentparser.scheduler;

import com.common.entity.CategoryData;
import com.common.entity.SiteData;
import com.common.enums.State;
import com.common.repository.CategoryDataRepository;
import com.common.repository.SiteDataRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
@RequiredArgsConstructor
public class MainPageJsonParser {
    private static final Logger logger = LoggerFactory.getLogger(MainPageJsonParser.class);

    @Autowired
    private SiteDataRepository siteDataRepo;

    @Autowired
    private CategoryDataRepository categoryDataRepo;

    @Transactional
    //@Scheduled(fixedDelay = 1000 * 60 * 60)
    public void getHierarchy() {
        for (SiteData siteData : siteDataRepo.findByState(State.DOWNLOADED)) {
            siteData.setState(State.PARSING);
            siteDataRepo.save(siteData);
            var categoryDataList = getJsonContent(siteData.getJson());
            if (!categoryDataList.isEmpty()) {
                categoryDataRepo.saveAll(categoryDataList);
                siteData.setState(State.PARSED);
            }
            else {
                siteData.setState(State.PARSING_ERROR);
            }
            siteDataRepo.save(siteData);
            logger.info("Categories parsed, state: " + siteData.getState());
        }
    }

    private List<CategoryData> getJsonContent(String json) {
        List<CategoryData> dataList = new ArrayList<>();
        Gson gson = new Gson();
        Queue<JsonElement> queue = new LinkedList<>();
        gson.fromJson(json, JsonObject.class)
                .getAsJsonObject("data")
                .getAsJsonArray("catalog")
                .forEach(queue::add);

        while (!queue.isEmpty()) {
            JsonObject jsonObj = queue.remove().getAsJsonObject();
            JsonArray jsonArray = jsonObj.getAsJsonArray("childNodes");
            if (jsonArray.size() == 0) {
                CategoryData categoryData = gson.fromJson(jsonObj, CategoryData.class);
                categoryData.setState(State.QUEUED);
                dataList.add(categoryData);
            }
            else
                jsonArray.forEach(queue::add);
        }

        return dataList;
    }
}
