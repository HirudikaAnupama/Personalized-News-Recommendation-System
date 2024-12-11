package org.example.personalizednewsrecommendationsystem.ArticleManagement;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ArticleCategory {

    private final ConcurrencyManagement concurrencyManagement;  // Concurrency management class



    // Constructor to initialize the concurrency manager
    public ArticleCategory(ConcurrencyManagement concurrencyManagement) {
        this.concurrencyManagement = concurrencyManagement;
    }



    // Method to categorize multiple articles concurrency
    public Map<String, String> handleCategorizationConcurrency(List<String> description) {
        Map<String, String> categorizedResults = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(description.size());

        for (String article : description) {
            concurrencyManagement.handleTask(() -> {
                try {
                    String category = categorizeArticle(article);
                    categorizedResults.put(article, category);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            // Wait for all tasks to complete
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return categorizedResults;
    }




    // Basic NLP algorithm for provide news article categorize when fetch articles
    public String categorizeArticle(String text) {
        text = text.toLowerCase();

        // Define keywords for each category
        String[] technologyKeywords = {
                "technology", "software", "internet", "computer", "gadget", "hardware", "blockchain", "cybersecurity",
                "electronics", "automation", "virtual reality", "vr", "ar", "robotics", "5g", "startup", "iot",
                "smartphones", "web development", "programming", "networking", "semiconductors", "cloud computing",
                "e-commerce", "gaming technology", "digital devices", "smart home", "wearables", "data storage", "encryption"
        };


        String[] aiKeywords = {
                "artificial intelligence", "ai", "neural network", "deep learning", "natural language processing",
                "nlp", "computer vision", "automation", "ai ethics", "generative ai", "chatbot", "ai models",
                "algorithm", "data training", "machine learning", "predictive modeling", "artificial neural network",
                "ai-generated", "artificial agent"
        };

        String[] healthKeywords = {
                "health", "medicine", "wellness", "fitness", "disease", "nutrition", "hospital", "doctor",
                "mental health", "public health", "surgery", "therapy", "vaccine", "virus", "epidemic", "pharmacy",
                "cardiology", "diet", "exercise", "healthcare", "clinic", "epidemiology", "immunization", "diagnosis",
                "symptom", "infection", "prevention", "biotechnology", "covid", "pandemic", "cancer research"
        };

        String[] sportsKeywords = {
                "sports", "game", "athlete", "football", "soccer", "basketball", "olympics", "tournament",
                "cricket", "tennis", "rugby", "golf", "baseball", "hockey", "league", "athletics", "marathon",
                "medal", "championship", "score", "goal", "team", "competition", "world cup", "boxing", "swimming",
                "cycling", "fifa", "uefa", "nba", "premier league", "super bowl", "match", "play", "coach"
        };

        String[] politicsKeywords = {
                "politics", "election", "government", "policy", "diplomacy", "president", "congress", "senate",
                "minister", "parliament", "democracy", "republic", "campaign", "law", "constitution", "international relations",
                "embassy", "treaty", "un", "nato", "alliance", "legislation", "bill", "party", "leader", "conservative",
                "liberal", "socialist", "reform", "voter", "referendum", "ballot", "governor", "ambassador", "debate", "politician"
        };

        String[] financeKeywords = {
                "finance", "economy", "stock", "market", "investment", "bank", "cryptocurrency", "trade",
                "budget", "tax", "income", "revenue", "insurance", "loan", "mortgage", "interest rate", "fund", "credit",
                "forex", "capital", "inflation", "deflation", "gross domestic product", "gdp", "dividend", "debt",
                "bond", "wealth", "accounting", "audit", "spending", "venture capital", "ipo", "wall street", "hedge fund",
                "fintech", "economics", "financial"
        };

        String[] entertainmentKeywords = {
                "movie", "music", "entertainment", "film", "celebrity", "hollywood", "award", "show",
                "theater", "comedy", "drama", "tv", "series", "album", "song", "concert", "actor", "actress",
                "director", "producer", "streaming", "netflix", "cinema", "festival", "reality show", "bollywood",
                "broadway", "oscars", "emmy", "grammy", "media", "binge-watch", "fan", "celebration", "viral",
                "musical", "video", "blockbuster", "premiere"
        };

        String[] fashionKeywords = {
                "fashion", "style", "designer", "runway", "couture", "trend", "model", "outfit",
                "clothing", "brand", "wardrobe", "accessory", "jewelry", "beauty", "makeup", "hairstyle",
                "vogue", "cosmetics", "catwalk", "fashion week", "textile", "vintage", "boutique", "glamour",
                "outfits", "styling", "runway show"
        };

        String[] educationKeywords = {
                "education", "school", "university", "college", "learning", "study", "teaching", "curriculum",
                "classroom", "student", "teacher", "course", "scholarship", "academic", "research", "graduate",
                "degree", "e-learning", "online course", "tuition", "knowledge", "pedagogy", "skill", "training",
                "exam", "online learning", "study material", "college admission"
        };

        String[] tourismKeywords = {
                "tourism", "travel", "destination", "adventure", "holiday", "trip", "vacation", "beach",
                "hotel", "resort", "itinerary", "cruise", "flight", "landmark", "sightseeing", "tourist",
                "backpacking", "road trip", "national park", "culture", "exploration", "getaway", "nature",
                "world heritage", "ecotourism", "hospitality", "guide", "tour"
        };

        // Count keyword matches for each category
        int techCount = countKeywordMatches(text, technologyKeywords);
        int aiCount = countKeywordMatches(text, aiKeywords);
        int healthCount = countKeywordMatches(text, healthKeywords);
        int sportsCount = countKeywordMatches(text, sportsKeywords);
        int politicsCount = countKeywordMatches(text, politicsKeywords);
        int financeCount = countKeywordMatches(text, financeKeywords);
        int entertainmentCount = countKeywordMatches(text, entertainmentKeywords);
        int fashionCount = countKeywordMatches(text, fashionKeywords);
        int educationCount = countKeywordMatches(text, educationKeywords);
        int tourismCount = countKeywordMatches(text, tourismKeywords);


        // Determine the category with the highest match count
        int maxCount = Math.max(
                Math.max(techCount, aiCount),
                Math.max(Math.max(healthCount, sportsCount),
                        Math.max(Math.max(politicsCount, financeCount),
                                Math.max(Math.max(entertainmentCount, fashionCount),
                                        Math.max(educationCount, tourismCount))))
        );

        if (maxCount == 0) {
            return "General"; // Default category if no keywords match
        } else if (maxCount == techCount) {
            return "Technology";
        } else if (maxCount == aiCount) {
            return "Artificial intelligence ";
        } else if (maxCount == healthCount) {
            return "Health";
        } else if (maxCount == sportsCount) {
            return "Sports";
        } else if (maxCount == politicsCount) {
            return "Politics";
        } else if (maxCount == financeCount) {
            return "Finance";
        } else if (maxCount == entertainmentCount) {
            return "Entertainment";
        } else if (maxCount == fashionCount) {
            return "Fashion";
        } else if (maxCount == tourismCount) {
            return "Tourism";
        } else {
            return "Education";
        }
    }



    // Keyword count method
    private int countKeywordMatches(String text, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                count++;
            }
        }
        return count;
    }
}
