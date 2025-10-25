package com.codtech.recommend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.codtech.recommend.recommender.RecommenderService;

import java.util.Map;

@RestController
@RequestMapping("/recommend")
public class RecommendationController {

	private final RecommenderService recommender;

	@Autowired
	public RecommendationController(RecommenderService recommender) {
		this.recommender = recommender;
	}

	/**
	 * GET /recommend/{userId}?topK=5
	 */
	@GetMapping("/{userId}")
	public Map<Long, Double> recommend(@PathVariable("userId") long userId,
			@RequestParam(name = "topK", defaultValue = "5") int topK) throws Exception {
		return recommender.recommend(userId, topK);
	}
}