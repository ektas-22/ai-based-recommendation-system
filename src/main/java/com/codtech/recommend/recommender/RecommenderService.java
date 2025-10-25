package com.codtech.recommend.recommender;

import java.util.List;
import java.util.Map;

public interface RecommenderService {

	/**
	 * Recommend items for userId. Returns map itemId -> score.
	 */
	Map<Long, Double> recommend(long userId, int topK) throws Exception;

	default List<Long> recommendTopKIds(long userId, int topK) throws Exception {
		Map<Long, Double> mp = recommend(userId, topK);
		return mp.entrySet().stream().sorted((a, b) -> Double.compare(b.getValue(), a.getValue())).limit(topK)
				.map(Map.Entry::getKey).toList();
	}
}
