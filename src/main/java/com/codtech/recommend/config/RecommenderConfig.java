package com.codtech.recommend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codtech.recommend.recommender.FallbackRecommender;
import com.codtech.recommend.recommender.MahoutRecommenderService;
import com.codtech.recommend.recommender.RecommenderService;

import java.nio.file.Paths;

@Configuration
public class RecommenderConfig {

	@Value("${recommender.data.path:./data/ratings.csv}")
	private String dataPath;

	@Bean
	public RecommenderService recommenderService() {
		try {
			// try Mahout-based recommender
			MahoutRecommenderService mahout = new MahoutRecommenderService(Paths.get(dataPath).toFile());
			return mahout;
		} catch (Exception e) {
			// graceful fallback
			System.err.println("Mahout recommender initialization failed: " + e.getMessage());
			System.err.println("Falling back to pure-Java recommender.");
			return new FallbackRecommender(Paths.get(dataPath).toFile());
		}
	}
}
