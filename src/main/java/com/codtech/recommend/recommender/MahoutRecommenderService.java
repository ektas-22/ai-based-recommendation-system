package com.codtech.recommend.recommender;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;

public class MahoutRecommenderService implements RecommenderService {

    private final DataModel model;
    private final Recommender recommender;

    public MahoutRecommenderService(File csvFile) throws Exception {
        if (csvFile == null || !csvFile.exists()) {
            throw new IllegalArgumentException(
                "Ratings file not found: " + (csvFile == null ? "null" : csvFile.getAbsolutePath()));
        }

        // Mahout FileDataModel expects: userID,itemID,preference (no header)
        model = new FileDataModel(csvFile);
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);
        recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        System.out.println("Mahout recommender initialized with data: " + csvFile.getAbsolutePath());
    }

    @Override
    public Map<Long, Double> recommend(long userId, int topK) throws Exception {
        List<RecommendedItem> recs = recommender.recommend(userId, topK);
        Map<Long, Double> out = new HashMap<>();
        for (RecommendedItem ri : recs) {
            out.put(ri.getItemID(), (double) ri.getValue());
        }
        return out;
    }
}
