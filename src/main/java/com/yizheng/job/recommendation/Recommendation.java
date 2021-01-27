package com.yizheng.job.recommendation;

import com.yizheng.job.database.MySQLConnection;
import com.yizheng.job.entity.Item;
import com.yizheng.job.external.GitHubClient;

import java.util.*;

public class Recommendation {
    public List<Item> recommendItems(String userId, double lat, double lon) {
        //we get the user's favorite items' ids, then we get the keywords for each item and sort the keywords by their occurrences,
        //then we get only select the 3 keywords that occur the most numbers of times (keywordList),
        //then using the keywords, we search for jobs, and if the resulting job is not in the favorite list or was not already returned before,
        //we add it to the result to be returned and also add it to the visited list

        List<Item> recommendedItems = new ArrayList<>();

        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);

        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> keywords = connection.getKeywords(itemId);
            for (String keyword : keywords) {
                allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0) + 1);
            }
        }
        connection.close();

        List<Map.Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
        keywordList.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        if (keywordList.size() > 3) {
            keywordList = keywordList.subList(0, 3);
        }

        Set<String> visitedItemIds = new HashSet<>();
        GitHubClient client = new GitHubClient();

        for (Map.Entry<String, Integer> keyword : keywordList) {
            List<Item> items = client.search(lat, lon, keyword.getKey());
            for (Item item : items) {
                if (!favoriteItemIds.contains(item.getId()) && visitedItemIds.add(item.getId())) {
                    recommendedItems.add(item);
                }
            }
        }
        return recommendedItems;
    }
}
