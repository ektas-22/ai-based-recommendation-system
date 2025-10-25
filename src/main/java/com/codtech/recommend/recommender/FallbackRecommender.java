package com.codtech.recommend.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple pure-Java fallback recommender using cosine similarity
 */
public class FallbackRecommender implements RecommenderService {

	private final Map<Long, Map<Long, Double>> userRatings = new HashMap<>();

	// ✅ This is the constructor your config is trying to call
	public FallbackRecommender(File csvFile) {
		loadRatings(csvFile);
	}

	private void loadRatings(File csvFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Expected format: userId,itemId,rating
				String[] parts = line.trim().split(",");
				if (parts.length < 3)
					continue;
				long userId = Long.parseLong(parts[0]);
				long itemId = Long.parseLong(parts[1]);
				double rating = Double.parseDouble(parts[2]);

				userRatings.computeIfAbsent(userId, k -> new HashMap<>()).put(itemId, rating);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load fallback ratings: " + e.getMessage(), e);
		}
	}

	@Override
	public Map<Long, Double> recommend(long userId, int topK) throws Exception {
		Map<Long, Double> targetRatings = userRatings.get(userId);
		if (targetRatings == null) {
			throw new IllegalArgumentException("User not found: " + userId);
		}

		// Compute cosine similarities
		Map<Long, Double> similarities = new HashMap<>();
		for (var entry : userRatings.entrySet()) {
			long otherUser = entry.getKey();
			if (otherUser == userId)
				continue;
			double sim = cosineSimilarity(targetRatings, entry.getValue());
			similarities.put(otherUser, sim);
		}

		// Find top 3 similar users
		List<Long> topUsers = similarities.entrySet().stream()
				.sorted((a, b) -> Double.compare(b.getValue(), a.getValue())).limit(3).map(Map.Entry::getKey)
				.collect(Collectors.toList());

		// Generate recommendations
		Map<Long, Double> scores = new HashMap<>();
		for (long neighbor : topUsers) {
			Map<Long, Double> neighborRatings = userRatings.get(neighbor);
			double sim = similarities.get(neighbor);
			for (var e : neighborRatings.entrySet()) {
				long item = e.getKey();
				if (targetRatings.containsKey(item))
					continue; // skip already rated
				scores.merge(item, e.getValue() * sim, Double::sum);
			}
		}

		// Normalize and return top K
		return scores.entrySet().stream().sorted((a, b) -> Double.compare(b.getValue(), a.getValue())).limit(topK)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
	}

	private double cosineSimilarity(Map<Long, Double> a, Map<Long, Double> b) {
		Set<Long> common = new HashSet<>(a.keySet());
		common.retainAll(b.keySet());
		if (common.isEmpty())
			return 0.0;

		double dot = 0, normA = 0, normB = 0;
		for (long item : common) {
			dot += a.get(item) * b.get(item);
		}
		for (double val : a.values())
			normA += val * val;
		for (double val : b.values())
			normB += val * val;

		return dot / (Math.sqrt(normA) * Math.sqrt(normB));
	}
}
